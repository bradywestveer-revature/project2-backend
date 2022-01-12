package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreateLikeBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PostLikeService;
import com.revature.project2backend.services.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PostLikeControllerTest {
	private final PostLikeController postLikeController;
	private final PostLikeService postLikeService = Mockito.mock (PostLikeService.class);
	private final PostService postService = Mockito.mock (PostService.class);
	
	private final Post post = new Post (new User (), "body", new Date ());
	
	public PostLikeControllerTest () {
		this.postLikeController = new PostLikeController (postLikeService, postService);
		
		post.setId (1);
		
		post.getCreator ().setId (1);
		
		post.setImages (new ArrayList <> ());
		post.setLikes (new ArrayList <> ());
		post.setComments (new ArrayList <> ());
	}
	
	@Test
	void createPostLikeWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.postLikeController.createPostLike (new CreateLikeBody (), new MockHttpSession ()));
		
		Mockito.verify (postLikeService, Mockito.never ()).createPostLike (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostLikeWhenInvalidPostId () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postLikeController.createPostLike (new CreateLikeBody (), mockHttpSession));
		
		assertEquals ("Error! Invalid post id", exception.getMessage ());
		
		Mockito.verify (postLikeService, Mockito.never ()).createPostLike (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostLikeWhenPostAlreadyLiked () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", post.getCreator ());
		
		Mockito.when (postService.getPost (post.getId ())).thenReturn (post);
		
		post.getLikes ().add (new PostLike (post, post.getCreator ()));
		
		CreateLikeBody createLikeBody = new CreateLikeBody ();
		
		createLikeBody.setPostId (post.getId ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postLikeController.createPostLike (createLikeBody, mockHttpSession));
		
		assertEquals ("Error! Post is already liked by user with id: " + post.getCreator ().getId (), exception.getMessage ());
		
		post.getLikes ().remove (0);
		
		Mockito.verify (postLikeService, Mockito.never ()).createPostLike (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostLike () throws InvalidValueException, UnauthorizedException, NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		Mockito.when (postService.getPost (post.getId ())).thenReturn (post);
		
		CreateLikeBody createLikeBody = new CreateLikeBody ();
		
		createLikeBody.setPostId (post.getId ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Created like", true)), this.postLikeController.createPostLike (createLikeBody, mockHttpSession));
		
		Mockito.verify (postService).updatePost (Mockito.any (Post.class));
		Mockito.verify (postLikeService).createPostLike (Mockito.any (PostLike.class));
	}
	
	@Test
	void deletePostLikeWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.postLikeController.deletePostLike (1, new MockHttpSession ()));
		
		Mockito.verify (postLikeService, Mockito.never ()).deletePostLike (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void deletePostLikeWhenPostNotLiked () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", post.getCreator ());
		
		Mockito.when (postService.getPost (post.getId ())).thenReturn (post);
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postLikeController.deletePostLike (post.getId (), mockHttpSession));
		
		assertEquals ("Error! Post is not liked by user with id: " + post.getCreator ().getId (), exception.getMessage ());
		
		Mockito.verify (postLikeService, Mockito.never ()).deletePostLike (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void deletePostLike () throws NotFoundException, InvalidValueException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", post.getCreator ());
		
		Mockito.when (postService.getPost (post.getId ())).thenReturn (post);
		
		post.getLikes ().add (new PostLike (post, post.getCreator ()));
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Deleted like", true)), this.postLikeController.deletePostLike (post.getId (), mockHttpSession));
		
		Mockito.verify (postLikeService).deletePostLike (Mockito.any (PostLike.class));
		Mockito.verify (postService).updatePost (Mockito.any (Post.class));
	}
}
