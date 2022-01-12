package com.revature.project2backend.jsonmodels;

import lombok.Data;

/**
 * CreateLikeBody is used as the body for the method in PostLikeController that creates a like
 */
@Data
public class CreateLikeBody {
	/**
	 * The ID of the post to like
	 */
	private Integer postId;
}
