package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreateCommentBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Comment;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.CommentService;
import com.revature.project2backend.services.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentControllerTest {
	private final CommentController commentController;
	
	private final CommentService commentService = Mockito.mock (CommentService.class);
	private final PostService postService = Mockito.mock (PostService.class);
	
	public CommentControllerTest () {
		this.commentController = new CommentController (this.commentService, this.postService);
	}
	
	@Test
	void createCommentWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.commentController.createComment (new CreateCommentBody (), new MockHttpSession ()));
		
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
		Mockito.verify (commentService, Mockito.never ()).createComment (Mockito.any ());
	}
	
	@Test
	void createCommentWhenPostIdIsNull () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.commentController.createComment (new CreateCommentBody (), mockHttpSession));
		
		assertEquals ("Error! Invalid post id", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
		Mockito.verify (commentService, Mockito.never ()).createComment (Mockito.any ());
	}
	
	@Test
	void createCommentWhenBodyIsNull () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.commentController.createComment (createCommentBody, mockHttpSession));
		
		assertEquals ("Error! Invalid comment", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
		Mockito.verify (commentService, Mockito.never ()).createComment (Mockito.any ());
	}
	
	@Test
	void createCommentWhenBodyIsEmpty () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		createCommentBody.setBody ("");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.commentController.createComment (createCommentBody, mockHttpSession));
		
		assertEquals ("Error! Invalid comment", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
		Mockito.verify (commentService, Mockito.never ()).createComment (Mockito.any ());
	}
	
	@Test
	void createCommentWhenBodyIsWhitespace () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		createCommentBody.setBody ("         ");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.commentController.createComment (createCommentBody, mockHttpSession));
		
		assertEquals ("Error! Invalid comment", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
		Mockito.verify (commentService, Mockito.never ()).createComment (Mockito.any ());
	}
	
	@Test
	void createComment () throws InvalidValueException, UnauthorizedException, NotFoundException {
		int postId = 1;
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (postId);
		createCommentBody.setBody ("body");
		
		Post post = new Post ();
		
		post.setComments (new ArrayList <> ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Created comment", true)), this.commentController.createComment (createCommentBody, mockHttpSession));
		
		Mockito.verify (postService).updatePost (post);
		Mockito.verify (commentService).createComment (Mockito.any (Comment.class));
	}
}
