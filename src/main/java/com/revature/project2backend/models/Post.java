package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Post {
	public Post (User creator, String body, Date created) {
		this.creator = creator;
		this.body = body;
		this.created = created;
	}
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne (optional = false)
	private User creator;
	
	@Column (nullable = false)
	private String body;
	
	@OneToMany
	private List <PostImage> images;
	
	@OneToMany
	private List <PostLike> likes;
	
	@OneToMany
	private List <Comment> comments;
	
	@Column (nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
}
