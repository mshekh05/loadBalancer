package com.cc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cc.api.service.LoadBalancer;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class RestfulWebServiceApplication {
	static LoadBalancer loadBalancer = new LoadBalancer();
	public static void main(String[] args) {
		Thread t = new Thread(loadBalancer);
		t.start();
		SpringApplication.run(RestfulWebServiceApplication.class, args);
		
	}
}
