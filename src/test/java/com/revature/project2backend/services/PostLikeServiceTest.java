package com.revature.project2backend.services;

import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.repositories.PostLikeRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostLikeServiceTest {
	private final PostLikeService postLikeService;
	
	private final PostLikeRepo postLikeRepo = Mockito.mock (PostLikeRepo.class);
	
	public PostLikeServiceTest () {
		this.postLikeService = new PostLikeService (this.postLikeRepo);
	}
	
	@Test
	void createPostLike () {
		PostLike postLike = new PostLike ();
		
		postLikeService.createPostLike (postLike);
		
		Mockito.verify (postLikeRepo).save (postLike);
	}
	
	@Test
	void deletePostLike () {
		PostLike postLike = new PostLike ();
		
		postLikeService.deletePostLike (postLike);
		
		Mockito.verify (postLikeRepo).delete (postLike);
	}
}