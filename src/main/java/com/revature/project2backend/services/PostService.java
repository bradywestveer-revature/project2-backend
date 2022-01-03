package com.revature.project2backend.services;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepo postRepo;

    @Autowired
    public PostService(PostRepo postRepo){
        this.postRepo = postRepo;
    }

    public void createPost(Post post){
        this.postRepo.save(post);
    }

    public List<Post> getAllPosts(Integer pageNum){

        Pageable pageable = PageRequest.of(pageNum, 20, Sort.by("created").descending());
        Slice<Post> slice = this.postRepo.findAll(pageable);
        if(slice.hasContent()){
            return slice.getContent();
        } else {
            return null;
        }

    }

    public Slice<Post> getProfilePosts(Integer profileId){

        Pageable pageable = PageRequest.of(0, 20, Sort.by("created").descending());

        return this.postRepo.findByCreator(profileId, pageable);
    }


}
