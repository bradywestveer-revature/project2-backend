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
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne (optional = false)
	private User creator;
	
	@Column (nullable = false)
	private String body;
	
	@Column (nullable = false)
	@Temporal (TemporalType.TIMESTAMP)
	private Date created;
}
