package com.revature.project2backend.jsonmodels;

import lombok.Data;

/**
 * CreateCommentBody is used as the body for the method in CommentController that creates a comment
 */
@Data
public class CreateCommentBody {
	/**
	 * The ID of the post that the comment is replying to
	 */
	private Integer postId;
	
	/**
	 * The text body of the comment
	 */
	private String body;
}
