package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * The Comment class contains all fields and constructors necessary for creating and testing a Comment Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor.
 * The usage of Spring Data allows for table generation to be done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Comment {

	/**
	 * This constructor is used to easily generate a Comment.
	 *
	 * @param creator A User Object
	 * @param post A Post Object
	 * @param body A String
	 * @param created A Date the object was created
	 */
	public Comment (User creator, Post post, String body, Date created) {
		this.creator = creator;
		this.post = post;
		this.body = body;
		this.created = created;
	}

	/**
	 * This field sets the id of a Comment to a unique value that is incremented when a new Comment Object is added to the database.
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * This field sets the Comment to a specific User Object.
	 */
	@ManyToOne (optional = false)
	private User creator;

	/**
	 * This field sets the Comment to a specific Post Object.
	 */
	@ManyToOne (optional = false)
	private Post post;

	/**
	 * This field sets the comments body.
	 */
	@Column (nullable = false)
	private String body;

	/**
	 * This field sets Date that the Comment Object was created
	 */
	@Column (nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
}
