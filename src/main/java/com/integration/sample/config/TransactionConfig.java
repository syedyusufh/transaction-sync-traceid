package com.integration.sample.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.transaction.DefaultTransactionSynchronizationFactory;
import org.springframework.integration.transaction.ExpressionEvaluatingTransactionSynchronizationProcessor;
import org.springframework.integration.transaction.PseudoTransactionManager;
import org.springframework.integration.transaction.TransactionSynchronizationFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Configuration
public class TransactionConfig {

	private ApplicationContext applicationContext;

	@Bean
	PlatformTransactionManager pseudoTransactionManager() {
		return new PseudoTransactionManager();
	}

	@Bean
	TransactionSynchronizationFactory transactionLoggingSynchronizationFactory() {

		var parser = new SpelExpressionParser();

		var processor = new ExpressionEvaluatingTransactionSynchronizationProcessor();
		processor.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
		processor.setAfterCommitExpression(parser.parseExpression("payload"));
		processor.setAfterCommitChannel(transactionLoggingChannel());

		return new DefaultTransactionSynchronizationFactory(processor);
	}

	@Bean
	IntegrationFlow transactionLoggingFlow() {

		// @formatter:off
		return IntegrationFlow.from(transactionLoggingChannel())
							.handle(m -> log.info("Transaction Committed, but traceId is different here !!"))
							.get();
		// @formatter:on
	}

	@Bean
	MessageChannel transactionLoggingChannel() {
		return new DirectChannel();
	}

}
