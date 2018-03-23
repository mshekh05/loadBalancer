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
//	private static int currentRunningInstances = 1;
	private static String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_sender1";
	private int requestQueueLength = 0;
	
	
    public LoadBalancer(){
       
//        AwsInstanceService.startinstance(instanceID);
    }
    public void run(){
    	System.out.println("#######################   Load Balancer Started #######################");

        while(true) {
            try {
        
            	GetQueueAttributesRequest request = new GetQueueAttributesRequest(requestQueueUrl).withAttributeNames("ApproximateNumberOfMessages");
            	Map<String,String> attributes = sqs.getQueueAttributes(request).getAttributes();
            	requestQueueLength = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));
            	System.out.println("#######################  Number of msg Aprox #######################" );
            	System.out.println(requestQueueLength);

            	
            	Queue<String> runningInstances = new LinkedList<>();
            	//Queue<String> stoppedInstances = new LinkedList<>();
            	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
            	DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
            	List<Reservation> reservations = describeInstancesResult.getReservations();

            	
            	List<Instance> listOfInstances = new ArrayList<>();
            	for(Reservation reservation : reservations)
            		listOfInstances.addAll(reservation.getInstances());
            	
            	
            	for(Instance instance: listOfInstances)
            	{
            		if(instance.getInstanceId().equals("i-046121b4e494562ed")) {
            			System.out.println("#############################Cont###################################");
            			continue;
            			
            		}
            			
            		if(instance.getState().getName().equals("running")||instance.getState().getName().equals("pending") )
            			runningInstances.add(instance.getInstanceId());
            			
            		
            	}
            	
            	System.out.println("############################# Run inst"+Integer.toString(runningInstances.size())+"#############################################");   	
                
                if(runningInstances.size()<requestQueueLength) {
                    for(int i=runningInstances.size();i<Math.min(requestQueueLength, 5);i++) {
                        //String instanceID = stoppedInstances.poll();
       
                        	System.out.println("###############################Creating Instance ##################################");
                            Instance instance = AwsInstanceService.createinstance();
                            Thread.sleep(10*1000);
                            String instanceID = instance.getInstanceId();
                        	runningInstances.add(instanceID);
                       
                        
                    }
                    
                }/*
                if(currentRunningInstances>requestQueueLength) {
                    
                    String instanceID = prop.getHost(currentRunningInstances);
                    AwsInstanceService.stopinstance(instanceID);
                    currentRunningInstances--; 
                }  */
            } catch (InterruptedException e) {
            	System.out.println("################################ Error in load balancer ");
            	System.out.println(e);
               e.printStackTrace();
            }
        }
        
    }

}
