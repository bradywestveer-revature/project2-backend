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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * The CommentController is responsible for mapping all endpoints necessary for passing data, pertaining to comments, from
 * the client to the server.
 */
@RestController
@RequestMapping ("comment")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class CommentController {

	/**
	 * An instance of CommentService
	 */
	private final CommentService commentService;

	/**
	 * An instance of PostService
	 */
	private final PostService postService;

	/**
	 * This constructor is annotated with Autowired so that it's dependencies can be managed by Spring.
	 *
	 * @param commentService An instance of CommentService to access methods
	 * @param postService An instance of PostService to access methods
	 */
	@Autowired
	public CommentController (CommentService commentService, PostService postService) {
		this.commentService = commentService;
		this.postService = postService;
	}

	/**
	 * Validates comment to ensure a null body or empty String isn't passed.
	 *
	 * @param comment The Comment that needs validation
	 * @throws InvalidValueException Thrown when the body is null or empty.
	 */
	private void validateComment (Comment comment) throws InvalidValueException {
		if (comment.getBody () == null) {
			throw new InvalidValueException ("Invalid comment");
		}
		
		if (comment.getBody ().trim ().equals ("")) {
			throw new InvalidValueException ("Invalid comment");
		}
	}

	/**
	 * Creates, validates, and assigns a comment to a user and post.
	 *
	 * @param body The comment submitted by the user
	 * @param httpSession The Session created by the user
	 * @return A Json Response stating the comment was created
	 * @throws InvalidValueException Thrown when post isn't found
	 * @throws UnauthorizedException Thrown when there is no session
	 * @throws NotFoundException
	 */
	@PostMapping
	public ResponseEntity <JsonResponse> createComment (@RequestBody CreateCommentBody body, HttpSession httpSession) throws InvalidValueException, UnauthorizedException, NotFoundException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		if (body.getPostId () == null) {
			throw new InvalidValueException ("Invalid post id");
		}
		
		Post post = this.postService.getPost (body.getPostId ());
		
		Comment comment = new Comment (user, post, body.getBody (), new Date (System.currentTimeMillis ()));
		
		validateComment (comment);
		
		this.commentService.createComment (comment);
		
		post.getComments ().add (comment);
		
		this.postService.updatePost (post);
		
		return ResponseEntity.ok (new JsonResponse ("Created comment", true));
	}
}
