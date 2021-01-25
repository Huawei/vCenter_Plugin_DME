package com.huawei.vmware.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * SecureSSLSocketFactory
 *
 * @author Administrator
 * @since 2020-12-09
 */
public class SecurtySslSocketFactory extends SSLSocketFactory {
    /**
     * logger
     */
    public static final Logger logger = LoggerFactory.getLogger(SecurtySslSocketFactory.class);

    private SSLContext sslContext;

    /**
     * SecureSSLSocketFactory
     *
     * @param sslContext sslContext
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public SecurtySslSocketFactory(SSLContext sslContext) throws NoSuchAlgorithmException {
        if (sslContext != null) {
            this.sslContext = sslContext;
        } else {
            this.sslContext = SslUtil.getSslContext();
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return getSupportedCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        String[] ciphers = null;
        try {
            ciphers = SslUtil.getSupportedCiphers();
        } catch (NoSuchAlgorithmException e) {
            logger.error("SecureSSLSocketFactory::getDefaultCipherSuites found no cipher suites");
        }
        return ciphers;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean isAutoClose) throws IOException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(s, host, port, isAutoClose);
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(
                SslUtil.getSupportedProtocols(((SSLSocket) socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(host, port);
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(
                SslUtil.getSupportedProtocols(((SSLSocket) socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress inetAddress, int localPort)
        throws IOException, UnknownHostException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(host, port, inetAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(
                SslUtil.getSupportedProtocols(((SSLSocket) socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int localPort) throws IOException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(inetAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(
                SslUtil.getSupportedProtocols(((SSLSocket) socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
        throws IOException {
        SSLSocketFactory factory = this.sslContext.getSocketFactory();
        Socket socket = factory.createSocket(address, port, localAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(
                SslUtil.getSupportedProtocols(((SSLSocket) socket).getEnabledProtocols()));
        }
        return socket;
    }
}