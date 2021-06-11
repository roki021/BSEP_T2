package com.hospitalplatform.hospital_platform.config;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import com.hospitalplatform.hospital_platform.service.OCSPService;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

@Configuration
@EnableScheduling
public class RestSSLConfig {

    @Value("${hospital.keystore.filepath}")
    private String keyStorePath;

    @Value("${hospital.truststore.filepath}")
    private String trustStorePath;

    @Value("${hospital.truststore.password}")
    private String trustStorePassword;

    @Value("${hospital.keystore.password}")
    private String keyStorePassword;

    @Value("${hospital.keystore.password}")
    private String keyPassword;

    @Value("${admin_platform.ocsp_url}")
    private String ocspUrl;

    @Value("${hospital.ocsp_resp}")
    private String ocspPath;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private CertificateService certificateService;

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

    @Scheduled(cron = "*/31 * * * * *")
    public void scheduleTaskUsingCronExpression() throws Exception {
        RestTemplate restTemplate = new RestTemplate(); // no https because ocsp request is on http
        X509Certificate cert = certificateService.readCertificate(
                keyStorePath, keyStorePassword, "hospital"
        );
        X509Certificate issuerCert = certificateService.readCertificate(
                trustStorePath, trustStorePassword, "ca-cert"
        );
        byte[] reqEncoded = ocspService.generateOCSPRequestEncoded(cert, issuerCert);
        ResponseEntity<byte[]> resp = restTemplate.postForEntity(ocspUrl, reqEncoded, byte[].class);

        FileOutputStream output = new FileOutputStream(ocspPath);
        IOUtils.write(resp.getBody(), output);
    }
}
