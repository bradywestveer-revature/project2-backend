package com.revature.project2backend.services;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
	private final PostRepo postRepo;
	
	@Autowired
	public PostService (PostRepo postRepo) {
		this.postRepo = postRepo;
	}
	
	public void createPost (Post post) {
		this.postRepo.save (post);
	}
	
	public List <Post> getPosts (Integer pageNum) {
		Pageable pageable = PageRequest.of (pageNum, 20, Sort.by ("created").descending ());
		
		Slice <Post> slice = this.postRepo.findAll (pageable);
		
		return slice.getContent ();
	}
	
	public List <Post> getUserPosts (Integer userId, Integer page) {
		return new ArrayList <> ();
	}
	
	public Post getPost (Integer id) {
		return null;
	}
	
	public void updatePost (Post post) {
		postRepo.save (post);
	}
	
	public Slice <Post> getProfilePosts (Integer profileId) {
		Pageable pageable = PageRequest.of (0, 20, Sort.by ("created").descending ());
		
		return this.postRepo.findByCreator (profileId, pageable);
	}
}
