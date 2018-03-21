package com.cc.api.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.springframework.stereotype.Component;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;

@Component
public class LoadBalancer implements Runnable{
	
	//instance id, Currently how many requests are getting processed on instance
//	private HashMap<String,Integer> instanceToLoadMap = new HashMap<>();
//	private int threshold = 1;
//	private int count = 1;
//	private int totalRunningInstances = 1;
//	@Autowired
//	private AwsInstanceService awsService;
//	@Autowired
//	private SqsServices sqsServices;
//	
//	
//	public String nextInstance() {
//		String initInstance = "";
//		count = 1;
//		PropertiesService prop = new PropertiesService();
//		for(String instance: instanceToLoadMap.keySet() ) {
//			if(instanceToLoadMap.get(instance)<threshold) {
//				initInstance = instance;
//				count++;
//				instanceToLoadMap.put(instance, instanceToLoadMap.get(instance)+1);
//				return instance;
//				
//					
//			}
//		}
//		if(totalRunningInstances < 20) {
//			String instanceID = prop.getHost(count);
//			count++;
//			String instanceIP = getPublicIp(instanceID);
//			totalRunningInstances++;
//			instanceToLoadMap.put(instanceIP, 1);
//			return instanceIP;
//		}
//		return 	initInstance;
//		
//	}
	final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	private static int currentRunningInstances = 1;
	private static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_sender1";
	private int requestQueueLength = 0;
	
	
    PropertiesService prop;
    public LoadBalancer(){
        prop = new PropertiesService();
        String instanceID = "i-017b7f87292dc3202";
        AwsInstanceService.startinstance(instanceID);
    }
    public void run(){

        while(true) {
            try {
        
            	GetQueueAttributesRequest request = new GetQueueAttributesRequest(requestQueueUrl);
            	Map<String,String> attributes = sqs.getQueueAttributes(request).getAttributes();
            	requestQueueLength = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));
            	
            	Queue<String> runningInstances = new LinkedList<>();
            	Queue<String> stoppedInstances = new LinkedList<>();
            	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
            	DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
            	List<Reservation> reservations = describeInstancesResult.getReservations();

            	
            	List<Instance> listOfInstances = new ArrayList<>();
            	for(Reservation reservation : reservations)
            		listOfInstances.addAll(reservation.getInstances());
            	
            	
            	for(Instance instance: listOfInstances)
            	{
            		if(instance.getState().getName().equals("running"))
            			runningInstances.add(instance.getInstanceId());
            		else if(instance.getState().getName().equals("stopped"))
            			stoppedInstances.add(instance.getInstanceId());
            			
            			
            		
            	}
            	
                Thread.sleep(10*1000);
                if(runningInstances.size()<requestQueueLength) {
                    for(int i=runningInstances.size();i<Math.min(requestQueueLength, 5);i++) {
                        String instanceID = stoppedInstances.poll();
                        AwsInstanceService.startinstance(instanceID);
                        if(instanceID != null)
                        	runningInstances.add(instanceID);
                        
                    }
                    
                }/*
                if(currentRunningInstances>requestQueueLength) {
                    
                    String instanceID = prop.getHost(currentRunningInstances);
                    AwsInstanceService.stopinstance(instanceID);
                    currentRunningInstances--; 
                }  */
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }
        
    }

}
