package com.hospitalplatform.hospital_platform.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class RestSSLConfig {

    @Value("${hospital.keystore.filepath}")
    private String keyStorePath;

    @Value("${hospital.truststore.filepath}")
    private String trustStorePath;

    @Value("${hospital.keystore.password}")
    private String keyStorePassword;

    @Value("${hospital.keystore.password}")
    private String keyPassword;

    @Bean(name = "twoWaySSLConfig")
    public RestTemplate restTemplateMutual() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        File initialFile = new File(keyStorePath);
        InputStream keyStoreData = new FileInputStream(initialFile);

        File initialFile1 = new File(trustStorePath);
        InputStream trustStoreData = new FileInputStream(initialFile1);

        keyStore.load(new BufferedInputStream(keyStoreData), keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance("JKS", "SUN");
        trustStore.load(new BufferedInputStream(trustStoreData), keyStorePassword.toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()
                        .loadTrustMaterial(trustStore, null)
                        .loadKeyMaterial(keyStore, keyPassword.toCharArray()).build()
                , NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setMaxConnTotal(1)
                .setMaxConnPerRoute(5)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(10000);
        requestFactory.setConnectionRequestTimeout(10000);

        return new RestTemplate(requestFactory);
    }

    @Bean(name = "ignoreSSLConfig")
    public RestTemplate restTemplateNoop() throws Exception {
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustAllStrategy()).build());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setMaxConnTotal(1)
                .setMaxConnPerRoute(5)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(10000);
        requestFactory.setConnectionRequestTimeout(10000);

        return new RestTemplate(requestFactory);
    }
}
