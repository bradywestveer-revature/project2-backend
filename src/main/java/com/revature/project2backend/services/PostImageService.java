package com.revature.project2backend.services;

import com.revature.project2backend.models.PostImage;
import com.revature.project2backend.repositories.PostImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostImageService {
	private final PostImageRepo postImageRepo;
	
	@Autowired
	public PostImageService (PostImageRepo postImageRepo) {
		this.postImageRepo = postImageRepo;
	}
	
	public void createPostImage (PostImage postImage) {
		postImageRepo.save (postImage);
	}
}
