package com.revature.project2backend.services;

import com.revature.project2backend.models.Comment;
import com.revature.project2backend.repositories.CommentRepo;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	private final CommentRepo commentRepo;
	
	public CommentService (CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
	}
	
	public void createComment (Comment comment) {
		this.commentRepo.save (comment);
	}
}
