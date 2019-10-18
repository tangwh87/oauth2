package com.alibaba.cloud.examples;

import java.security.KeyPair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@SpringBootApplication
@EnableAuthorizationServer
@EnableDiscoveryClient
public class AnthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnthServerApplication.class, args);
	}

	  @Bean
	    public KeyPair keyPair() {
	        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("oauth2-jwt.jks"), "test123" .toCharArray());
	        return keyStoreKeyFactory.getKeyPair("oauth2-jwt");
	    }
}
