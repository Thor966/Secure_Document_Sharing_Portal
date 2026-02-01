package com.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SecureDocumentSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureDocumentSharingApplication.class, args);
	}

}
