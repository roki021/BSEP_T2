package com.hospitalplatform.hospital_platform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class HospitalPlatformApplication {

	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;

	@Value("${server.ssl.key-store}")
	private String keyStore;

	@Value("${server.ssl.trust-store-password}")
	private String trustStorePassword;

	@Value("${server.ssl.trust-store}")
	private String trustStore;

	@PostConstruct
	public void initSsl(){
		System.setProperty("javax.net.ssl.keyStore", keyStore);
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
	}

	public static void main(String[] args) {
		SpringApplication.run(HospitalPlatformApplication.class, args);
	}

}
