package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PostImage {
	public PostImage (String path, Post post) {
		this.path = path;
		this.post = post;
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (nullable = false)
	private String path;
	
	@ManyToOne (optional = false)
	private Post post;
}
