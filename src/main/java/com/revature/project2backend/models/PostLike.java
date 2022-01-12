package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The PostLike class contains all fields and constructors necessary for creating a PostLike Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor.
 * The usage of Spring Data allows for table generation to done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PostLike {

	/**
	 * This constructor is used to easily generate a PostLike.
	 * The arguments are used to assign the like to a specific Post Object and User Object.
	 *
	 * @param post A Post Object
	 * @param creator A User Object
	 */
	public PostLike (Post post, User creator) {
		this.post = post;
		this.creator = creator;
	}

	/**
	 * This field sets the id of a PostLike to a unique value that is incremented when a new PostLike Object is added to the database.
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * This is used to assign a PostLike with a specific User Object
	 */
	@ManyToOne (optional = false)
	private User creator;

	/**
	 * This is used to assign a PostLike with a specific Post Object
	 */
	@ManyToOne (optional = false)
	private Post post;
}
