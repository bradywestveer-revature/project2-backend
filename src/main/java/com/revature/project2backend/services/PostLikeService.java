package com.revature.project2backend.services;

import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.repositories.PostLikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {
	private final PostLikeRepo postLikeRepo;
	
	@Autowired
	public PostLikeService (PostLikeRepo postLikeRepo) {
		this.postLikeRepo = postLikeRepo;
	}
	
	public void createPostLike (PostLike postLike) {
		this.postLikeRepo.save (postLike);
	}
	
	public void deletePostLike (PostLike postLike) {
		this.postLikeRepo.delete (postLike);
	}
}
