package com.revature.project2backend.jsonmodels;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePostBody {
	private String body;
	//todo replace with List of ImageDataBody?
	private List <Map <String, String>> images;
}