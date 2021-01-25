/*
 * *******************************************************
 * Copyright VMware, Inc. 2013, 2016.  All Rights Reserved.
 * SPDX-License-Identifier: MIT
 * *******************************************************
 *
 * DISCLAIMER. THIS PROGRAM IS PROVIDED TO YOU "AS IS" WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, WHETHER ORAL OR WRITTEN,
 * EXPRESS OR IMPLIED. THE AUTHOR SPECIFICALLY DISCLAIMS ANY IMPLIED
 * WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.huawei.vmware.autosdk;

import com.huawei.vmware.util.VmwareClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * Retrieves the SSL certificate chain of an HTTPS server and stores the root
 * certificate into an in-memory trust store.
 * <p>
 * Note: Circumventing SSL trust is unsafe and should not use these in
 * production software.
 * </p>
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class SslUtil {
    /**
     * SslUtil
     */
    private SslUtil() { }

    /**
     * Method to trust all the HTTPS certificates. To be used only in the
     * development environment for convenience.
     */
    public static void trustAllHttpsCertificates() {
        try {
            // Create the trust manager.
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager tm = new VmwareClient.TrustAllTrustManager();
            trustAllCerts[0] = tm;

            // Create the SSL context
            SSLContext sc = SSLContext.getInstance("TLS");

            // Create the session context
            javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
            /*
             * Initialize the contexts; the session context takes the trust
             * manager.
             */
            sslsc.setSessionTimeout(0);
            sc.init(null, trustAllCerts, null);

            /*
             * Use the default socket factory to create the socket for the
             * secure connection
             */
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            /*
             * Declare a host name verifier that will automatically enable the
             * connection. The host name verifier is invoked during the SSL
             * handshake.
             */
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };

            // Set the default host name verifier to enable the connection.
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
