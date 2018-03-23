package com.cc.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.Message;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.logging.LogManager;

public class receiver {

	public static void main(String[] args) throws InterruptedException {

		final String cmd1 = "source /home/ubuntu/tensorflow/bin/activate;";
		final String cmd2 = "cd /home/ubuntu/tensorflow/models/tutorials/image/imagenet;";
		final String cmd3 = "python classify_image.py --image_file ";
		final String cmd4 = " --num_top_predictions 1;";
		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();	
		final String bucket_name = "keyvaluebucket";
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
          
		// count = number of times an instance pings the request queue
		int count = 1;
		
		// output from image recognition script
		String imageRecognitionOutput = "";
		
		// URL picked from request Q
		String Url = "";
		
		// get image name from given Url
		String[] s3ProcessedUrl;
		
		String command_terminate[] = { "/bin/bash", "-c", "aws ec2 terminate-instances --instance-ids $(wget -q -O - http://instance-data/latest/meta-data/instance-id);" };

		while (true) {
			
			// Instance stops automatically when message is not available for pick up for more than 100 times
			if(count > 60) {
				try {
					System.out.println("Terminating Instance......... ");
					Process p = Runtime.getRuntime().exec(command_terminate);
					p.waitFor();
					p.destroy();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();

			for (Message m : messages) {
				
				long startTime = (new Date()).getTime();
				
				count = 0;
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
		        		System.out.println(e);
		        }

				System.out.println("Msgs worked on: "+ Url);
				imageRecognitionOutput = output;
				
				System.out.println("Output placed in bucket: " + imageRecognitionOutput);
				
				
				s3ProcessedUrl = Url.split("\\/");
				
				// put [imageID, output] into S3
				s3.putObject(bucket_name, s3ProcessedUrl[s3ProcessedUrl.length - 1], imageRecognitionOutput);
				
				System.out.println("Object placed in bucket");
				
				// delete message from request Q
				sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
				System.out.println("Message deleted from request Q");
				
				long endTime = (new Date()).getTime();	
				long difference = endTime - startTime;
				
				// time taken to complete 1 request
				System.out.println("Time taken to complete 1 request: " + difference);
				
				// publish message to response Q
				SendMessageRequest send_msg_request = new SendMessageRequest()
						.withQueueUrl(responseQueueUrl)
						.withMessageBody(s3ProcessedUrl[s3ProcessedUrl.length - 1] + "||" + imageRecognitionOutput);
				
				sqs.sendMessage(send_msg_request);
				System.out.println("Message sent to response Q");
	
			}
			count++;
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
