package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Comment {
	public Comment (User creator, Post post, String body, Date created) {
		this.creator = creator;
		this.post = post;
		this.body = body;
		this.created = created;
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne (optional = false)
	private User creator;
	
	@ManyToOne (optional = false)
	private Post post;
	
	@Column (nullable = false)
	private String body;
	
	@Column (nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
}
