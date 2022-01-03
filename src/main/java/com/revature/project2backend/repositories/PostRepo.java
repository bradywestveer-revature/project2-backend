package com.revature.project2backend.repositories;

import com.revature.project2backend.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    //List<Post> findByCreator(Integer id);

    Slice<Post> findByCreator(Integer creatorId, Pageable pageable);

}
