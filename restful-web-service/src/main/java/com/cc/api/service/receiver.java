package com.cc.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.Message;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.logging.LogManager;

public class receiver {
	
	// Instance terminates automatically once request Q is empty
	public static void terminateInstance() {
		
		final String command_getInstanceId[] = { "/bin/bash", "-c", "wget -q -O - http://instance-data/latest/meta-data/instance-id;" };
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		String Ins_output = "";
		String s = "";
		String output = "";
		
		try {
			System.out.println("Terminating Instance......... ");
			//test
			Process t = Runtime.getRuntime().exec(command_getInstanceId);
			t.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(t.getInputStream()));
			while ((s = br.readLine()) != null) {
				Ins_output = s;
        			System.out.println("InstanceID: "+ Ins_output);
			}
			t.destroy();
			
			TerminateInstancesRequest tir = new TerminateInstancesRequest().withInstanceIds(Ins_output);
			ec2.terminateInstances(tir);
			System.out.println("Terminate instances request has been run");
			
			DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
            	List<Reservation> reservations = describeInstancesResult.getReservations();                
            	List<Instance> listOfInstances = new ArrayList<>();
            
            System.out.println("Fetching instance ID from reservations");
            
            for (Reservation reservation : reservations)
                listOfInstances.addAll(reservation.getInstances());                
            for (Instance instance : listOfInstances) {
            		if(instance.getInstanceId().equals(output)) {
            			if (instance.getState().getName().equals("shutting-down")
	                            || instance.getState().getName().equals("terminated"))
	                        System.out.println("Termination successful.. YAYYY!! :) :)");
            			break;
            		}
            }
		} catch (Exception e) {
			System.out.println("**************************");
			System.out.println("Exception in terminating instance");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		final String cmd1 = "source /home/ubuntu/tensorflow/bin/activate;";
		final String cmd2 = "cd /home/ubuntu/tensorflow/models/tutorials/image/imagenet;";
		final String cmd3 = "python classify_image.py --image_file ";
		final String cmd4 = " --num_top_predictions 1;";
		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();	
		final String bucket_name = "mykeyvaluebucket";
		String s;
		String output = "";

		String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_sender1";
		String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_receiver2";
		
		// Log manager
		{
			LogManager.getLogManager().reset();
		    Set<String> loggers = new HashSet<>(Arrays.asList("org.apache.http", "groovyx.net.http","com.amazonaws"));
		       
		    for(String log:loggers) {
		       Logger logger = (Logger)LoggerFactory.getLogger(log);
		       logger.setLevel(Level.WARN);
		       logger.setAdditive(false);
		    }
		}
		
		// output from image recognition script
		String imageRecognitionOutput = "";
		
		// URL picked from request Q
		String Url = "";
		
		// get image name from given Url
		String[] s3ProcessedUrl;

		while (true) {
			
			//try was here previously
			
			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();
			if(messages.isEmpty()) {
				terminateInstance();
			}
				
			for (Message m : messages) {
				
				long startTime = (new Date()).getTime();
				
//				count = 0;
				Url = m.getBody().toString();
				
				System.out.println(Url);
				
				String command_img_recog[] = { "/bin/bash", "-c", cmd1 + cmd2 + cmd3 + Url + cmd4};
				
				// App Instance to run image recognition script and return output
				try {
					
				    System.out.println("Inside try block");
		            Process p = Runtime.getRuntime().exec(command_img_recog);
		            p.waitFor();
		            
		            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            System.out.println("-------------------------------------------------------------------------");
		            
		            while ((s = br.readLine()) != null) {
		            		output = s;
		            		System.out.println("Message: "+ output);
		            }
		            
		            p.destroy();
		        } catch (Exception e) {
		        		System.out.println("**************************");
					System.out.println("Exception in getting output from image recognition module");
		        		System.out.println(e);
		        		terminateInstance();
		        }

				System.out.println("Msgs worked on: "+ Url);

				System.out.println("Output retrieved: " + output);
				
				s3ProcessedUrl = Url.split("\\/");
				
				String[] imageRecognitionOutputArray = output.split("\\(");
				for(String str : imageRecognitionOutputArray)
					System.out.println("Output in array: " + str);
				imageRecognitionOutput = output.split("\\(")[0];
				System.out.println("imageRecognitionOutput: " + imageRecognitionOutput);
				
				try {
					// put [imageID, output] into S3
					s3.putObject(bucket_name, s3ProcessedUrl[s3ProcessedUrl.length - 1], imageRecognitionOutput);
					System.out.println("Object placed in bucket");
				} catch (Exception e) {
					System.out.println("**************************");
					System.out.println("Exception in S3 bucket");
					e.printStackTrace();
					terminateInstance();
				}
				
				try {
					// delete message from request Q
					sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
					System.out.println("Message deleted from request Q");
				} catch (Exception e){
					System.out.println("**************************");
					System.out.println("Exception in deleting from request Q");
					e.printStackTrace();
					terminateInstance();
				}
				
				
				long endTime = (new Date()).getTime();	
				long difference = endTime - startTime;
				
				// time taken to complete 1 request
				System.out.println("Time taken to complete 1 request: " + difference);
				
				try {
					// publish message to response Q
					SendMessageRequest send_msg_request = new SendMessageRequest()
							.withQueueUrl(responseQueueUrl)
							.withMessageBody(s3ProcessedUrl[s3ProcessedUrl.length - 1] + "||" + imageRecognitionOutput);
					
					sqs.sendMessage(send_msg_request);
					System.out.println("Message sent to response Q");
				} catch (Exception e) {
					System.out.println("**************************");
					System.out.println("Exception in sending to response Q");
					e.printStackTrace();
					terminateInstance();
				}	
			}
		}
	}
}
