package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class CreateCommentBody {
	Integer postId;
	String body;
}
