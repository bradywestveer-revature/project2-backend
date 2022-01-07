package com.revature.project2backend.controllers;

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

@RestController
@RequestMapping ("post")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class PostController {
	private final PostService postService;
	private final PostImageService postImageService;
	private final UserService userService;
	
	@Autowired
	public PostController (PostService postService, PostImageService postImageService, UserService userService) {
		this.postService = postService;
		this.postImageService = postImageService;
		this.userService = userService;
	}
	
	private void validatePost (Post post) throws InvalidValueException {
		if (post.getCreator () == null || post.getBody () == null || post.getCreated () == null) {
			throw new InvalidValueException ("Invalid post");
		}
		
		if (post.getBody ().trim ().equals ("")) {
			throw new InvalidValueException ("Invalid post");
		}
	}
	
	private List <Map <String, Object>> formatPosts (List <Post> postList) {
		List <Map <String, Object>> posts = new ArrayList <> ();
		
		for (Post postData : postList) {
			posts.add (formatPost (postData));
		}
		
		return posts;
	}
	
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
	
	@PostMapping
	public ResponseEntity <JsonResponse> createPost (@RequestBody CreatePostBody body, HttpSession httpSession) throws UnauthorizedException, InvalidValueException, IOException {
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
	
	@GetMapping
	public ResponseEntity <JsonResponse> getPosts (@RequestParam Integer page, HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		List <Post> posts = postService.getPosts (page);
		
		return ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, formatPosts (posts)));
	}
	
	@GetMapping ("user")
	public ResponseEntity <JsonResponse> getUserPosts (@RequestParam Integer userId, @RequestParam Integer page, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		User user = userService.getUser (userId);
		
		List <Post> posts = postService.getUserPosts (user, page);
		
		return ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, formatPosts (posts)));
	}
	
	@GetMapping ("{id}")
	public ResponseEntity <JsonResponse> getPost (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}

		return ResponseEntity.ok (new JsonResponse ("Found post", true, formatPost (postService.getPost (id))));
	}
}
