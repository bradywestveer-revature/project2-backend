package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepo extends JpaRepository<PostImage, Integer> {
}
