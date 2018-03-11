package com.cc.api.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.ec2.model.Instance;

@Component
public class LoadBalancer {
	//instance id, Currently how many requests are getting processed on instance
	private HashMap<String,Integer> instanceToLoadMap = new HashMap<>();
	private int threshold = 1;
	private int totalRunningInstances = 1;
	@Autowired
	private AwsInstanceService awsService;
	@Autowired
	private SqsServices sqsServices;
	
	
	public String nextInstance() {
		String initInstance = "";
		for(String instance: instanceToLoadMap.keySet() ) {
			if(instanceToLoadMap.get(instance)<threshold) {
				initInstance = instance;
				instanceToLoadMap.put(instance, instanceToLoadMap.get(instance)+1);
				return instance;
				
					
			}
		}
		if(totalRunningInstances < 20) {
			Instance i = awsService.createinstance();
			totalRunningInstances++;
			String instanceIP = i.getPublicIpAddress();
			instanceToLoadMap.put(instanceIP, 1);
			return instanceIP;
		}
		return 	initInstance;
		
	}
	
	public void update(String instance) {
		instanceToLoadMap.put(instance, instanceToLoadMap.get(instance)-1);
	}
}
