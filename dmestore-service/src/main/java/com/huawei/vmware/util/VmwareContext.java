// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.huawei.vmware.util;


import com.huawei.vmware.mo.DatacenterMO;
import com.huawei.vmware.mo.DatastoreFile;
import com.vmware.connection.helpers.builders.ObjectSpecBuilder;
import com.vmware.connection.helpers.builders.PropertyFilterSpecBuilder;
import com.vmware.connection.helpers.builders.PropertySpecBuilder;
import com.vmware.connection.helpers.builders.TraversalSpecBuilder;
import com.vmware.pbm.PbmPortType;
import com.vmware.pbm.PbmServiceInstanceContent;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VimPortType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * VmwareContext
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class VmwareContext {
    private static final Logger logger = LoggerFactory.getLogger(VmwareContext.class);
    private static final int MAX_CONNECT_RETRY = 5;
    private static final int CONNECT_RETRY_INTERVAL = 1000;
    /**
     * 1M
     **/
    private static final int CHUNK_SIZE = 1 * 1024 * 1024;
    private static volatile int outstandingCount = 0;
    private final VmwareClient vimClient;
    private final String serverAddress;
    private final int intValue = 100;
    private final int sleep = 1000;
    private DatacenterVmwareMoFactory datacenterVmwareMoFactory;

    private final Map<String, Object> stockMap = new HashMap<String, Object>();

    private VmwareContextPool pool;
    private String poolKey;


    static {
        try {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new TrustAllManager();
            trustAllCerts[0] = tm;
            javax.net.ssl.SSLContext sc = SslUtil.getSslContext();
            sc.init(null, trustAllCerts, null);
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(new SecurtySslSocketFactory(sc));

            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unexpected exception ", e);
        } catch (KeyManagementException e) {
            logger.error("Unexpected exception ", e);
        }
    }

    /**
     * VmwareContext
     *
     * @param client  client
     * @param address address
     */
    public VmwareContext(VmwareClient client, String address) {
        assert client != null : "Invalid parameter in constructing VmwareContext object";

        vimClient = client;
        serverAddress = address;

        registerOutstandingContext();
        if (logger.isInfoEnabled()) {
            logger.info("New VmwareContext object, current outstanding count: " + getOutstandingContextCount());
        }
    }

    /**
     * VmwareContext
     */
    public VmwareContext() {
        vimClient = new VmwareClient("vcentercontext");
        serverAddress = "";
    }

    /**
     * clearStockObjects
     */
    public void clearStockObjects() {
        synchronized (stockMap) {
            stockMap.clear();
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public VimPortType getService() {
        return vimClient.getService();
    }

    public PbmPortType getPbmService() {
        return vimClient.getPbmService();
    }

    public ServiceContent getServiceContent() {
        return vimClient.getServiceContent();
    }

    public PbmServiceInstanceContent getPbmServiceContent() {
        return vimClient.getPbmServiceContent();
    }

    public ManagedObjectReference getPropertyCollector() {
        return vimClient.getPropCol();
    }

    public ManagedObjectReference getRootFolder() {
        return vimClient.getRootFolder();
    }

    public VmwareClient getVimClient() {
        return vimClient;
    }

    /**
     * setPoolInfo
     *
     * @param param1 pool
     * @param param2 poolKey
     */
    public void setPoolInfo(VmwareContextPool param1, String param2) {
        this.pool = param1;
        this.poolKey = param2;
    }

    public VmwareContextPool getPool() {
        return pool;
    }

    public String getPoolKey() {
        return poolKey;
    }

    /**
     * idleCheck
     *
     * @throws Exception Exception
     */
    public void idleCheck() throws Exception {
        getRootFolder();
    }

    public static int getOutstandingContextCount() {
        return outstandingCount;
    }

    /**
     * registerOutstandingContext
     */
    public static void registerOutstandingContext() {
        outstandingCount++;
    }

    /**
     * unregisterOutstandingContext
     */
    public static void unregisterOutstandingContext() {
        outstandingCount--;
    }

    /**
     * path in format of <datacenter name>/<datastore name>
     *
     * @param inventoryPath inventoryPath
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference getDatastoreMorByPath(String inventoryPath) throws Exception {
        assert inventoryPath != null;

        String[] tokens;
        String regex = "/";
        if (inventoryPath.startsWith(regex)) {
            tokens = inventoryPath.substring(1).split(regex);
        } else {
            tokens = inventoryPath.split(regex);
        }

        if (tokens == null || tokens.length != 2) {
            logger.error("Invalid datastore inventory path. path: ", inventoryPath);
            return null;
        }

        DatacenterMO dcMo = datacenterVmwareMoFactory.build(this, tokens[0]);
        if (dcMo.getMor() == null) {
            logger.error("Unable to locate the datacenter specified in path: " + inventoryPath);
            return null;
        }

        return dcMo.findDatastore(tokens[1]);
    }

    /**
     * waitForTaskProgressDone
     *
     * @param morTask morTask
     * @throws Exception Exception
     */
    public void waitForTaskProgressDone(ManagedObjectReference morTask) throws Exception {
        while (true) {
            TaskInfo tinfo = (TaskInfo) vimClient.getDynamicProperty(morTask, "info");
            Integer progress = tinfo.getProgress();
            if (progress == null) {
                break;
            }

            if (progress.intValue() >= intValue) {
                break;
            }

            Thread.sleep(sleep);
        }
    }

    /**
     * getResourceContent
     *
     * @param urlString urlString
     * @return byte[]
     * @throws Exception Exception
     */
    public byte[] getResourceContent(String urlString) throws Exception {
        HttpURLConnection conn = getHttpConnection(urlString);
        InputStream in = conn.getInputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[CHUNK_SIZE];
        int len = 0;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }

    /**
     * composeDatastoreBrowseUrl
     *
     * @param dcName   dcName
     * @param fullPath fullPath
     * @return String
     */
    public String composeDatastoreBrowseUrl(String dcName, String fullPath) {
        DatastoreFile dsFile = new DatastoreFile(fullPath);
        return composeDatastoreBrowseUrl(dcName, dsFile.getDatastoreName(), dsFile.getRelativePath());
    }

    /**
     * composeDatastoreBrowseUrl
     *
     * @param dcName        dcName
     * @param datastoreName datastoreName
     * @param relativePath  relativePath
     * @return String
     */
    public String composeDatastoreBrowseUrl(String dcName, String datastoreName, String relativePath) {
        assert relativePath != null;
        assert datastoreName != null;

        StringBuffer sb = new StringBuffer();
        sb.append("https://");
        sb.append(serverAddress);
        sb.append("/folder/");
        sb.append(relativePath);
        try {
            sb.append("?dcPath=").append(URLEncoder.encode(dcName, "UTF-8"));
            sb.append("&dsName=").append(URLEncoder.encode(datastoreName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Unable to encode URL. dcPath : " + dcName + ", dsName :" + datastoreName, e);
        }
        return sb.toString();
    }

    /**
     * getHttpConnection
     *
     * @param urlString urlString
     * @return HttpURLConnection HttpURLConnection
     * @throws Exception Exception
     */
    public HttpURLConnection getHttpConnection(String urlString) throws Exception {
        return getHttpConnection(urlString, "GET");
    }

    /**
     * getHttpConnection
     *
     * @param urlString  urlString
     * @param httpMethod httpMethod
     * @return HttpURLConnection
     * @throws Exception Exception
     */
    public HttpURLConnection getHttpConnection(String urlString, String httpMethod) throws Exception {
        String cookie = vimClient.getServiceCookie();
        if (cookie == null) {
            throw new Exception("No cookie is found in vmware web service request context!");
        }
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);
        conn.addRequestProperty("Cookie", cookie);
        conn.setRequestMethod(httpMethod);
        connectWithRetry(conn);
        return conn;
    }

    private static void connectWithRetry(HttpURLConnection conn) throws Exception {
        boolean connected = false;
        for (int i = 0; i < MAX_CONNECT_RETRY && !connected; i++) {
            try {
                conn.connect();
                connected = true;
                logger.info("Connected, conn: " + conn.toString() + ", retry: " + i);
            } catch (ConnectException e) {
                logger.warn(
                    "Unable to connect, conn: " + conn.toString() + ", message: " + e.toString() + ", retry: " + i);

                try {
                    Thread.sleep(CONNECT_RETRY_INTERVAL);
                } catch (InterruptedException ex) {
                    logger.debug("[ignored] interupted while connecting.");
                }
            }
        }

        if (!connected) {
            throw new Exception("Unable to connect to " + conn.toString());
        }
    }

    /**
     * close
     */
    public void close() {
        clearStockObjects();
        try {
            logger.info("Disconnecting VMware session");
            vimClient.disconnect();
        } catch (SOAPFaultException sfe) {
            logger.debug("Tried to disconnect a session that is no longer valid");
        } catch (Exception e) {
            logger.warn("Unexpected exception: ", e);
        } finally {
            if (pool != null) {
                pool.unregisterContext(this);
            }
            unregisterOutstandingContext();
        }
    }

    /**
     * TrustAllManager
     *
     * @author Administrator
     * @since 2020-12-10
     */
    public static class TrustAllManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
            return;
        }
    }

    /**
     * Returns all the MOREFs of the specified type that are present under the
     * folder
     *
     * @param folder    {@link com.vmware.vim25.ManagedObjectReference} of the folder to begin the searchfrom
     * @param morefType Type of the managed entity that needs to be searched
     * @return Map of name and MOREF of the managed objects present. If none
     * exist then empty Map is returned
     *
     * @throws com.vmware.vim25.InvalidPropertyFaultMsg InvalidPropertyFaultMsg
     * @throws com.vmware.vim25.RuntimeFaultFaultMsg    RuntimeFaultFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> inFolderByType(
        final ManagedObjectReference folder, final String morefType, final RetrieveOptions retrieveOptions
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        final PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(folder, morefType, "name");

        // reuse this property collector again later to scroll through results
        final ManagedObjectReference propertyCollector = getPropertyCollector();

        RetrieveResult results = getService().retrievePropertiesEx(
            propertyCollector,
            Arrays.asList(propertyFilterSpecs),
            retrieveOptions);

        final List<Pair<ManagedObjectReference, String>> tgtMoref =
            new ArrayList<>();
        while (results != null && !results.getObjects().isEmpty()) {
            resultsToTgtMorefList(results, tgtMoref);
            final String token = results.getToken();

            // if we have  token, we can scroll through additional results, else there's nothing to do.
            results =
                (token != null)
                    ? getService().continueRetrievePropertiesEx(propertyCollector, token) : null;
        }
        return tgtMoref;
    }

    /**
     * inFolderByType
     *
     * @param folder folder
     * @param morefType morefType
     * @return List
     * @throws RuntimeFaultFaultMsg RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg InvalidPropertyFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> inFolderByType(ManagedObjectReference folder, String morefType)
        throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return inFolderByType(folder, morefType, new RetrieveOptions());
    }

    private void resultsToTgtMorefList(RetrieveResult results, List<Pair<ManagedObjectReference, String>> tgtMoref) {
        List<ObjectContent> objectCont = (results != null) ? results.getObjects() : null;

        if (objectCont != null) {
            for (ObjectContent oc : objectCont) {
                ManagedObjectReference mr = oc.getObj();
                String entityNm = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        entityNm = (String) dp.getVal();
                    }
                }
                tgtMoref.add(new Pair<>(mr, entityNm));
            }
        }
    }

    /**
     * propertyFilterSpecs
     *
     * @param container container
     * @param morefType morefType
     * @param morefProperties morefProperties
     * @return PropertyFilterSpec[]
     * @throws RuntimeFaultFaultMsg RuntimeFaultFaultMsg
     */
    public PropertyFilterSpec[] propertyFilterSpecs(
        ManagedObjectReference container,
        String morefType,
        String... morefProperties
    ) throws RuntimeFaultFaultMsg {
        ManagedObjectReference viewManager = getServiceContent().getViewManager();
        ManagedObjectReference containerView =
            getService().createContainerView(viewManager, container,
                Arrays.asList(morefType), true);

        return new PropertyFilterSpec[] {
            new PropertyFilterSpecBuilder()
                .propSet(
                    new PropertySpecBuilder()
                        .all(Boolean.FALSE)
                        .type(morefType)
                        .pathSet(morefProperties)
                )
                .objectSet(
                new ObjectSpecBuilder()
                    .obj(containerView)
                    .skip(Boolean.TRUE)
                    .selectSet(
                        new TraversalSpecBuilder()
                            .name("view")
                            .path("view")
                            .skip(false)
                            .type("ContainerView")
                    )
            )
        };
    }
}
