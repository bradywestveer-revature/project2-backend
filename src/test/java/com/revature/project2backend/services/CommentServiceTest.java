package com.revature.project2backend.services;

import com.revature.project2backend.models.Comment;
import com.revature.project2backend.repositories.CommentRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommentServiceTest {
	private final CommentService commentService;
	
	private final CommentRepo commentRepo = Mockito.mock (CommentRepo.class);
	
	public CommentServiceTest () {
		this.commentService = new CommentService (this.commentRepo);
	}
	
	@Test
	void createComment () {
		Comment comment = new Comment ();
		
		commentService.createComment (comment);
		
		Mockito.verify (commentRepo).save (comment);
	}
}