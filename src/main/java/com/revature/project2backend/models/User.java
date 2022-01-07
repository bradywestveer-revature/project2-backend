package com.revature.project2backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.project2backend.utilities.S3Utilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (nullable = false)
	private String firstName;
	
	@Column (nullable = false)
	private String lastName;
	
	@Column (unique = true, nullable = false)
	private String email;
	
	@Column (unique = true, nullable = false)
	private String username;
	
	@Column (nullable = false)
	@JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private String profileImageUrl = S3Utilities.url + "profile.jpg";

	@JsonIgnoreProperties({"user"})
	@OneToOne(mappedBy = "user")
	private PasswordReset passwordReset;

	public User (String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public User(Integer id, String firstName, String lastName, String email, String username, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
	}
}
