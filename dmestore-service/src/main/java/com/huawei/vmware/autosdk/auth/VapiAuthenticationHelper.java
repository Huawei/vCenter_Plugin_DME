package com.huawei.vmware.autosdk.auth;

import com.huawei.vmware.autosdk.SslUtil;

import com.vmware.cis.Session;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vapi.cis.authn.ProtocolFactory;
import com.vmware.vapi.cis.authn.SecurityContextFactory;
import com.vmware.vapi.core.ApiProvider;
import com.vmware.vapi.core.ExecutionContext.SecurityContext;
import com.vmware.vapi.protocol.HttpConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration.SslConfiguration;
import com.vmware.vapi.protocol.ProtocolConnection;
import com.vmware.vapi.security.SessionSecurityContext;

/**
 * Helper class which provides methods for
 * 1. login/logout using username, password authentication.
 * 2. getting authenticated stubs for client-side interfaces.
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class VapiAuthenticationHelper {
    /**
     * String
     */
    public static final String VAPI_PATH = "/api";

    private Session sessionSvc;

    private StubFactory stubFactory;

    /**
     * Creates session with the server using username and password
     *
     * @param server hostname or ip address of the server to log in to
     * @param username username for login
     * @param password password for login
     * @param httpConfig HTTP configuration settings to be applied for the connection to the server.
     * @param port port
     * @return the stub configuration configured with an authenticated session
     * @throws Exception if there is an existing session
     */
    public StubConfiguration loginByUsernameAndPassword(String server, String port, String username, String password,
        HttpConfiguration httpConfig) throws Exception {
        if (this.sessionSvc != null) {
            throw new Exception("Session already created");
        }

        this.stubFactory = createApiStubFactory(server, port, httpConfig);

        // Create security context for username/password authentication
        SecurityContext securityContext = SecurityContextFactory.createUserPassSecurityContext(username,
            password.toCharArray());

        // Create stub configuration with username/password security context
        StubConfiguration stubConfig = new StubConfiguration(securityContext);

        // Create session stub using the stub configuration.
        Session session = this.stubFactory.createStub(Session.class, stubConfig);

        // Login and create session
        char[] sessionId = session.create();

        // Initialize session security context from the generated session id
        SessionSecurityContext sessionSecurityContext = new SessionSecurityContext(sessionId);

        // Update the stub configuration to use the session id
        stubConfig.setSecurityContext(sessionSecurityContext);

        /*
         * Create stub for the session service using the authenticated
         * session
         */
        this.sessionSvc = this.stubFactory.createStub(Session.class, stubConfig);

        return stubConfig;
    }

    /**
     * Logs out of the current session.
     */
    public void logout() {
        if (this.sessionSvc != null) {
            this.sessionSvc.delete();
        }
    }

    /**
     * Connects to the server using https protocol and returns the factory
     * instance that can be used for creating the client side stubs.
     *
     * @param server hostname or ip address of the server
     * @param httpConfig HTTP configuration settings to be applied for the connection to the server.
     * @param port port
     * @return factory for the client side stubs
     * @throws Exception Exception
     */
    public StubFactory createApiStubFactory(String server, String port, HttpConfiguration httpConfig) throws Exception {
        // Create https connection with the vapi url
        ProtocolFactory pf = new ProtocolFactory();
        String apiUrl = "https://" + server + ":" + port + VAPI_PATH;

        // Get  connection to the vapi url
        ProtocolConnection connection = pf.getHttpConnection(apiUrl, null, httpConfig);

        // Initialize the stub factory with the api provider
        ApiProvider provider = connection.getApiProvider();
        return new StubFactory(provider);
    }

    /**
     * Returns the stub factory for the api endpoint
     *
     * @return StubFactory StubFactory
     */
    public StubFactory getStubFactory() {
        return this.stubFactory;
    }

    /**
     * Builds the http configuration.
     *
     * @param skipServerVerification skipServerVerification
     * @return the http configuration
     * @throws Exception the exception
     */
    public HttpConfiguration buildHttpConfiguration(boolean skipServerVerification) throws Exception {
        HttpConfiguration httpConfig = new HttpConfiguration.Builder().setSslConfiguration(
            buildSslConfiguration(skipServerVerification)).getConfig();
        return httpConfig;
    }

    /**
     * Builds the ssl configuration.
     *
     * @param skipServerVerification skipServerVerification
     * @return the ssl configuration
     * @throws Exception the exception
     */
    private SslConfiguration buildSslConfiguration(boolean skipServerVerification) throws Exception {
        SslConfiguration sslConfig;
        SslUtil.trustAllHttpsCertificates();
        sslConfig = new SslConfiguration.Builder().disableCertificateValidation()
            .disableHostnameVerification()
            .getConfig();
        return sslConfig;
    }
}
