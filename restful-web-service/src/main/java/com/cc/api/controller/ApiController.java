package com.cc.api.controller;

import java.io.UnsupportedEncodingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cc.api.service.SqsServices;

@RestController
public class ApiController {

	@GetMapping("/url")
	public String getUrl(@RequestParam String url) throws UnsupportedEncodingException {
		// System.out.println(url);
		
		// String input = java.net.URLDecoder.decode(url, "UTF-8");
		// System.out.println(input);
		
		String output = "Instance is not free";
		try {
			SqsServices.sendMsg(url);
//			String instance = loadBalancer.nextInstance();
//			if (instance.isEmpty()) {
//				SqsServices.sendMsg(url);
//			} else {
//				output = processURL(instance,url);
//				//loadBalancer.update(instance);
//			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return output;
	}
	
//	public String processURL(String instance, String URL) {
//		String output = "Not Found";
//		output = rs.getImage(instance, URL);
//		if (output.length() <= 2) {
//			output = "Not Found";
//		}
//		String currentURL= "";
//		//String currentURL = SqsServices.receiveMsg();
//		if(!currentURL.isEmpty()) {
//			instance = loadBalancer.nextInstance();
//			processURL(instance,currentURL);
//		}
//		//publish to S3 bucket
//		S3service.putObject(URL, output);
//		return output;
//	}
//
//	@GetMapping("/create")
//	public String createInstance() {
//		return awsService.createinstance().getInstanceId();
//	}
}
