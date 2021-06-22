package com.microsvc.currencyexchangeservice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {

	private Logger logger=LoggerFactory.getLogger(CircuitBreakerController.class);
	
	
	@GetMapping("/sample-api")
	//@CircuitBreaker(name = "sample-api", fallbackMethod="hardcodedResponse")
	//@Retry(name = "sample-api", fallbackMethod="hardcodedResponse")
	@RateLimiter(name="sample-api")
	public String sampleApiCircuitBreaker() {
		logger.info("Sample Api Call Received"+new Date());
		//new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class); 
		return "Sample API "+new Date();
	}
	
	public String hardcodedResponse(Exception exception) {
		return "fallback-response "+new Date();
	}
	
	
}
