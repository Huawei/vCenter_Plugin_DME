package com.huawei.vmware.autosdk;

import com.huawei.vmware.autosdk.auth.VapiAuthenticationHelper;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration;

/**
 * SessionHelper
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class SessionHelper {
    protected VapiAuthenticationHelper vapiAuthHelper;
    protected StubConfiguration sessionStubConfig;

    /**
     * login
     *
     * @param server   server
     * @param port     port
     * @param username username
     * @param password password
     * @throws Exception Exception
     */
    public void login(String server, String port, String username, String password) throws Exception {
        this.vapiAuthHelper = new VapiAuthenticationHelper();

        HttpConfiguration httpConfig = buildHttpConfiguration();
        this.sessionStubConfig = vapiAuthHelper.loginByUsernameAndPassword(
            server, port, username, password, httpConfig);
    }

    /**
     * buildHttpConfiguration
     *
     * @return HttpConfiguration HttpConfiguration
     * @throws Exception Exception
     */
    protected HttpConfiguration buildHttpConfiguration() throws Exception {
        HttpConfiguration httpConfig =
            new HttpConfiguration.Builder()
                .setSslConfiguration(buildSslConfiguration())
                .getConfig();

        return httpConfig;
    }

    /**
     * buildSslConfiguration
     *
     * @return HttpConfiguration.SslConfiguration sslConfig
     * @throws Exception Exception
     */
    protected HttpConfiguration.SslConfiguration buildSslConfiguration() throws Exception {
        HttpConfiguration.SslConfiguration sslConfig;

        /*
         * Below method enables all VIM API connections to the server
         * without validating the server certificates.
         *
         * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
         * Circumventing SSL trust is unsafe and should not be used in
         * production software.
         */
        SslUtil.trustAllHttpsCertificates();

        /*
         * Below code enables all vAPI connections to the server
         * without validating the server certificates..
         *
         * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
         * Circumventing SSL trust is unsafe and should not be used in
         * production software.
         */
        sslConfig = new HttpConfiguration.SslConfiguration.Builder()
            .disableCertificateValidation()
            .disableHostnameVerification()
            .getConfig();


        return sslConfig;
    }

    /**
     * logout
     */
    public void logout() {
        this.vapiAuthHelper.logout();
    }
}
