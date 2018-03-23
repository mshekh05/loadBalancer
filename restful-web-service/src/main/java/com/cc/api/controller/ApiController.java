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


		} catch (Exception e) {
			System.out.println(e);
		}
		return output;
	}
	

}
