package com.revature.project2backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreatePostBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.*;
import com.revature.project2backend.services.PostImageService;
import com.revature.project2backend.services.PostService;
import com.revature.project2backend.services.UserService;
import com.revature.project2backend.utilities.S3Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * The PostController is responsible for mapping all endpoints necessary for passing data, pertaining to Posts, from
 * the client to the server.
 */
@RestController
@RequestMapping ("post")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class PostController {

	/**
	 * An instance of PostService used for accessing the methods in the class.
	 */
	private final PostService postService;

	/**
	 * An instance of PostImageService used for accessing the methods in the class.
	 */
	private final PostImageService postImageService;

	/**
	 * An instance of UserService used for accessing the methods in the class.
	 */
	private final UserService userService;

	/**
	 * This Constructor initializes UserService, PostService, and PostImageService.
	 *
	 * @param postService An instance of PostService used for accessing the methods in the class.
	 * @param postImageService An instance of PostImageService used for accessing the methods in the class.
	 * @param userService An instance of UserService used for accessing the methods in the class.
	 */
	@Autowired
	public PostController (PostService postService, PostImageService postImageService, UserService userService) {
		this.postService = postService;
		this.postImageService = postImageService;
		this.userService = userService;
	}

	/**
	 * Validates a PostObject to ensure the body is not empty or null.
	 *
	 * @param post A Post Object
	 * @throws InvalidValueException Thrown when the body is null or empty
	 */
	private void validatePost (Post post) throws InvalidValueException {
		if (post.getCreator () == null || post.getBody () == null || post.getCreated () == null) {
			throw new InvalidValueException ("Invalid post");
		}
		
		if (post.getBody ().trim ().equals ("")) {
			throw new InvalidValueException ("Invalid post");
		}
	}

	/**
	 * Formats a page of posts data for sending to the client.
	 *
	 * @param postList A List of posts that need to be formatted for the client
	 * @return A List of formatted posts data
	 */
	private List <Map <String, Object>> formatPosts (List <Post> postList) {
		List <Map <String, Object>> posts = new ArrayList <> ();
		
		for (Post postData : postList) {
			posts.add (formatPost (postData));
		}
		
		return posts;
	}

	/**
	 * Formats the data in a post.
	 *
	 * @param postData A Post Object who data needs to be formatted
	 * @return A Post Object with formatted data
	 */
	private Map <String, Object> formatPost (Post postData) {
		Map <String, Object> post = new HashMap <> ();
		
		post.put ("id", postData.getId ());
		post.put ("creatorId", postData.getCreator ().getId ());
		post.put ("body", postData.getBody ());
		
		List <String> imageUrls = new ArrayList <> ();
		
		for (PostImage postImage : postData.getImages ()) {
			imageUrls.add (S3Utilities.url + postImage.getPath ());
		}
		
		post.put ("imageUrls", imageUrls);
		
		Map <Integer, Integer> likes = new HashMap <> ();
		
		for (PostLike postLike : postData.getLikes ()) {
			likes.put (postLike.getCreator ().getId (), postLike.getId ());
		}
		
		post.put ("likes", likes);
		
		List <Map <String, Object>> comments = new ArrayList <> ();
		
		for (Comment commentData : postData.getComments ()) {
			Map <String, Object> comment = new HashMap <> ();
			
			comment.put ("id", commentData.getId ());
			comment.put ("creatorId", commentData.getCreator ().getId ());
			comment.put ("body", commentData.getBody ());
			comment.put ("created", commentData.getCreated ());
			
			comments.add (comment);
		}
		
		//sort newest to oldest, todo use created
		Collections.reverse (comments);
		
		post.put ("comments", comments);
		
		post.put ("created", postData.getCreated ().toString ());
		
		return post;
	}

	/**
	 * Creates and Post Object and sends it to the database
	 *
	 * @param body A DTO containing a List of images and a String for the body
	 * @param httpSession A session started by the user
	 * @return A JsonResponse notifying the user the post was created
	 * @throws UnauthorizedException Thrown when the user is associated with the session
	 * @throws InvalidValueException Thrown when the post body is empty and there are no images
	 */
	@PostMapping
	public ResponseEntity <JsonResponse> createPost (@RequestBody CreatePostBody body, HttpSession httpSession) throws UnauthorizedException, InvalidValueException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		Post post = new Post (user, body.getBody (), new Date (System.currentTimeMillis ()));
		
		validatePost (post);
		
		postService.createPost (post);
		
		List <PostImage> postImages = new ArrayList <> ();
		
		if (body.getImages () != null) {
			for (int i = 0; i < body.getImages ().size (); i++) {
				String imageFileName = body.getImages ().get (i).getOrDefault ("fileName", null);
				String imageData = body.getImages ().get (i).getOrDefault ("data", null);
				
				if (imageFileName == null || imageData == null) {
					continue;
				}
				
				//todo move image upload stuff to PostService?
				String path = System.currentTimeMillis () + String.valueOf (i) + user.getUsername () + imageFileName;
				
				S3Utilities.uploadImage (path, imageData);
				
				PostImage postImage = new PostImage (path, post);
				
				postImageService.createPostImage (postImage);
				
				postImages.add (postImage);
			}
			
			post.setImages (postImages);
			
			postService.updatePost (post);
		}
		
		return ResponseEntity.ok (new JsonResponse ("Created post", true));
	}

	/**
	 * Returns a page of posts. One page will contain up to a maximum of 20 posts.
	 *
	 * @param page The number of the page the user is trying to access
	 * @param httpSession A session started by the user
	 * @return A page of formatted posts up to a maximum of 20
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 */
	@GetMapping
	public ResponseEntity <JsonResponse> getPosts (@RequestParam Integer page, HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		List <Post> posts = postService.getPosts (page);
		
		return ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, formatPosts (posts)));
	}

	/**
	 * Returns formatted posts for a specific user. This user is not always the one currently occupying the session.
	 *
	 * @param userId The id of the user whose posts are being viewed.
	 * @param page The page number that posts are being loaded on up toa maximum of 20
	 * @param httpSession A session associated with the user.
	 * @return A list of posts from a specific User
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws NotFoundException Thrown when the user can't be found
	 */
	@GetMapping ("user")
	public ResponseEntity <JsonResponse> getUserPosts (@RequestParam Integer userId, @RequestParam Integer page, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		User user = userService.getUser (userId);
		
		List <Post> posts = postService.getUserPosts (user, page);
		
		return ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, formatPosts (posts)));
	}

	/**
	 * Uses a path parameter to find one specific post.
	 *
	 * @param id The path parameter containing the id of a post
	 * @param httpSession A session associated with the user.
	 * @return A post with a specific id
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws NotFoundException Thrown when the user can't be found
	 */
	@GetMapping ("{id}")
	public ResponseEntity <JsonResponse> getPost (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}

		return ResponseEntity.ok (new JsonResponse ("Found post", true, formatPost (postService.getPost (id))));
	}
}
