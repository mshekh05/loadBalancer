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

	private static String cmd1 = "source ~/tensorflow/bin/activate && cd /home/ubuntu/tensorflow/models/tutorials/image/imagenet && python classify_image.py --image_file ";
	private static String cmd2 = " --num_top_predictions 1";
	
	// App Instance to run image recognition script and return output
	public static String processURL(String url) {
		 String s;
		 String output = "";
		try {
            Process p = Runtime.getRuntime().exec(cmd1 + url + cmd2);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.println ("-------------------------------------------------------------------------");
            while ((s = br.readLine()) != null) {
            		output = s;
            		System.out.println("Message: "+output);
            }
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}
		
		return output;
	}

	public static void main(String[] args) throws InterruptedException {

		final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();	
		final String bucket_name = "keyvaluebucket";
		
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

		while (true) {
			
			// Instance stops automatically when message is not available for pick up for more than 100 times
			if(count > 100) {
				try {
					System.out.println("Shutting down.........");
					Process p = Runtime.getRuntime().exec("sudo shutdown");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();
			
			// get current time to be appended with s3 key - image ID
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			
			TimeUnit.SECONDS.sleep(5);
			
			for (Message m : messages) {
				
				count = 0;
				Url = m.getBody();
				
				// for each message received, process to get the output of the URL
				imageRecognitionOutput = processURL(Url.toString());

				System.out.println("Msgs worked on: "+ Url.toString());

				TimeUnit.SECONDS.sleep(20);
				
				s3ProcessedUrl = Url.split("\\/");
				
				// put [imageID_timestamp, output] into S3
				s3.putObject(bucket_name, s3ProcessedUrl[s3ProcessedUrl.length - 1] + "_" + timeStamp, imageRecognitionOutput);
				
				System.out.println("Object placed in bucket");
				
				// publish message to response Q
				SendMessageRequest send_msg_request = new SendMessageRequest()
						.withQueueUrl(responseQueueUrl)
						.withMessageBody(Url);
				
				sqs.sendMessage(send_msg_request);
				System.out.println("Message sent to response Q");
				
				// delete message from request Q
				sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
				System.out.println("Message deleted from request Q");
			}
			count++;
		}
	}
}
