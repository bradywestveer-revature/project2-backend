package com.revature.project2backend.repositories;

import com.revature.project2backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
}
