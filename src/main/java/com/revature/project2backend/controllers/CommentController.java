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

@RestController
@RequestMapping ("comment")
public class CommentController {
	private final CommentService commentService;
	private final PostService postService;
	
	@Autowired
	public CommentController (CommentService commentService, PostService postService) {
		this.commentService = commentService;
		this.postService = postService;
	}
	
	private void validateComment (Comment comment) throws InvalidValueException {
		if (comment.getBody () == null) {
			throw new InvalidValueException ("Invalid comment");
		}
		
		if (comment.getBody ().trim ().equals ("")) {
			throw new InvalidValueException ("Invalid comment");
		}
	}
	
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
