package com.revature.project2backend.services;

import com.revature.project2backend.models.Comment;
import com.revature.project2backend.repositories.CommentRepo;
import org.springframework.stereotype.Service;

/**
 * This class contains all methods for creating a comment
 */
@Service
public class CommentService {

	/**
	 * An instance of the CommentRepository
	 */
	private final CommentRepo commentRepo;

	/**
	 * The constructor for CommentService.
	 *
	 * @param commentRepo An instance of the CommentRepository
	 */
	public CommentService (CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
	}

	/**
	 * The argument is a String that is saved to the database as a comment.
	 *
	 * @param comment A String
	 */
	public void createComment (Comment comment) {
		this.commentRepo.save (comment);
	}
}
