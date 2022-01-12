package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The Post class contains all fields and constructors necessary for creating and testing a Post Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor. All custom constructors are for testing purposes.
 * The usage of Spring Data allows for table generation to done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Post {

	/**
	 * This constructor is used ofr testing and to build Post Object that do not have an image.
	 *
	 * @param creator A User Object
	 * @param body A String
	 * @param created A Date Object
	 */
	public Post (User creator, String body, Date created) {
		this.creator = creator;
		this.body = body;
		this.created = created;
	}

	/**
	 * This field sets the id of a Post to a unique value that is incremented when a new Post Object is added to the database.
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * This field sets the Post to a specific User Object.
	 */
	@ManyToOne (optional = false)
	private User creator;

	/**
	 * This field sets posts body.
	 */
	@Column (nullable = false)
	private String body;

	/**
	 * This field associates
	 */
	@OneToMany
	private List <PostImage> images;

	/**
	 * This field sets the id of a User to a unique value that is incremented when a new User Object is added to the database.
	 */
	@OneToMany
	private List <PostLike> likes;

	/**
	 * This field sets the id of a User to a unique value that is incremented when a new User Object is added to the database.
	 */
	@OneToMany
	private List <Comment> comments;

	/**
	 * This field sets the id of a User to a unique value that is incremented when a new User Object is added to the database.
	 */
	@Column (nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
}
