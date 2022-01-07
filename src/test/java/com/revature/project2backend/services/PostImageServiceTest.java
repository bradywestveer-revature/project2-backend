package com.revature.project2backend.services;

import com.revature.project2backend.models.PostImage;
import com.revature.project2backend.repositories.PostImageRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostImageServiceTest {
	private final PostImageService postImageService;
	
	private final PostImageRepo postImageRepo = Mockito.mock (PostImageRepo.class);
	
	public PostImageServiceTest () {
		this.postImageService = new PostImageService (this.postImageRepo);
	}
	
	@Test
	void createPostImage () {
		PostImage postImage = new PostImage ();
		
		postImageService.createPostImage (postImage);
		
		Mockito.verify (postImageRepo).save (postImage);
	}
}