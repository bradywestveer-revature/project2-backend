package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.PostImage;
import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PostImageService;
import com.revature.project2backend.services.PostService;
import com.revature.project2backend.utilities.S3Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping ("post")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class PostController {
	private final PostService postService;
	private final PostImageService postImageService;
	
	@Autowired
	public PostController (PostService postService, PostImageService postImageService) {
		this.postService = postService;
		this.postImageService = postImageService;
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
			Map <String, Object> post = new HashMap <> ();
			
			post.put ("id", postData.getId ());
			post.put ("creatorId", postData.getCreator ().getId ());
			post.put ("body", postData.getBody ());
			
			List <String> imageUrls = new ArrayList <> ();
			
			for (PostImage postImage : postData.getImages ()) {
				imageUrls.add (S3Utilities.urlPrefix + postImage.getPath ());
			}
			
			post.put ("imageUrls", imageUrls);
			
			Map <Integer, Integer> likes = new HashMap <> ();
			
			for (PostLike postLike : postData.getLikes ()) {
				likes.put (postLike.getCreator ().getId (), postLike.getId ());
			}
			
			post.put ("likes", likes);
			
			post.put ("comments", postData.getComments ());
			
			posts.add (post);
		}
		
		return posts;
	}
	
	//todo refactor use CreatePostBody instead of Map 
	@PostMapping
	public ResponseEntity <JsonResponse> createPost (@RequestBody Map <String, Object> body, HttpSession httpSession) throws UnauthorizedException, InvalidValueException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		Post post = new Post (user, (String) body.getOrDefault ("body", null), new Date (System.currentTimeMillis ()));
		
		validatePost (post);
		
		postService.createPost (post);
		
		List <PostImage> postImages = new ArrayList <> ();
		
		Object images = body.get ("images");
		
		if (images != null) {
			if (!(images instanceof List <?>)) {
				throw new InvalidValueException ("Invalid images");
			}
			
			if (((List<?>) images).size () > 0 && !(((List<?>) images).get (0) instanceof String)) {
				throw new InvalidValueException ("Invalid images");
			}
			
			for (int i = 0; i < ((List<?>) images).size (); i++) {
				PostImage postImage = new PostImage (S3Utilities.uploadImage ((String) ((List<?>) images).get (i)), post);
				
				postImageService.createPostImage (postImage);
				
				postImages.add (postImage);
			}
			
			post.setImages (postImages);
			
			//todo refactor
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
	public ResponseEntity <JsonResponse> getUserPosts (@RequestParam Integer userId, @RequestParam Integer page, HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		List <Post> posts = postService.getUserPosts (userId, page);
		
		return ResponseEntity.ok (new JsonResponse ("Found " + posts.size () + " posts", true, formatPosts (posts)));
	}
	
	@GetMapping ("{id}")
	public ResponseEntity <JsonResponse> getPost (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}

		return ResponseEntity.ok (new JsonResponse ("Found post", true, postService.getPost (id)));
	}
}
