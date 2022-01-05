package com.revature.project2backend.repositories;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository <Post, Integer> {
	Slice <Post> findByCreator (User creator, Pageable pageable);
}
