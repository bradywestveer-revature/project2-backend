package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreateLikeBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.PostLike;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PostLikeService;
import com.revature.project2backend.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping ("postlike")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class PostLikeController {
	private final PostLikeService postLikeService;
	private final PostService postService;
	
	@Autowired
	public PostLikeController (PostLikeService postLikeService, PostService postService) {
		this.postLikeService = postLikeService;
		this.postService = postService;
	}
	
	@PostMapping
	public ResponseEntity <JsonResponse> createPostLike (@RequestBody CreateLikeBody body, HttpSession httpSession) throws UnauthorizedException, InvalidValueException {
		User user = (User) httpSession.getAttribute ("user"); 
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		if (body.getPostId () == null) {
			throw new InvalidValueException ("Invalid post ID");
		}
		
		Post post = postService.getPost (body.getPostId ());
		
		if (post == null) {
			throw new InvalidValueException ("Invalid post ID");
		}
		
		//check if user has already liked this post
		for (int i = 0; i < post.getLikes ().size (); i++) {
			if (post.getLikes ().get (i).getCreator ().getId ().equals (user.getId ())) {
				throw new InvalidValueException ("Post has already been liked by user with ID: " + user.getId ());
			}
		}
		
		PostLike postLike = new PostLike (post, user);
		
		this.postLikeService.createPostLike (postLike);
		
		post.getLikes ().add (postLike);
		
		postService.updatePost (post);
		
		return ResponseEntity.ok (new JsonResponse ("Created like", true));
	}
	
	@DeleteMapping ("/{postId}")
	public ResponseEntity <JsonResponse> deletePostLike (@PathVariable Integer postId, HttpSession httpSession) throws UnauthorizedException, InvalidValueException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		Post post = this.postService.getPost (postId);
		
		if (post == null) {
			throw new InvalidValueException ("Invalid post ID");
		}
		
		for (int i = 0; i < post.getLikes ().size (); i++) {
			if (post.getLikes ().get (i).getCreator ().getId ().equals (user.getId ())) {
				PostLike postLike = post.getLikes ().get (i);
				
				post.getLikes ().remove (postLike);
				
				this.postService.updatePost (post);
				
				this.postLikeService.deletePostLike (postLike);
				
				return ResponseEntity.ok (new JsonResponse ("Deleted like", true));
			}
		}
		
		throw new InvalidValueException ("Invalid post ID");
	}
}
