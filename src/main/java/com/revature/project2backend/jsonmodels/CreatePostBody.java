package com.revature.project2backend.jsonmodels;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePostBody {
	private String body;
	private List <Map <String, String>> images;
}