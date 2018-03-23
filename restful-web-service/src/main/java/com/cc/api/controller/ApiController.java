package com.cc.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.cc.api.service.SqsServices;

@RestController
public class ApiController {

	final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    final String responseQueueUrl = "https://sqs.us-west-1.amazonaws.com/087303647010/cc_proj_receiver2";
	@GetMapping("/url")
	public String getUrl(@RequestParam String url) throws UnsupportedEncodingException {
		// System.out.println(url);
		
		// String input = java.net.URLDecoder.decode(url, "UTF-8");
		// System.out.println(input);
		
		String output = "Timed Out";
		try {
            SqsServices.sendMsg(url);
            String[] url_split = url.split("\\/");
            String image = url_split[url_split.length - 1];
            
            long startTime = System.currentTimeMillis(); //fetch starting time
            while((System.currentTimeMillis()-startTime)<15000)
            {
                List<Message> messages = sqs.receiveMessage(responseQueueUrl).getMessages();

                for (Message m : messages) {
                    String response = m.getBody().toString();
                    if (response.split("\\|\\|")[0].equals(image)) {
                        sqs.deleteMessage(responseQueueUrl, m.getReceiptHandle());

                        return response.split("\\|\\|")[1];
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            System.out.println(e);
            return output;
        }
		return output;
	}
	

}
