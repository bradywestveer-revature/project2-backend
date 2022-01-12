package com.revature.project2backend.jsonmodels;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * CreatePostBody is used as the body for the method in PostController that creates a post
 */
@Data
public class CreatePostBody {
	/**
	 * The text body of the post
	 */
	private String body;
	
	/**
	 * The list of image data for the images of the post
	 */
	private List <Map <String, String>> images;
}