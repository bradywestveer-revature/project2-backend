package com.revature.project2backend.services;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
	private final PostRepo postRepo;
	
	private final int postsPerPage = 5;
	
	@Autowired
	public PostService (PostRepo postRepo) {
		this.postRepo = postRepo;
	}
	
	public void createPost (Post post) {
		this.postRepo.save (post);
	}
	
	public List <Post> getPosts (Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		return this.postRepo.findAll (pageable).getContent ();
	}
	
	public List <Post> getUserPosts (User user, Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		return this.postRepo.findByCreator (user, pageable).getContent ();
	}
	
	public Post getPost (Integer id) {
		return postRepo.findById (id).orElse (null);
	}
	
	public void updatePost (Post post) {
		postRepo.save (post);
	}
}
