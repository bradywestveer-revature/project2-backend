package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PostRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {
	private final PostService postService;
	
	private final PostRepo postRepo = Mockito.mock (PostRepo.class);
	
	public PostServiceTest () {
		this.postService = new PostService (this.postRepo);
	}
	
	@Test
	void createPost () {
		Post post = new Post (new User (), "body", new Date ());
		
		postService.createPost (post);
		
		Mockito.verify (postRepo).save (post);
	}
	
	@Test
	void getPosts () {
		List <Post> posts = new ArrayList <> ();
		
		posts.add (new Post (new User (), "body", new Date ()));
		posts.add (new Post (new User (), "body", new Date ()));
		posts.add (new Post (new User (), "body", new Date ()));
		
		Mockito.when (postRepo.findAll (Mockito.any (Pageable.class))).thenReturn (new PageImpl <> (posts));
		
		assertEquals (posts, postService.getPosts (0));
	}
	
	@Test
	void getUserPosts () {
		List <Post> posts = new ArrayList <> ();
		
		posts.add (new Post (new User (), "body", new Date ()));
		posts.add (new Post (new User (), "body", new Date ()));
		posts.add (new Post (new User (), "body", new Date ()));
		
		Mockito.when (postRepo.findByCreator (Mockito.any (User.class), Mockito.any (Pageable.class))).thenReturn (new PageImpl <> (posts));
		
		assertEquals (posts, postService.getUserPosts (new User (), 0));
	}
	
	@Test
	void getPost () throws NotFoundException {
		int id = 1;
		
		Post post = new Post (new User (), "body", new Date ());
		
		post.setId (id);
		
		Mockito.when (postRepo.findById (id)).thenReturn (java.util.Optional.of (post));
		
		assertEquals (post, postService.getPost (id));
	}
	
	@Test
	void getPostWhenInvalidPostId () {
		int id = 1;
		
		Mockito.when (postRepo.findById (id)).thenReturn (Optional.empty ());
		
		assertThrows (NotFoundException.class, () -> {
			postService.getPost (id);
		});
	}
	
	@Test
	void updatePost () {
		Post post = new Post (new User (), "body", new Date ());
		
		postService.updatePost (post);
		
		Mockito.verify (postRepo).save (post);
	}
}