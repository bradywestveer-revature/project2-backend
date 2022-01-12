package com.revature.project2backend.services;

import com.revature.project2backend.models.PostImage;
import com.revature.project2backend.repositories.PostImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostImageService {

	/**
	 * An instance of the PostImageRepository.
	 */
	private final PostImageRepo postImageRepo;

	/**
	 * The constructor for the PostImageService that uses dependency injection through Spring.
	 *
	 * @param postImageRepo An instance of the PostImageRepository.
	 */
	@Autowired
	public PostImageService (PostImageRepo postImageRepo) {
		this.postImageRepo = postImageRepo;
	}

	/**
	 * Saves an image and associates it to a specific user and post.
	 *
	 * @param postImage A PostImage Object
	 */
	public void createPostImage (PostImage postImage) {
		postImageRepo.save (postImage);
	}
}
