package com.revature.project2backend.repositories;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PostRepoIT {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    private final User creator = new User("David", "Helfer", "yes@gmail.com", "username", "password");
    private final User diffy = new User("David", "Helfer", "no@gmail.com", "name", "password");



    @BeforeEach
    void setUp() {

        userRepo.save(creator);
        userRepo.save(diffy);

        for(int i = 0; i <10; i++){
            postRepo.save(new Post (diffy, Integer.toString(i), new Date(System.currentTimeMillis())));
        }
        for(int i = 0; i <21; i++){
            postRepo.save(new Post (creator, Integer.toString(i), new Date(System.currentTimeMillis())));
        }

    }

    @AfterEach
    void tearDown() {
        postRepo.deleteAll();
    }

    @Test
    void findByCreator() {
        Pageable pageable = PageRequest.of (0, 20, Sort.by ("created").descending ());

        List<Post> posts = this.postRepo.findByCreator (creator, pageable).getContent();

        assertEquals(20, posts.size());

        for (Post post : posts){
            assertEquals(creator.getId(), post.getCreator().getId());
        }
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of (0, 20, Sort.by ("created").descending ());

        List<Post> posts = this.postRepo.findAll(pageable).getContent();

        assertEquals(20, posts.size());

    }
}