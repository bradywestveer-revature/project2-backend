package com.revature.project2backend.services;

import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.repositories.PostLikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The PostLikeService class is responsible for the methods that create and delete likes from a Post.
 */
@Service
public class PostLikeService {

	/**
	 * An Instance of the PostLikeRepository
	 */
	private final PostLikeRepo postLikeRepo;

	/**
	 * The constructor for PostLikeService that uses dependency injection through Spring.
	 *
	 * @param postLikeRepo An instance of the PostLikeRepository
	 */
	@Autowired
	public PostLikeService (PostLikeRepo postLikeRepo) {
		this.postLikeRepo = postLikeRepo;
	}

	/**
	 * Creates a like that is tied to a specific post and user.
	 *
	 * @param postLike A PostLike Object
	 */
	public void createPostLike (PostLike postLike) {
		this.postLikeRepo.save (postLike);
	}

	/**
	 * Creates a like that is tied to a specific post and user.
	 *
	 * @param postLike A PostLike Object
	 */
	public void deletePostLike (PostLike postLike) {
		this.postLikeRepo.delete (postLike);
	}
}
