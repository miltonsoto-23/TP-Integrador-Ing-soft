package com.iot.chain;

import com.iot.repositories.SystemConfigRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for instantiating and assembling the Chain of Responsibility for
 * PIN validation. The resulting bean is managed by the Spring IoC container and injected into the
 * AccessService layer during application startup.
 */
@Configuration
public class ChainConfig {
  @Bean
  public Handler validationChain(SystemConfigRepository configRepository) {
    // independent instantiation
    PinFormatHandler formatHandler = new PinFormatHandler();
    PinVerificationHandler verificationHandler = new PinVerificationHandler(configRepository);

    // Join in correct order
    formatHandler.setNext(verificationHandler);

    // return the first link
    return formatHandler;
  }
}
