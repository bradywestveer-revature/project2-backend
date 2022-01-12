package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreateCommentBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.CommentService;
import com.revature.project2backend.services.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@WebMvcTest (CommentController.class)
public class CommentControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CommentService commentService;
	
	@MockBean
	private PostService postService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	@Test
	void createCommentWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (new CreateCommentBody ())))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void createCommentWhenPostIdIsNull () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setBody ("body");
		
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createCommentBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid post id")))));
	}
	
	@Test
	void createCommentWhenBodyIsNull () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createCommentBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid comment")))));
	}
	
	@Test
	void createCommentWhenBodyIsEmpty () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		createCommentBody.setBody ("");
		
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createCommentBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid comment")))));
	}
	
	@Test
	void createCommentWhenBodyIsWhitespace () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		createCommentBody.setBody ("         ");
		
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createCommentBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid comment")))));
	}
	
	@Test
	void createComment () throws Exception {
		int postId = 1;
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreateCommentBody createCommentBody = new CreateCommentBody ();
		
		createCommentBody.setPostId (1);
		createCommentBody.setBody ("test body");
		
		Post post = new Post ();
		
		post.setComments (new ArrayList <> ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		mvc.perform (MockMvcRequestBuilders.post ("/comment")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createCommentBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Created comment", true))));
	}
}
