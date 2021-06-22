package com.microsvc.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,@PathVariable String to,@PathVariable  BigDecimal quantity) {
		 
		 String port=environment.getProperty("local.server.port");
		
		 CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(from, to);
		 
		 CurrencyConversion currencyConversionR=new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()),"Exchange Port:" +currencyConversion.getEnvironment()+" Conversion Port:"+port+" Feign");	 
		 
		return currencyConversionR;
		
	}
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,@PathVariable String to,@PathVariable  BigDecimal quantity) {
		
		HashMap<String, String> uriVariables=new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		String port=environment.getProperty("local.server.port");
		
		 ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,uriVariables);
		
		 CurrencyConversion currencyConversion=responseEntity.getBody();
		 
		 CurrencyConversion currencyConversionR=new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()), "Exchange Port:" +currencyConversion.getEnvironment()+" Conversion Port:"+port+" RestTemplate");	 
		 
		return currencyConversionR;
		
	}
	
}
