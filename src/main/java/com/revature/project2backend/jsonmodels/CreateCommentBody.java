package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class CreateCommentBody {
	private Integer postId;
	private String body;
}
