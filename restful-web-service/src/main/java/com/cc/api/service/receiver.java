package com.cc.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.Message;



public class receiver {

	private static String cmd1 = "source ~/tensorflow/bin/activate && cd /home/ubuntu/tensorflow/models/tutorials/image/imagenet && python classify_image.py --image_file ";
	private static String cmd2 = " --num_top_predictions 1";
	
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
		
		int count = 1;
		String output = "";
		String Url = "";
		String s3Url = "";

		while (true) {
			String requestQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_sender1";
			String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_receiver2";
			
			List<Message> messages = sqs.receiveMessage(requestQueueUrl).getMessages();

			TimeUnit.SECONDS.sleep(5);
			
			for (Message m : messages) {
				Url = m.getBody();
				output = processURL(Url.toString());

				System.out.println("Msgs worked on:"+ Url.toString());

				TimeUnit.SECONDS.sleep(20);
				
				s3Url = Url.toString().replace("/", "|");
				S3service.putObject(s3Url, output);
				
				System.out.println("Object placed in bucket");
				
				//publish message to response Q
				SendMessageRequest send_msg_request = new SendMessageRequest()
						.withQueueUrl(responseQueueUrl)
						.withMessageBody(Url);
						//.withDelaySeconds(5); //how many secs to delay msg from being sent to the Q
				
				sqs.sendMessage(send_msg_request);
				System.out.println("Message sent to response Q");
				
				sqs.deleteMessage(requestQueueUrl, m.getReceiptHandle());
				System.out.println("Message deleted from request Q");
			}
			count++;
		}
	}
}
