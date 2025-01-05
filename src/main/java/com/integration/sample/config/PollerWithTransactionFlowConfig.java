package com.integration.sample.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.transaction.TransactionSynchronizationFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Configuration
public class PollerWithTransactionFlowConfig {

	private PlatformTransactionManager pseudoTransactionManager;

	private TransactionSynchronizationFactory transactionLoggingSynchronizationFactory;

	private MessageChannel appErrorChannel;

	@Bean
	IntegrationFlow pollerWithTransactionFlow() {

		// @formatter:off
		return IntegrationFlow.fromSupplier(() -> "Good Day", p -> p.poller(pollerSpec()))
							.handle(m -> log.info("Supplied Message is: {}", m.getPayload()))
							.get();
		// @formatter:on
	}

	@Bean
	PollerSpec pollerSpec() {

		// @formatter:off
		return Pollers.fixedDelay(Duration.ofSeconds(30))
					.transactional(pseudoTransactionManager)
					.transactionSynchronizationFactory(transactionLoggingSynchronizationFactory)
					.errorChannel(appErrorChannel);
		// @formatter:on
	}

}
