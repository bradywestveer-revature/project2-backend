package com.revature.project2backend.repositories;

import com.revature.project2backend.models.Post;
import com.revature.project2backend.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository <Post, Integer> {

	/**
	 * Returns a Slice Object that contains a certain page of a specific number of Post Objects ordered in particular way
	 * and all set by the pageable argument.
	 *
	 * @param creator A User Object
	 * @param pageable A Pageable Object set to page number, number of Posts returned, and organization of Posts.
	 * @return A Slice Object with Post data
	 */
	Slice <Post> findByCreator (User creator, Pageable pageable);
}
