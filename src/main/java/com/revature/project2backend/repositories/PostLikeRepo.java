package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepo extends JpaRepository<PostLike, Integer> {}
