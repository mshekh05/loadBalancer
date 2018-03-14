package com.cc.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

@Component
public class LoadBalancer {
	//instance id, Currently how many requests are getting processed on instance
	private HashMap<String,Integer> instanceToLoadMap = new HashMap<>();
	private int threshold = 1;
	private int count = 1;
	private int totalRunningInstances = 1;
	@Autowired
	private AwsInstanceService awsService;
	@Autowired
	private SqsServices sqsServices;
	
	
	public String nextInstance() {
		String initInstance = "";
		count = 1;
		PropertiesService prop = new PropertiesService();
		for(String instance: instanceToLoadMap.keySet() ) {
			if(instanceToLoadMap.get(instance)<threshold) {
				initInstance = instance;
				count++;
				instanceToLoadMap.put(instance, instanceToLoadMap.get(instance)+1);
				return instance;
				
					
			}
		}
		if(totalRunningInstances < 20) {
			String instanceID = prop.getHost(count);
			count++;
			String instanceIP = getPublicIp(instanceID);
			totalRunningInstances++;
			instanceToLoadMap.put(instanceIP, 1);
			return instanceIP;
		}
		return 	initInstance;
		
	}
	
	public String getPublicIp(String instanceID) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();	
		
		DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
		List<Reservation> reservations = describeInstancesResult.getReservations();
		ArrayList<Instance> listOfInstances = new ArrayList<Instance>();
		for(Reservation reservation : reservations)
			listOfInstances.addAll(reservation.getInstances());
		
		String ownIP = null;
		
		for(Instance instance: listOfInstances)
		{
			if(instance.getInstanceId().equals(instanceID)) {
				ownIP = instance.getPublicIpAddress();
				break;
			}
				
			System.out.println(ownIP);
		}
        return ownIP;
    }
	
	public void update(String instance) {
		instanceToLoadMap.put(instance, instanceToLoadMap.get(instance)-1);
	}
}
