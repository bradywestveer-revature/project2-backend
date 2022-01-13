package com.revature.project2backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.project2backend.utilities.S3Utilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The User class contains all fields and constructors necessary for creating and testing a User Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor. All custom constructors are for testing purposes.
 * The usage of Spring Data allows for table generation to be done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

	/**
	 * This constructor is used for testing
	 *
	 * @param firstName A String
	 * @param lastName A String
	 * @param email A unique String that must be different from all other Users
	 * @param username A unique String that must be different from all other Users
	 * @param password A String
	 */
	public User (String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	/**
	 * This constructor is used for testing
	 *
	 * @param id A unique Integer that must be different from all other Users
	 * @param firstName A String
	 * @param lastName A String
	 * @param email A unique String that must be different from all other Users
	 * @param username A unique String that must be different from all other Users
	 * @param password A String
	 */
	public User(Integer id, String firstName, String lastName, String email, String username, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	/**
	 * This field sets the id of a User to a unique value that is incremented when a new User Object is added to the database.
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * This field sets the first name of the User Object. Cannot be empty.
	 */
	@Column (nullable = false)
	private String firstName;

	/**
	 * This field sets the last name of the User Object. Cannot be empty.
	 */
	@Column (nullable = false)
	private String lastName;

	/**
	 * This field sets the email of the User Object. Cannot be empty and must be unique.
	 */
	@Column (unique = true, nullable = false)
	private String email;

	/**
	 * This field sets the username of the User Object. Cannot be empty and must be unique.
	 */
	@Column (unique = true, nullable = false)
	private String username;

	/**
	 * This field sets the passowrd of the User Object. Cannot be empty.
	 */
	@Column (nullable = false)
	@JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * This field sets the profile image of the User Object.
	 * This field is set to a default photo but can be changed through other methods.
	 */
	private String profileImageUrl = S3Utilities.url + "profile.jpg";

	/**
	 * This field associates a User with a PasswordReset Object and is typically bound by a token of type String.
	 */
	@JsonIgnoreProperties({"user"})
	@OneToOne(mappedBy = "user")
	private PasswordReset passwordReset;

}
