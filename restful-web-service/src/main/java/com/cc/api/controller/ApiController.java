package com.cc.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cc.api.service.AwsInstanceService;
import com.cc.api.service.LoadBalancer;
import com.cc.api.service.RequestService;
import com.cc.api.service.SqsServices;

@RestController
public class ApiController {
	@Autowired
	private AwsInstanceService awsService;
	@Autowired
	private RequestService rs;
	@Autowired
	private LoadBalancer loadBalancer;

	@GetMapping("/url")
	public String getUrl(@RequestParam String url) throws UnsupportedEncodingException {
		// System.out.println(url);
		
		// String input = java.net.URLDecoder.decode(url, "UTF-8");
		// System.out.println(input);
		String output = "Instance is not free";
		try {
			
			String instance = loadBalancer.nextInstance();
			if (instance.isEmpty()) {
				SqsServices.sendMsg(url);
			} else {
				output = processURL(instance,url);
				//loadBalancer.update(instance);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return output;
	}
	
	public String processURL(String instance, String URL) {
		String output = "Not Found";
		output = rs.getImage(instance, URL);
		if (output.length() <= 2) {
			output = "Not Found";
		}
		String currentURL= "";
		//String currentURL = SqsServices.receiveMsg();
		if(!currentURL.isEmpty()) {
			instance = loadBalancer.nextInstance();
			processURL(instance,currentURL);
		}		
		return output;
	}

	@GetMapping("/create")
	public String createInstance() {
		return awsService.createinstance().getInstanceId();
	}

}
