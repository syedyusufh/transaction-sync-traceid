package com.integration.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegrationManagement;

@EnableIntegrationManagement(defaultLoggingEnabled = "false", observationPatterns = "*")
@SpringBootApplication
public class TransactionSyncTraceIdIntegration {

	public static void main(String[] args) {
		SpringApplication.run(TransactionSyncTraceIdIntegration.class, args);
	}

}
