package com.huawei.dmestore.utils;

import com.huawei.dmestore.exception.DmeException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

/**
 * RestUtils
 *
 * @author yy
 * @since 2020-09-02
 **/
public class RestUtils {
    public static final int RES_STATE_I_200 = 200;

    public static final int RES_STATE_I_202 = 202;

    public static final int RES_STATE_I_401 = 401;

    public static final int RES_STATE_I_403 = 403;

    public RestTemplate getRestTemplate() throws DmeException {
        RestTemplate restTemplate;
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setReadTimeout(10000);
            requestFactory.setConnectTimeout(3000);
            restTemplate = new RestTemplate(requestFactory);
            restTemplate.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) {
                    return true;
                }

                @Override
                public void handleError(ClientHttpResponse response) {
                }
            });
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new DmeException("初始化resttemplate错误");
        }
        return restTemplate;
    }
}
