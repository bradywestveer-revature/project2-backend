package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@WebMvcTest (PostLikeController.class)
public class PostLikeControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PostLikeService postLikeService;
	
	@MockBean
	private PostService postService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	@Test
	void createPostLikeWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/postlike")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (new CreateLikeBody ())))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void createPostLikeWhenInvalidPostId () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		mvc.perform (MockMvcRequestBuilders.post ("/postlike")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (new CreateLikeBody ())))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid post id")))));
	}
	
	@Test
	void createPostLikeWhenPostAlreadyLiked () throws Exception {
		int userId = 1;
		int postId = 1;
		
		User user = new User ();
		
		user.setId (userId);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		Post post = new Post ();
		
		post.setId (postId);
		post.setLikes (new ArrayList <> ());
		
		post.getLikes ().add (new PostLike (post, user));
		
		CreateLikeBody createLikeBody = new CreateLikeBody ();
		
		createLikeBody.setPostId (post.getId ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		mvc.perform (MockMvcRequestBuilders.post ("/postlike")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createLikeBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Post is already liked by user with id: " + user.getId ())))));
	}
	
	@Test
	void createPostLike () throws Exception {
		int postId = 1;
		
		MockHttpSession mockHttpSession = new MockHttpSession ();

		mockHttpSession.setAttribute ("user", new User ());

		Post post = new Post ();

		post.setId (postId);
		post.setLikes (new ArrayList <> ());
		
		CreateLikeBody createLikeBody = new CreateLikeBody ();
		
		createLikeBody.setPostId (post.getId ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		mvc.perform (MockMvcRequestBuilders.post ("/postlike")
				.contentType (MediaType.APPLICATION_JSON)
				.session (mockHttpSession)
				.content (json.writeValueAsString (createLikeBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Created like", true))));
	}
	
	@Test
	void deletePostLikeWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.delete ("/postlike/1")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void deletePostLikeWhenPostNotLiked () throws Exception {
		int postId = 1;
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		Post post = new Post ();
		
		post.setId (postId);
		post.setLikes (new ArrayList <> ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		mvc.perform (MockMvcRequestBuilders.delete ("/postlike/" + postId)
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Post is not liked by user with id: null")))));
	}
	
	@Test
	void deletePostLike () throws Exception {
		int postId = 1;
		
		User user = new User ();
		
		user.setId (1);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		Post post = new Post ();
		
		post.setId (postId);
		post.setLikes (new ArrayList <> ());
		
		post.getLikes ().add (new PostLike (post, user));
		
		CreateLikeBody createLikeBody = new CreateLikeBody ();
		
		createLikeBody.setPostId (post.getId ());
		
		Mockito.when (postService.getPost (postId)).thenReturn (post);
		
		mvc.perform (MockMvcRequestBuilders.delete ("/postlike/" + postId)
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Deleted like", true))));
	}
}
