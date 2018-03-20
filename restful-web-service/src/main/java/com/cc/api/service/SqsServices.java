package com.cc.api.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Component
public class SqsServices {
	private static final String QUEUE_NAME = "testQueue";
	final static AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	public static void createQueue() {
		CreateQueueResult create_result = sqs.createQueue(QUEUE_NAME);
		System.out.println("Created a queue");
		
	}

	public static void sendMsg(String urlRequest) {
		String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
		SendMessageRequest send_msg_request = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(urlRequest).withDelaySeconds(5);
		sqs.sendMessage(send_msg_request);
		System.out.println("send a msg");
	}
	
	public static String receiveMsg() {
		String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();
		List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
		if(!messages.isEmpty()) {
			for( Message m: messages) {
				m.getBody();
				sqs.deleteMessage(queueUrl,m.getReceiptHandle());
				System.out.print("delete one msg");
				return m.getBody();
			}
		}
		
		return "";
		
	}
}
