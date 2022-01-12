package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * The PostImage class contains all fields and constructors necessary for creating a PostImage Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor.
 * The usage of Spring Data allows for table generation to done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PostImage {

	/**
	 * This constructor is used to easily generate a PostImage.
	 * The arguments assign the image to a specific Post and hold a path leading to the image.
	 *
	 * @param path A String specifying the location of the image
	 * @param post A Post Object
	 */
	public PostImage (String path, Post post) {
		this.path = path;
		this.post = post;
	}

	/**
	 * This field sets the id of a PostImage to a unique value that is incremented when a new PostImage Object is added to the database.
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * This field sets the location of the image
	 */
	@Column (nullable = false)
	private String path;

	/**
	 * This is used to assign a PostImage with a specific Post Object
	 */
	@ManyToOne (optional = false)
	private Post post;
}
