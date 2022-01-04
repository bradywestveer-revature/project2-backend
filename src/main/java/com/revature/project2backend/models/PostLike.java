package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PostLike {
	public PostLike (Post post, User creator) {
		this.post = post;
		this.creator = creator;
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne (optional = false)
	private User creator;
	
	@ManyToOne (optional = false)
	private Post post;
}
