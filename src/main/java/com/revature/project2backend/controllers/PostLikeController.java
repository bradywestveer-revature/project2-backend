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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * The PostLikeController is responsible for mapping all endpoints necessary for passing data, pertaining to PostLikes, from
 * the client to the server.
 */
@RestController
@RequestMapping ("postlike")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class PostLikeController {

	/**
	 * An instance of PostLikeService used for accessing the methods in the class.
	 */
	private final PostLikeService postLikeService;

	/**
	 * An instance of PostService used for accessing the methods in the class.
	 */
	private final PostService postService;

	/**
	 * This Constructor initializes PostService and PostLikeService.
	 *
	 * @param postLikeService An instance of PostService used for accessing the methods in the class.
	 * @param postService An instance of PostLikeService used for accessing the methods in the class.
	 */
	@Autowired
	public PostLikeController (PostLikeService postLikeService, PostService postService) {
		this.postLikeService = postLikeService;
		this.postService = postService;
	}

	/**
	 * Creates a like adn associates with a specific Post and User Object
	 *
	 * @param body A DTO containing the posts id
	 * @param httpSession A session started by the user
	 * @return A JsonResponse notifying the user the like was created
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws InvalidValueException Thrown when the post has an invalid id
	 * @throws NotFoundException Thrown when the post or user isn't found
	 */
	@PostMapping
	public ResponseEntity <JsonResponse> createPostLike (@RequestBody CreateLikeBody body, HttpSession httpSession) throws UnauthorizedException, InvalidValueException, NotFoundException {
		User user = (User) httpSession.getAttribute ("user"); 
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		if (body.getPostId () == null) {
			throw new InvalidValueException ("Invalid post id");
		}
		
		Post post = postService.getPost (body.getPostId ());
		
		//check if user has already liked this post
		for (int i = 0; i < post.getLikes ().size (); i++) {
			if (post.getLikes ().get (i).getCreator ().getId ().equals (user.getId ())) {
				throw new InvalidValueException ("Post is already liked by user with id: " + user.getId ());
			}
		}
		
		PostLike postLike = new PostLike (post, user);
		
		this.postLikeService.createPostLike (postLike);
		
		post.getLikes ().add (postLike);
		
		postService.updatePost (post);
		
		return ResponseEntity.ok (new JsonResponse ("Created like", true));
	}

	/**
	 * Uses a path parameter to find one specific post and remove the users like from that post
	 *
	 * @param postId The path parameter containing the id of a post
	 * @param httpSession A session started by the user
	 * @return A JsonResponse notifying the user the like was deleted
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws InvalidValueException Thrown when the post has an invalid id
	 * @throws NotFoundException Thrown when the post or user isn't found
	 */
	@DeleteMapping ("/{postId}")
	public ResponseEntity <JsonResponse> deletePostLike (@PathVariable Integer postId, HttpSession httpSession) throws UnauthorizedException, InvalidValueException, NotFoundException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		Post post = this.postService.getPost (postId);
		
		for (int i = 0; i < post.getLikes ().size (); i++) {
			if (post.getLikes ().get (i).getCreator ().getId ().equals (user.getId ())) {
				PostLike postLike = post.getLikes ().get (i);
				
				post.getLikes ().remove (postLike);
				
				this.postService.updatePost (post);
				
				this.postLikeService.deletePostLike (postLike);
				
				return ResponseEntity.ok (new JsonResponse ("Deleted like", true));
			}
		}
		
		//user hasn't liked this post
		throw new InvalidValueException ("Post is not liked by user with id: " + user.getId ());
	}
}
