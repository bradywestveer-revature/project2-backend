package com.revature.project2backend.utilities;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class S3Utilities {
	private static final String accessKey = System.getenv ("AWS_S3_ACCESS_KEY");
	private static final String secretKey = System.getenv ("AWS_S3_SECRET_KEY");
	private static final String region = "us-east-2";
	private static final String bucketName = "jwa-s3";
	
	public static final String url = "https://jwa-s3.s3.us-east-2.amazonaws.com/";
	
	private static final AmazonS3 s3 = AmazonS3ClientBuilder
		.standard ()
		.withRegion (region)
		.withCredentials (new AWSStaticCredentialsProvider (new BasicAWSCredentials (accessKey, secretKey)))
		.build ();
	
	public static void uploadImage (String path, String imageData) {
		s3.putObject (bucketName, path, new ByteArrayInputStream (Base64.getDecoder ().decode (imageData)), new ObjectMetadata ());
	}
}
