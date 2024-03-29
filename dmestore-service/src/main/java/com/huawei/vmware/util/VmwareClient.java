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

import com.vmware.pbm.PbmPortType;
import com.vmware.pbm.PbmService;
import com.vmware.pbm.PbmServiceInstanceContent;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.MethodFault;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.ObjectUpdate;
import com.vmware.vim25.ObjectUpdateKind;
import com.vmware.vim25.PropertyChange;
import com.vmware.vim25.PropertyChangeOp;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertyFilterUpdate;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RequestCanceled;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.TaskInfoState;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.UpdateSet;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VimService;
import com.vmware.vim25.WaitOptions;
import com.vmware.vise.usersession.ServerInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

/**
 * A wrapper class to handle Vmware vsphere connection and disconnection.
 * <p>
 * DISCLAIMER: This code is partly copied from sample codes that come along with Vmware web service 5.1 SDK.
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class VmwareClient {
    private static final Logger logger = LoggerFactory.getLogger(VmwareClient.class);

    private static VimService vimService;
    private static PbmService pbmService;
    private static final String SVC_INST_NAME = "ServiceInstance";
    private static final String PBMSERVICEINSTANCETYPE = "PbmServiceInstance";
    private static final String PBMSERVICEINSTANCEVALUE = "ServiceInstance";
    private final ManagedObjectReference svcInstRef = new ManagedObjectReference();
    private final ManagedObjectReference pbmInstRef = new ManagedObjectReference();
    private PbmPortType pbmPortType;
    private VimPortType vimPort;
    private String serviceCookie;

    // Timeout in milliseconds
    private int vcenterSessionTimeOut = 1200000;
    private final String reqTimeOut = "com.sun.xml.internal.ws.request.timeout";
    private final String conTimeOut = "com.sun.xml.internal.ws.connect.timeout";
    private boolean isConnected = false;

    /**
     * VmwareClient
     *
     * @param name name
     */
    public VmwareClient(String name) {
    }

    /**
     * TrustAllTrustManager
     *
     * @since 2020-12-10
     */
    public static class TrustAllTrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

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

    static {
        try {
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            vimService = new VimService();
        } catch (Exception e) {
            logger.info("[ignored]" + "failed to trust all certificates blindly: ", e);
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {
        // Create trust manager that does not validate certificate chains:
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = SslUtil.getSslContext();
        javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
        sslsc.setSessionTimeout(0);
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(new SecurtySslSocketFactory(sc));
    }


    /**
     * Establishes session with the virtual center server.
     *
     * @param serverInfo serverInfo
     * @throws Exception the exception
     */
    public void connect(ServerInfo serverInfo) throws Exception {
        svcInstRef.setType(SVC_INST_NAME);
        svcInstRef.setValue(SVC_INST_NAME);
        pbmInstRef.setType(PBMSERVICEINSTANCETYPE);
        pbmInstRef.setValue(PBMSERVICEINSTANCEVALUE);

        vimPort = vimService.getVimPort();
        Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

        String sessionCookie = serverInfo.sessionCookie;
        List<String> values = new ArrayList<>();
        values.add("vmware_soap_session=" + sessionCookie);
        Map<String, List<String>> reqHeadrs =
            new HashMap<>();
        reqHeadrs.put("Cookie", values);
        serviceCookie = serverInfo.sessionCookie;

        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverInfo.serviceUrl);
        ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        ctxt.put(MessageContext.HTTP_REQUEST_HEADERS, reqHeadrs);

        ctxt.put(reqTimeOut, vcenterSessionTimeOut);
        ctxt.put(conTimeOut, vcenterSessionTimeOut);

        pbmPortType = pbmService.getPbmPort();
        Map<String, Object> pbmctxt = ((BindingProvider) vimPort).getRequestContext();


        List<String> pbmvalues = new ArrayList<>();
        values.add("vcSessionCookie=" + sessionCookie);
        Map<String, List<String>> pbmreqHeadrs =
            new HashMap<>();
        pbmreqHeadrs.put("Cookie", pbmvalues);

        pbmctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverInfo.serviceUrl.replace("/sdk", "/pbm"));
        pbmctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        pbmctxt.put(MessageContext.HTTP_REQUEST_HEADERS, pbmreqHeadrs);

        pbmctxt.put(reqTimeOut, vcenterSessionTimeOut);
        pbmctxt.put(conTimeOut, vcenterSessionTimeOut);

        isConnected = true;
    }

    /**
     * public
     *
     * @param url      url
     * @param userName userName
     * @param password password
     * @throws Exception Exception
     */
    public void connect(String url, String userName, String password) throws Exception {
        svcInstRef.setType(SVC_INST_NAME);
        svcInstRef.setValue(SVC_INST_NAME);
        pbmInstRef.setType(PBMSERVICEINSTANCETYPE);
        pbmInstRef.setValue(PBMSERVICEINSTANCEVALUE);

        vimPort = vimService.getVimPort();

        Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        ctxt.put(reqTimeOut, vcenterSessionTimeOut);
        ctxt.put(conTimeOut, vcenterSessionTimeOut);

        ServiceContent serviceContent = vimPort.retrieveServiceContent(svcInstRef);

        Map<String, List<String>> headers = (Map<String, List<String>>) ((BindingProvider) vimPort).getResponseContext()
            .get(MessageContext.HTTP_RESPONSE_HEADERS);
        List<String> cookies = headers.get("Set-cookie");

        vimPort.login(serviceContent.getSessionManager(), userName, password, null);

        if (cookies == null) {
            Map<String, List<String>> responseHeaders =
                (Map<String, List<String>>) ((BindingProvider) vimPort).getResponseContext()
                    .get(MessageContext.HTTP_RESPONSE_HEADERS);
            cookies = responseHeaders.get("Set-cookie");
            if (cookies == null) {
                String msg = "Login successful, but failed to get server cookies from url :[" + url + "]";
                logger.error(msg);
                throw new Exception(msg);
            }
        }

        String cookieValue = cookies.get(0);
        StringTokenizer tokenizer = new StringTokenizer(cookieValue, ";");
        cookieValue = tokenizer.nextToken();
        String pathData = "$" + tokenizer.nextToken();
        String[] tokens = cookieValue.split(";");
        tokens = tokens[0].split("=");
        String extractedCookie = tokens[1];
        extractedCookie = extractedCookie.replace("\"", "");

        serviceCookie = extractedCookie;
        pbmService = new PbmService();

        HeaderHandlerResolver headerResolver = new HeaderHandlerResolver();
        headerResolver.addHandler(new VcSessionHandler(extractedCookie));
        pbmService.setHandlerResolver(headerResolver);


        pbmPortType = pbmService.getPbmPort();

        Map<String, Object> pbmctxt = ((BindingProvider) pbmPortType).getRequestContext();

        pbmctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url.replace("/sdk", "/pbm"));
        pbmctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        pbmctxt.put(reqTimeOut, vcenterSessionTimeOut);
        pbmctxt.put(conTimeOut, vcenterSessionTimeOut);

        pbmPortType.pbmRetrieveServiceContent(pbmInstRef);
        isConnected = true;
    }

    /**
     * Disconnects the user session.
     *
     * @throws Exception Exception
     */
    public void disconnect() throws Exception {
        if (isConnected) {
            vimPort.logout(getServiceContent().getSessionManager());
        }
        isConnected = false;
    }

    /**
     * getService
     *
     * @return Service instance
     */
    public VimPortType getService() {
        return vimPort;
    }

    /**
     * getPbmService
     *
     * @return Service instance
     */
    public PbmPortType getPbmService() {
        return pbmPortType;
    }

    /**
     * getServiceContent
     *
     * @return Service instance content
     */
    public ServiceContent getServiceContent() {
        try {
            return vimPort.retrieveServiceContent(svcInstRef);
        } catch (RuntimeFaultFaultMsg e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * getPbmServiceContent
     *
     * @return pbm Service instance content
     */
    public PbmServiceInstanceContent getPbmServiceContent() {

        try {
            return pbmPortType.pbmRetrieveServiceContent(pbmInstRef);
        } catch (com.vmware.pbm.RuntimeFaultFaultMsg runtimeFaultFaultMsg) {
            logger.error(runtimeFaultFaultMsg.getMessage());
        }
        return null;
    }

    /**
     * getServiceCookie
     *
     * @return cookie used in service connection
     */
    public String getServiceCookie() {
        return serviceCookie;
    }

    /**
     * getPropCol
     *
     * @return Service property collector
     */
    public ManagedObjectReference getPropCol() {
        return getServiceContent().getPropertyCollector();
    }

    /**
     * getRootFolder
     *
     * @return Root folder
     */
    public ManagedObjectReference getRootFolder() {
        return getServiceContent().getRootFolder();
    }

    /**
     * Get the property value of managed object
     *
     * @param mor          managed object reference
     * @param propertyName property name.
     * @param <T>          T
     * @return T property value.
     * @throws Exception in case of error.
     */
    @SuppressWarnings("unchecked")
    public <T> T getDynamicProperty(ManagedObjectReference mor, String propertyName) throws Exception {
        List<String> props = new ArrayList<String>();
        props.add(propertyName);
        List<ObjectContent> objContent = retrieveMoRefProperties(mor, props);

        Object propertyValue = null;
        if (objContent != null && objContent.size() > 0) {
            List<DynamicProperty> dynamicProperty = objContent.get(0).getPropSet();
            if (dynamicProperty != null && dynamicProperty.size() > 0) {
                DynamicProperty dp = dynamicProperty.get(0);
                propertyValue = dp.getVal();
                /*
                 * If object is ArrayOfXXX object, then get the XXX[] by
                 * invoking getXXX() on the object.
                 * For Ex:
                 * ArrayOfManagedObjectReference.getManagedObjectReference()
                 * returns ManagedObjectReference[] array.
                 */
                Class dpCls = propertyValue.getClass();
                String dynamicPropertyName = dpCls.getName();
                String arrayOf = "ArrayOf";
                if (dynamicPropertyName.indexOf(arrayOf) != -1) {
                    String methodName = "get" + dynamicPropertyName
                        .substring(dynamicPropertyName.indexOf(arrayOf) + arrayOf.length(),
                            dynamicPropertyName.length());

                    Method getMorMethod = dpCls.getDeclaredMethod(methodName, null);
                    propertyValue = getMorMethod.invoke(propertyValue, (Object[]) null);
                }
            }
        }
        return (T) propertyValue;
    }

    /**
     * retrieveMoRefProperties
     *
     * @param mobj  mobj
     * @param props props
     * @return List
     * @throws Exception Exception
     */
    private List<ObjectContent> retrieveMoRefProperties(ManagedObjectReference mobj, List<String> props)
        throws Exception {
        PropertySpec pspec = new PropertySpec();
        pspec.setAll(false);
        pspec.setType(mobj.getType());
        pspec.getPathSet().addAll(props);

        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(mobj);
        ospec.setSkip(false);
        PropertyFilterSpec spec = new PropertyFilterSpec();
        spec.getPropSet().add(pspec);
        spec.getObjectSet().add(ospec);
        List<PropertyFilterSpec> specArr = new ArrayList<>();
        specArr.add(spec);

        return vimPort.retrieveProperties(getPropCol(), specArr);
    }

    /**
     * This method returns boolean value specifying whether the Task is
     * succeeded or failed.
     *
     * @param task ManagedObjectReference representing the Task.
     * @return boolean value representing the Task result.
     * @throws InvalidCollectorVersionFaultMsg InvalidCollectorVersionFaultMsg
     * @throws RuntimeFaultFaultMsg            RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg         InvalidPropertyFaultMsg
     * @throws Exception                       Exception
     */
    public boolean waitForTask(ManagedObjectReference task)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg, Exception {
        boolean retVal = false;

        try {
            // info has property - state for state of the task
            Object[] result = waitForValues(task, new String[] {"info.state", "info.error"}, new String[] {"state"},
                new Object[][] {new Object[] {
                    TaskInfoState.SUCCESS, TaskInfoState.ERROR}});
            // result for two properties: info.state, info.error
            if (result != null && result.length == 2) {
                if (result[0].equals(TaskInfoState.SUCCESS)) {
                    retVal = true;
                }
                if (result[1] instanceof LocalizedMethodFault) {
                    throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
                }
            }
        } catch (WebServiceException we) {
            logger.warn("Session to vCenter failed with: ", we.getLocalizedMessage());

            TaskInfo taskInfo = (TaskInfo) getDynamicProperty(task, "info");
            if (!taskInfo.isCancelable()) {
                logger.warn("vCenter task: "
                    + taskInfo.getName()
                    + "{"
                    + taskInfo.getKey()
                    + "}"
                    + " will continue to run on vCenter because the task cannot be cancelled");
                throw new RuntimeException(we.getLocalizedMessage());
            }

            logger.debug("Cancelling vCenter task: " + taskInfo.getName() + "{" + taskInfo.getKey() + "}");
            getService().cancelTask(task);

            // Since task cancellation is asynchronous, wait for the task to be cancelled
            Object[] result = waitForValues(task, new String[] {"info.state", "info.error"}, new String[] {"state"},
                new Object[][] {new Object[] {TaskInfoState.SUCCESS, TaskInfoState.ERROR}});
            // result for 2 properties: info.state, info.error
            if (result != null && result.length == 2) {
                if (result[0].equals(TaskInfoState.SUCCESS)) {
                    logger.warn(
                        "Failed to cancel vCenter task: "
                            + taskInfo.getName()
                            + "("
                            + taskInfo.getKey()
                            + ")"
                            + " and the task successfully completed");
                    retVal = true;
                }

                if (result[1] instanceof LocalizedMethodFault) {
                    MethodFault fault = ((LocalizedMethodFault) result[1]).getFault();
                    if (fault instanceof RequestCanceled) {
                        logger.debug("vCenter task "
                            + taskInfo.getName()
                            + "("
                            + taskInfo.getKey()
                            + ")"
                            + " was successfully cancelled");
                        throw new RuntimeException(we.getLocalizedMessage());
                    }
                } else {
                    throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
                }
            }
        }
        return retVal;
    }

    /**
     * Handle Updates for single object. waits till expected values of
     * properties to check are reached Destroys the ObjectFilter when done.
     *
     * @param objmor       MOR of the Object to wait for
     * @param filterProps  Properties list to filter
     * @param endWaitProps Properties list to check for expected values these
     *                     beproperties of property in the filter properties list
     * @param expectedVals values for properties to end the wait
     * @return true indicating expected values were met, and false otherwise
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    private synchronized Object[] waitForValues(ManagedObjectReference objmor, String[] filterProps,
                                                String[] endWaitProps, Object[][] expectedVals)
        throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
        // version string is initially null
        String version = "";
        Object[] endVals = new Object[endWaitProps.length];
        Object[] filterVals = new Object[filterProps.length];
        String stateVal = null;

        PropertyFilterSpec spec = new PropertyFilterSpec();
        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(objmor);
        ospec.setSkip(Boolean.FALSE);
        spec.getObjectSet().add(ospec);

        PropertySpec pspec = new PropertySpec();
        pspec.getPathSet().addAll(Arrays.asList(filterProps));
        pspec.setType(objmor.getType());
        spec.getPropSet().add(pspec);

        ManagedObjectReference propertyCollector = getPropCol();
        ManagedObjectReference filterSpecRef = vimPort.createFilter(propertyCollector, spec, true);

        boolean reached = false;

        UpdateSet updateset = null;
        List<PropertyFilterUpdate> filtupary = null;
        List<ObjectUpdate> objupary = null;
        List<PropertyChange> propchgary = null;
        while (!reached) {
            updateset = vimPort.waitForUpdatesEx(propertyCollector, version, new WaitOptions());
            if (updateset == null || updateset.getFilterSet() == null) {
                continue;
            }
            version = updateset.getVersion();
            // Make this code more general purpose when PropCol changes later.
            filtupary = updateset.getFilterSet();
            for (PropertyFilterUpdate filtup : filtupary) {
                objupary = filtup.getObjectSet();
                for (ObjectUpdate objup : objupary) {
                    if (objup.getKind() == ObjectUpdateKind.MODIFY || objup.getKind() == ObjectUpdateKind.ENTER
                        || objup.getKind() == ObjectUpdateKind.LEAVE) {
                        propchgary = objup.getChangeSet();
                        for (PropertyChange propchg : propchgary) {
                            updateValues(endWaitProps, endVals, propchg);
                            updateValues(filterProps, filterVals, propchg);
                        }
                    }
                }
            }
            Object expctdval = null;
            // Check if the expected values have been reached and exit the loop if done.
            // Also exit the WaitForUpdates loop if this is the case.
            for (int chgi = 0; chgi < endVals.length && !reached; chgi++) {
                for (int vali = 0; vali < expectedVals[chgi].length && !reached; vali++) {
                    expctdval = expectedVals[chgi][vali];
                    if (endVals[chgi] == null) {
                        // Do nothing
                        continue;
                    } else if (endVals[chgi].toString().contains("val: null")) {
                        // Handle JAX-WS De-serialization issue, by parsing nodes
                        Element stateElement = (Element) endVals[chgi];
                        if (stateElement != null && stateElement.getFirstChild() != null) {
                            stateVal = stateElement.getFirstChild().getTextContent();
                            reached = expctdval.toString().equalsIgnoreCase(stateVal) || reached;
                        }
                    } else {
                        reached = expctdval.equals(endVals[chgi]) || reached;
                        stateVal = "filtervals";
                    }
                }
            }
        }

        // Destroy the filter when we are done.
        vimPort.destroyPropertyFilter(filterSpecRef);

        Object[] retVal = filterVals;
        if (stateVal != null && "success".equalsIgnoreCase(stateVal)) {
            retVal = new Object[] {TaskInfoState.SUCCESS, null};
        }

        return retVal;
    }

    private void updateValues(String[] props, Object[] vals, PropertyChange propchg) {
        for (int findi = 0; findi < props.length; findi++) {
            if (propchg.getName().lastIndexOf(props[findi]) >= 0) {
                if (propchg.getOp() == PropertyChangeOp.REMOVE) {
                    vals[findi] = "";
                } else {
                    vals[findi] = propchg.getVal();
                }
            }
        }
    }

    private SelectionSpec getSelectionSpec(String name) {
        SelectionSpec genericSpec = new SelectionSpec();
        genericSpec.setName(name);
        return genericSpec;
    }

    private List<SelectionSpec> constructCompleteTraversalSpec() {
        // ResourcePools to VM: RP -> VM
        String vm = "vm";
        final String strVisitFolders = "VisitFolders";
        final String strDatacenter = "Datacenter";
        final String strResourcePool = "resourcePool";
        final String strRpToRp = "rpToRp";
        TraversalSpec rpToVm = new TraversalSpec();
        rpToVm.setName("rpToVm");
        rpToVm.setType("ResourcePool");
        rpToVm.setPath(vm);
        rpToVm.setSkip(Boolean.FALSE);

        // VirtualApp to VM: vApp -> VM
        TraversalSpec vappToVm = new TraversalSpec();
        vappToVm.setName("vAppToVM");
        vappToVm.setType("VirtualApp");
        vappToVm.setPath(vm);

        // Host to VM: HostSystem -> VM
        TraversalSpec htoVm = new TraversalSpec();
        htoVm.setType("HostSystem");
        htoVm.setPath(vm);
        htoVm.setName("hToVm");
        htoVm.getSelectSet().add(getSelectionSpec(strVisitFolders));
        htoVm.setSkip(Boolean.FALSE);

        // DataCenter to DataStore: DC -> DS
        TraversalSpec dcToDs = new TraversalSpec();
        dcToDs.setType(strDatacenter);
        dcToDs.setPath("datastore");
        dcToDs.setName("dcToDs");
        dcToDs.setSkip(Boolean.FALSE);

        // Recurse through all ResourcePools
        TraversalSpec rpToRp = new TraversalSpec();
        rpToRp.setType("ResourcePool");
        rpToRp.setPath(strResourcePool);
        rpToRp.setSkip(Boolean.FALSE);
        rpToRp.setName(strRpToRp);
        rpToRp.getSelectSet().add(getSelectionSpec(strRpToRp));

        TraversalSpec crToRp = new TraversalSpec();
        crToRp.setType("ComputeResource");
        crToRp.setPath(strResourcePool);
        crToRp.setSkip(Boolean.FALSE);
        crToRp.setName("crToRp");
        crToRp.getSelectSet().add(getSelectionSpec(strRpToRp));

        TraversalSpec crToH = new TraversalSpec();
        crToH.setSkip(Boolean.FALSE);
        crToH.setType("ComputeResource");
        crToH.setPath("host");
        crToH.setName("crToH");

        TraversalSpec dcToHf = new TraversalSpec();
        dcToHf.setSkip(Boolean.FALSE);
        dcToHf.setType(strDatacenter);
        dcToHf.setPath("hostFolder");
        dcToHf.setName("dcToHf");
        dcToHf.getSelectSet().add(getSelectionSpec(strVisitFolders));

        TraversalSpec vappToRp = new TraversalSpec();
        vappToRp.setName("vAppToRp");
        vappToRp.setType("VirtualApp");
        vappToRp.setPath(strResourcePool);
        vappToRp.getSelectSet().add(getSelectionSpec(strRpToRp));

        TraversalSpec dcToVmf = new TraversalSpec();
        dcToVmf.setType(strDatacenter);
        dcToVmf.setSkip(Boolean.FALSE);
        dcToVmf.setPath("vmFolder");
        dcToVmf.setName("dcToVmf");
        dcToVmf.getSelectSet().add(getSelectionSpec(strVisitFolders));

        // For Folder -> Folder recursion
        TraversalSpec visitFolders = new TraversalSpec();
        visitFolders.setType("Folder");
        visitFolders.setPath("childEntity");
        visitFolders.setSkip(Boolean.FALSE);
        visitFolders.setName(strVisitFolders);
        List<SelectionSpec> sspecarrvf = new ArrayList<SelectionSpec>();
        sspecarrvf.add(getSelectionSpec("crToRp"));
        sspecarrvf.add(getSelectionSpec("crToH"));
        sspecarrvf.add(getSelectionSpec("dcToVmf"));
        sspecarrvf.add(getSelectionSpec("dcToHf"));
        sspecarrvf.add(getSelectionSpec("vAppToRp"));
        sspecarrvf.add(getSelectionSpec("vAppToVM"));
        sspecarrvf.add(getSelectionSpec("dcToDs"));
        sspecarrvf.add(getSelectionSpec("hToVm"));
        sspecarrvf.add(getSelectionSpec("rpToVm"));
        sspecarrvf.add(getSelectionSpec(strVisitFolders));

        visitFolders.getSelectSet().addAll(sspecarrvf);

        List<SelectionSpec> resultspec = new ArrayList<>();
        resultspec.add(visitFolders);
        resultspec.add(crToRp);
        resultspec.add(crToH);
        resultspec.add(dcToVmf);
        resultspec.add(dcToHf);
        resultspec.add(vappToRp);
        resultspec.add(vappToVm);
        resultspec.add(dcToDs);
        resultspec.add(htoVm);
        resultspec.add(rpToVm);
        resultspec.add(rpToRp);

        return resultspec;
    }

    /**
     * Get the ManagedObjectReference for an item under the
     * specified root folder that has the type and name specified.
     *
     * @param root root folder if available, or null for default
     * @param type type of the managed object
     * @param name name to match
     * @return First ManagedObjectReference of the type / name pair found
     * @throws Exception Exception
     */
    public ManagedObjectReference getDecendentMoRef(ManagedObjectReference root, String type, String name)
        throws Exception {
        if (name == null || name.length() == 0) {
            return null;
        }

        try {
            // Create PropertySpecs
            PropertySpec pspec = new PropertySpec();
            pspec.setType(type);
            pspec.setAll(false);
            pspec.getPathSet().add("name");

            ObjectSpec ospec = new ObjectSpec();
            ospec.setObj(root);
            ospec.setSkip(false);
            ospec.getSelectSet().addAll(constructCompleteTraversalSpec());

            PropertyFilterSpec spec = new PropertyFilterSpec();
            spec.getPropSet().add(pspec);
            spec.getObjectSet().add(ospec);
            List<PropertyFilterSpec> specArr = new ArrayList<PropertyFilterSpec>();
            specArr.add(spec);

            ManagedObjectReference propCollector = getPropCol();
            List<ObjectContent> ocary = vimPort.retrieveProperties(propCollector, specArr);

            if (ocary == null || ocary.size() == 0) {
                return null;
            }

            // filter through retrieved objects to get the first match.
            for (ObjectContent oc : ocary) {
                ManagedObjectReference mor = oc.getObj();
                List<DynamicProperty> propary = oc.getPropSet();
                if (type == null || type.equals(mor.getType())) {
                    if (propary.size() > 0) {
                        String propval = (String) propary.get(0).getVal();
                        if (propval != null && name.equalsIgnoreCase(propval)) {
                            return mor;
                        }
                    }
                }
            }
        } catch (InvalidPropertyFaultMsg invalidPropertyException) {
            logger.debug(
                "Failed to get Vmware ManagedObjectReference for name: " + name
                    + " and type: " + type
                    + " due to " + invalidPropertyException.getMessage());
            throw invalidPropertyException;
        } catch (RuntimeFaultFaultMsg runtimeFaultException) {
            logger.debug(
                "Failed to get Vmware ManagedObjectReference for name: " + name
                    + " and type: " + type
                    + " due to " + runtimeFaultException.getMessage());
            throw runtimeFaultException;
        }

        return null;
    }

    /**
     * Get MORef from the property returned.
     *
     * @param objMor   Object to get reference property from
     * @param propName name of the property that is the MORef
     * @return the ManagedObjectReference for that property.
     * @throws Exception Exception
     */
    public ManagedObjectReference getMoRefProp(ManagedObjectReference objMor, String propName) throws Exception {
        Object props = getDynamicProperty(objMor, propName);
        ManagedObjectReference propmor = null;
        if (!props.getClass().isArray()) {
            propmor = (ManagedObjectReference) props;
        }
        return propmor;
    }

    /**
     * setVcenterSessionTimeout
     *
     * @param param vcenterSessionTimeOut
     */
    public void setVcenterSessionTimeout(int param) {
        this.vcenterSessionTimeOut = param;
    }

    public int getVcenterSessionTimeout() {
        return vcenterSessionTimeOut;
    }
}
