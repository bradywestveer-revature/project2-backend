package com.revature.project2backend.utilities;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * S3Utilities contains members and methods that interface with AWS S3
 */
public class S3Utilities {
	/**
	 * The access key to use for AWS S3
	 */
	private static final String accessKey = System.getenv ("AWS_S3_ACCESS_KEY");
	
	/**
	 * The secret key to use for AWS S3
	 */
	private static final String secretKey = System.getenv ("AWS_S3_SECRET_KEY");
	
	/**
	 * The region to use for AWS S3
	 */
	private static final String region = "us-east-2";
	
	/**
	 * The bucket name to use for AWS S3
	 */
	private static final String bucketName = "jwa-s3";
	
	/**
	 * The url to use for AWS S3
	 */
	public static final String url = "https://jwa-s3.s3.us-east-2.amazonaws.com/";
	
	/**
	 * The S3 object is used to upload the files to S3
	 */
	private static final AmazonS3 s3 = AmazonS3ClientBuilder
		.standard ()
		.withRegion (region)
		.withCredentials (new AWSStaticCredentialsProvider (new BasicAWSCredentials (accessKey, secretKey)))
		.build ();
	
	/**
	 * The uploadImage method takes in a path to save the image under in the S3 bucket, and the base64 imageData to decrypt and upload
	 * @param path The path to save the image under
	 * @param imageData The base64 encoded data to upload to the S3 bucket
	 */
	public static void uploadImage (String path, String imageData) {
		s3.putObject (bucketName, path, new ByteArrayInputStream (Base64.getDecoder ().decode (imageData)), new ObjectMetadata ());
	}
}
