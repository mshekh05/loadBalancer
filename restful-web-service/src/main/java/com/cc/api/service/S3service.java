package com.cc.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3service {
	final static AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
	
	final static String bucket_name = "keyvaluebucket";
	
	public static void putObject(String url_key, String output_value) {
		//String key_name = url_key;
		s3.putObject(bucket_name, url_key, output_value);
		System.out.println("object placed");
	}
}
