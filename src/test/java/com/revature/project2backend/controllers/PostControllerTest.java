package com.revature.project2backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreatePostBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.PostImage;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PostImageService;
import com.revature.project2backend.services.PostService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PostControllerTest {
	private final PostController postController;
	private final PostService postService = Mockito.mock (PostService.class);
	private final PostImageService postImageService = Mockito.mock (PostImageService.class);
	private final UserService userService = Mockito.mock (UserService.class);
	
	private final ObjectMapper json = new ObjectMapper ();
	
	private final User user1 = new User ("John", "Smith", "johnsmith@example.com", "johnsmith", "password");
	private final User user2 = new User ("Sarah", "Smith", "sarahsmith@example.com", "sarahsmith", "password");
	private final List <Post> posts = new ArrayList <> ();
	private final List <Post> userPosts = new ArrayList <> ();
	
	public PostControllerTest () {
		this.postController = new PostController (postService, postImageService, userService);
		
		for (int i = 0; i < 5; i++) {
			Post post = new Post (user2, "post body " + i, new Date (System.currentTimeMillis ()));
			
			//+1 to start at 1
			post.setId (i + 1);
			post.setImages (new ArrayList <> ());
			post.setLikes (new ArrayList <> ());
			post.setComments (new ArrayList <> ());
			
			posts.add (post);
		}
		
		for (int i = 0; i < 5; i++) {
			Post post = new Post (user1, "user post body " + i, new Date (System.currentTimeMillis ()));
			
			//+6 to start at 1 and after above for loop
			post.setId (i + 6);
			post.setImages (new ArrayList <> ());
			post.setLikes (new ArrayList <> ());
			post.setComments (new ArrayList <> ());
			
			posts.add (post);
			userPosts.add (post);
		}
	}
	
	@Test
	void createPostWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.postController.createPost (new CreatePostBody (), new MockHttpSession ()));
		
		Mockito.verify (postService, Mockito.never ()).createPost (Mockito.any ());
		Mockito.verify (postImageService, Mockito.never ()).createPostImage (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostWhenBodyIsNull () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postController.createPost (createPostBody, mockHttpSession));
		
		assertEquals ("Error! Invalid post", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).createPost (Mockito.any ());
		Mockito.verify (postImageService, Mockito.never ()).createPostImage (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostWhenBodyIsEmpty () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postController.createPost (createPostBody, mockHttpSession));
		
		assertEquals ("Error! Invalid post", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).createPost (Mockito.any ());
		Mockito.verify (postImageService, Mockito.never ()).createPostImage (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostWhenBodyIsWhitespace () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("          ");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.postController.createPost (createPostBody, mockHttpSession));
		
		assertEquals ("Error! Invalid post", exception.getMessage ());
		
		Mockito.verify (postService, Mockito.never ()).createPost (Mockito.any ());
		Mockito.verify (postImageService, Mockito.never ()).createPostImage (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPost () throws InvalidValueException, UnauthorizedException, IOException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("body");
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Created post", true)), this.postController.createPost (createPostBody, mockHttpSession));
		
		Mockito.verify (postService).createPost (Mockito.any (Post.class));
		
		//todo make sure s3.uploadimage wasn't called
		//todo make sure post.images is empty
		
		Mockito.verify (postImageService, Mockito.never ()).createPostImage (Mockito.any ());
		Mockito.verify (postService, Mockito.never ()).updatePost (Mockito.any ());
	}
	
	@Test
	void createPostWithImages () throws InvalidValueException, UnauthorizedException, IOException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		CreatePostBody createPostBody = new CreatePostBody ();
		
		createPostBody.setBody ("body");
		
		List <Map <String, String>> images = new ArrayList <> ();
		
		Map <String, String> image1 = new HashMap <> ();
		
		image1.put ("fileName", "test.png");
		image1.put ("data", "testdata");
		
		images.add (image1);
		
		createPostBody.setImages (images);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Created post", true)), this.postController.createPost (createPostBody, mockHttpSession));
		
		//todo mock s3utilities
		//todo make sure s3.uploadImage was called
		//todo make sure post.images has image data in it
		
		Mockito.verify (postImageService).createPostImage (Mockito.any (PostImage.class));
		Mockito.verify (postService).updatePost (Mockito.any (Post.class));
		
		Mockito.verify (postService).createPost (Mockito.any (Post.class));
	}
	
	@Test
	void getPostsWhenNotLoggedIn () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		assertThrows (UnauthorizedException.class, () -> this.postController.getPosts (0, mockHttpSession));
	}
	
	@Test
	void getPosts () throws UnauthorizedException, JsonProcessingException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		int page = 0;
		
		Mockito.when (postService.getPosts (page)).thenReturn (posts);
		
		ResponseEntity <JsonResponse> actualResult = this.postController.getPosts (page, mockHttpSession);
		
		List <Map <String, Object>> data = (List <Map <String, Object>>) actualResult.getBody ().getData ();
		
		//test createds
		for (Map <String, Object> post : data) {
			assertTrue (post.get ("created").toString ().matches ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+"));
		}
		
		//test everything else (createds are set to empty strings)
		assertEquals ("[{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":1,\"body\":\"post body 0\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":2,\"body\":\"post body 1\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":3,\"body\":\"post body 2\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":4,\"body\":\"post body 3\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":5,\"body\":\"post body 4\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":6,\"body\":\"user post body 0\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":7,\"body\":\"user post body 1\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":8,\"body\":\"user post body 2\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":9,\"body\":\"user post body 3\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":10,\"body\":\"user post body 4\",\"likes\":{}}]",
			json.writeValueAsString (actualResult.getBody ().getData ()).replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
		
		//set data to null to test that everything else about the response
		actualResult.getBody ().setData (null);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, null)), actualResult); 
	}
	
	@Test
	void getUserPostsWhenNotLoggedIn () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		assertThrows (UnauthorizedException.class, () -> this.postController.getUserPosts (1, 0, mockHttpSession));
	}
	
	@Test
	void getUserPosts () throws UnauthorizedException, NotFoundException, JsonProcessingException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		int page = 0;
		
		Mockito.when (userService.getUser (user1.getId ())).thenReturn (user1);
		Mockito.when (postService.getUserPosts (user1, page)).thenReturn (userPosts);
		
		ResponseEntity <JsonResponse> actualResult = this.postController.getUserPosts (user1.getId (), page, mockHttpSession);
		
		List <Map <String, Object>> data = (List <Map <String, Object>>) actualResult.getBody ().getData ();
		
		//test createds
		for (Map <String, Object> post : data) {
			assertTrue (post.get ("created").toString ().matches ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+"));
		}
		
		//test everything else (createds are set to empty strings)
		assertEquals ("[{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":6,\"body\":\"user post body 0\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":7,\"body\":\"user post body 1\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":8,\"body\":\"user post body 2\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":9,\"body\":\"user post body 3\",\"likes\":{}},{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":10,\"body\":\"user post body 4\",\"likes\":{}}]",
			json.writeValueAsString (actualResult.getBody ().getData ()).replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
		
		//set data to null to test that everything else about the response
		actualResult.getBody ().setData (null);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Found " + userPosts.size () + " posts", true, null)), actualResult);
	}
	
	@Test
	void getPostWhenNotLoggedIn () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		assertThrows (UnauthorizedException.class, () -> this.postController.getPost (1, mockHttpSession));
	}
	
	@Test
	void getPost () throws NotFoundException, JsonProcessingException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		int postId = posts.get (0).getId ();
		
		Mockito.when (postService.getPost (postId)).thenReturn (posts.get (0));
		
		ResponseEntity <JsonResponse> actualResult = this.postController.getPost (postId, mockHttpSession);
		
		Map <String, Object> post = (Map <String, Object>) actualResult.getBody ().getData ();
		
		//test created
		assertTrue (post.get ("created").toString ().matches ("[a-zA-Z]+ [a-zA-Z]+ \\d+ \\d+:\\d+:\\d+ [a-zA-Z]+ \\d+"));
		
		//test everything else (createds are set to empty strings)
		assertEquals ("{\"comments\":[],\"created\":\"\",\"imageUrls\":[],\"creatorId\":null,\"id\":1,\"body\":\"post body 0\",\"likes\":{}}",
			json.writeValueAsString (actualResult.getBody ().getData ()).replaceAll ("\"created\":\".+?\"", "\"created\":\"\""));
		
		//set data to null to test that everything else about the response
		actualResult.getBody ().setData (null);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Found post", true, null)), actualResult);
	}
}
