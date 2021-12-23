package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
}
