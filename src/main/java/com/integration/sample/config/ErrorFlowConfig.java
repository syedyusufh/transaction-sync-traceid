package com.integration.sample.config;

import static org.springframework.integration.handler.LoggingHandler.Level.ERROR;
import static org.springframework.integration.handler.LoggingHandler.Level.INFO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

@Configuration
public class ErrorFlowConfig {

	@Bean
	MessageChannel appErrorChannel() {
		return new PublishSubscribeChannel();
	}

	// @Bean
	IntegrationFlow appErrorChannelFlow() {

		// @formatter:off
		return IntegrationFlow.from(appErrorChannel())
							.<MessagingException> log(ERROR, m -> "Exception: " + m.getPayload().getCause().getMessage())
							.log(INFO, m -> "This IntegrationFlow's job is ONLY to log the exceptions, which is done above")
							.get();
		// @formatter:on
	}

}
