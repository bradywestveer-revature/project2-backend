package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.NotFoundException;
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
	
	private final int postsPerPage = 20;
	
	@Autowired
	public PostService (PostRepo postRepo) {
		this.postRepo = postRepo;
	}
	
	public void createPost (Post post) {
		this.postRepo.save (post);
	}
	
	public List <Post> getPosts (Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		//todo do we need to check for null? When i was mocking without passing in pageable I was getting a NullPointerException, so findAll could return null somehow?
		return this.postRepo.findAll (pageable).getContent ();
	}
	
	public List <Post> getUserPosts (User user, Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		return this.postRepo.findByCreator (user, pageable).getContent ();
	}
	
	public Post getPost (Integer id) throws NotFoundException {
		Post post = postRepo.findById (id).orElse (null);
		
		if (post == null) {
			throw new NotFoundException ("Post with id: " + id + " not found");
		}
		
		return post;
	}
	
	public void updatePost (Post post) {
		postRepo.save (post);
	}
}
