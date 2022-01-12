package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreatePostBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.*;
import com.revature.project2backend.services.PostImageService;
import com.revature.project2backend.services.PostService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest (PostController.class)
public class PostControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PostService postService;
	
	@MockBean
	private PostImageService postImageService;
	
	@MockBean
	private UserService userService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	@Test
	void createPostWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (new CreatePostBody ())))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void createPostWhenBodyIsNull () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setImages (new ArrayList <> ());
		
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createPostBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid post")))));
	}
	
	@Test
	void createPostWhenBodyIsEmpty () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("");
		createPostBody.setImages (new ArrayList <> ());
		
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createPostBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid post")))));
	}
	
	@Test
	void createPostWhenBodyIsWhitespace () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("       ");
		createPostBody.setImages (new ArrayList <> ());
		
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createPostBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid post")))));
	}
	
	@Test
	void createPost () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("test body");
		
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createPostBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Created post", true))));
	}
	
	@Test
	void createPostWithImages () throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("test body");
		createPostBody.setImages (new ArrayList <> ());
		
		mvc.perform (MockMvcRequestBuilders.post ("/post")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession)
			.content (json.writeValueAsString (createPostBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Created post", true))));
	}
	
	@Test
	void getPostsWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.get ("/post?page=0")
				.contentType (MediaType.APPLICATION_JSON)
				.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void getPosts () throws Exception {
		int page = 0;
		
		User user = new User ();
		
		user.setId (1);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		List <Post> posts = new ArrayList <> ();
		
		Post post1 = new Post (1, user, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post1.getImages ().add (new PostImage (1, "pathtoimage", post1));
		
		Post post2 = new Post (2, user, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post2.getComments ().add (new Comment (1, user, post2, "body", new Date ()));
		
		Post post3 = new Post (3, user, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post3.getLikes ().add (new PostLike (1, user, post3));
		
		posts.add (post1);
		posts.add (post2);
		posts.add (post3);
		
		Mockito.when (postService.getPosts (0)).thenReturn (posts);
		
		MockHttpServletResponse response = mvc.perform (MockMvcRequestBuilders.get ("/post?page=" + page)
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andReturn ().getResponse ();
		
		//testing the body normally doesn't work for some reason, so we are returning the body and testing the returned value
		assertEquals (MockHttpServletResponse.SC_OK, response.getStatus ());
		
		//test createds
		Pattern postCreatedPattern = Pattern.compile ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+");
		
		Matcher postMatcher = postCreatedPattern.matcher (response.getContentAsString ());
		
		//test for 3 matches of the format that the posts' createds are in
		int numberOfMatches = 0;
		
		while (postMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (3, numberOfMatches);
		
		//test for 1 match of the format that the comment's created is in
		numberOfMatches = 0;
		
		Pattern commentCreatedPattern = Pattern.compile ("\\d+-\\d+-\\d+T+\\d+:\\d+:\\d+.\\d+\\+\\d+:\\d+");
		
		Matcher commentMatcher = commentCreatedPattern.matcher (response.getContentAsString ());
		
		while (commentMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (1, numberOfMatches);
		
		//test everything except createds
		assertEquals ("{\"message\":\"Found 3 posts\",\"success\":true,\"data\":[{\"comments\":[],\"created\":\"\",\"imageUrls\":[\"https://jwa-s3.s3.us-east-2.amazonaws.com/pathtoimage\"],\"creatorId\":1,\"id\":1,\"body\":\"test body\",\"likes\":{}},{\"comments\":[{\"created\":\"\",\"creatorId\":1,\"id\":1,\"body\":\"body\"}],\"created\":\"\",\"imageUrls\":[],\"creatorId\":1,\"id\":2,\"body\":\"test body\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":1,\"id\":3,\"body\":\"test body\",\"likes\":{\"1\":1}}],\"redirect\":null}", response.getContentAsString ().replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
	}
	
	@Test
	void getUserPostsWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.get ("/post/user?userId=1&page=0")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void getUserPosts () throws Exception {
		int page = 0;
		
		User user1 = new User ();
		
		user1.setId (1);
		
		User user2 = new User ();
		
		user2.setId (2);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user1);
		
		List <Post> posts = new ArrayList <> ();
		
		Post post1 = new Post (1, user2, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post1.getImages ().add (new PostImage (1, "pathtoimage", post1));
		
		Post post2 = new Post (2, user1, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post2.getComments ().add (new Comment (1, user2, post2, "body", new Date ()));
		
		Post post3 = new Post (3, user1, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post3.getLikes ().add (new PostLike (1, user1, post3));
		
		posts.add (post2);
		posts.add (post3);
		
		Mockito.when (userService.getUser (user1.getId ())).thenReturn (user1);
		Mockito.when (postService.getUserPosts (user1, 0)).thenReturn (posts);
		
		MockHttpServletResponse response = mvc.perform (MockMvcRequestBuilders.get ("/post/user?userId=" + user1.getId () + "&page=" + page)
				.contentType (MediaType.APPLICATION_JSON)
				.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andReturn ().getResponse ();
		
		//testing the body normally doesn't work for some reason, so we are returning the body and testing the returned value
		assertEquals (MockHttpServletResponse.SC_OK, response.getStatus ());
		
		//test createds
		Pattern postCreatedPattern = Pattern.compile ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+");
		
		Matcher postMatcher = postCreatedPattern.matcher (response.getContentAsString ());
		
		//test for 3 matches of the format that the posts' createds are in
		int numberOfMatches = 0;
		
		while (postMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (2, numberOfMatches);
		
		//test for 1 match of the format that the comment's created is in
		numberOfMatches = 0;
		
		Pattern commentCreatedPattern = Pattern.compile ("\\d+-\\d+-\\d+T+\\d+:\\d+:\\d+.\\d+\\+\\d+:\\d+");
		
		Matcher commentMatcher = commentCreatedPattern.matcher (response.getContentAsString ());
		
		while (commentMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (1, numberOfMatches);
		
		//test everything except createds
		assertEquals ("{\"message\":\"Found 2 posts\",\"success\":true,\"data\":[{\"comments\":[{\"created\":\"\",\"creatorId\":2,\"id\":1,\"body\":\"body\"}],\"created\":\"\",\"imageUrls\":[],\"creatorId\":1,\"id\":2,\"body\":\"test body\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":1,\"id\":3,\"body\":\"test body\",\"likes\":{\"1\":1}}],\"redirect\":null}", response.getContentAsString ().replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
	}
	
	@Test
	void getPostWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.get ("/post/1")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void getPost () throws Exception {
		User user = new User ();
		
		user.setId (1);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		Post post = new Post (1, user, "test body", new ArrayList <> (), new ArrayList <> (), new ArrayList <> (), new Date ());
		
		post.getImages ().add (new PostImage (1, "pathtoimage", post));
		post.getComments ().add (new Comment (1, user, post, "body", new Date ()));
		post.getLikes ().add (new PostLike (1, user, post));
		
		Mockito.when (postService.getPost (post.getId ())).thenReturn (post);
		
		MockHttpServletResponse response = mvc.perform (MockMvcRequestBuilders.get ("/post/" + post.getId ())
				.contentType (MediaType.APPLICATION_JSON)
				.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andReturn ().getResponse ();
		
		//testing the body normally doesn't work for some reason, so we are returning the body and testing the returned value
		assertEquals (MockHttpServletResponse.SC_OK, response.getStatus ());
		
		//test createds
		Pattern postCreatedPattern = Pattern.compile ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+");
		
		Matcher postMatcher = postCreatedPattern.matcher (response.getContentAsString ());
		
		//test for 3 matches of the format that the posts' createds are in
		int numberOfMatches = 0;
		
		while (postMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (1, numberOfMatches);
		
		//test for 1 match of the format that the comment's created is in
		numberOfMatches = 0;
		
		Pattern commentCreatedPattern = Pattern.compile ("\\d+-\\d+-\\d+T+\\d+:\\d+:\\d+.\\d+\\+\\d+:\\d+");
		
		Matcher commentMatcher = commentCreatedPattern.matcher (response.getContentAsString ());
		
		while (commentMatcher.find ()) {
			numberOfMatches += 1;
		}
		
		assertEquals (1, numberOfMatches);
		
		//test everything except createds
		assertEquals ("{\"message\":\"Found post\",\"success\":true,\"data\":{\"comments\":[{\"created\":\"\",\"creatorId\":1,\"id\":1,\"body\":\"body\"}],\"created\":\"\",\"imageUrls\":[\"https://jwa-s3.s3.us-east-2.amazonaws.com/pathtoimage\"],\"creatorId\":1,\"id\":1,\"body\":\"test body\",\"likes\":{\"1\":1}},\"redirect\":null}", response.getContentAsString ().replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
	}
}
