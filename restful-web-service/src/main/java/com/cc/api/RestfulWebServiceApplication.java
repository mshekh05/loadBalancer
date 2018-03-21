package com.cc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cc.api.service.LoadBalancer;

@SpringBootApplication
public class RestfulWebServiceApplication {
	static LoadBalancer loadBalancer = new LoadBalancer();
	public static void main(String[] args) {
		Thread t = new Thread(loadBalancer);
		t.start();
		SpringApplication.run(RestfulWebServiceApplication.class, args);
		
	}
}
