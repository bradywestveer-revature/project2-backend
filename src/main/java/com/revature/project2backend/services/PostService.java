package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The PostService class contains all methods necessary for creating and retrieving Post Objects.
 */
@Service
public class PostService {

	/**
	 * An instance of the PostRepository.
	 */
	private final PostRepo postRepo;

	/**
	 * A variable used to set the maximum number of Posts that will be retrieved in any given call.
	 */
	private final int postsPerPage = 20;

	/**
	 * The constructor for PostService that uses dependency injection through Spring.
	 *
	 * @param postRepo An instance of the PostRepository.
	 */
	@Autowired
	public PostService (PostRepo postRepo) {
		this.postRepo = postRepo;
	}

	/**
	 * Creates and saves a Post Object to the database.
	 * @param post A Post Object
	 */
	public void createPost (Post post) {
		this.postRepo.save (post);
	}

	/**
	 * Returns a List of 20 Post objects, sorted by most recently submitted.
	 * The page argument can be any integer and will return an empty Array if the page has no posts.
	 *
	 * @param page The number of the page
	 * @return A List of 20 Post objects
	 */
	public List <Post> getPosts (Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		//todo do we need to check for null? When i was mocking without passing in pageable I was getting a NullPointerException, so findAll could return null somehow?
		return this.postRepo.findAll (pageable).getContent ();
	}

	/**
	 * Returns a List of 20 Post objects for a specific User, sorted by most recently submitted.
	 * The page argument can be any integer and will return an empty Array if the page has no posts.
	 *
	 * @param user A User Object
	 * @param page The number of the page
	 * @return A List of 20 Post objects for a specific User
	 */
	public List <Post> getUserPosts (User user, Integer page) {
		Pageable pageable = PageRequest.of (page, postsPerPage, Sort.by ("created").descending ());
		
		//todo check if user is null? update test if so
		return this.postRepo.findByCreator (user, pageable).getContent ();
	}

	/**
	 * Returns a Post Object with the specified id.
	 *
	 * @param id The id of the specified Post
	 * @return A Post Object with the specific id
	 * @throws NotFoundException
	 */
	public Post getPost (Integer id) throws NotFoundException {
		Post post = postRepo.findById (id).orElse (null);
		
		if (post == null) {
			throw new NotFoundException ("Post with id: " + id + " not found");
		}
		
		return post;
	}

	/**
	 * This method is used to update the Post that is passed into its argument, typically to add comments.
	 *
	 * @param post A Post Object with new information
	 */
	public void updatePost (Post post) {
		postRepo.save (post);
	}
}
