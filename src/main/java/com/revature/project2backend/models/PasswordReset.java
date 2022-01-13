package com.revature.project2backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The PasswordReset class contains all fields and constructors necessary for creating and testing a PasswordReset Object.
 * The class utilizes Spring Data to create an All-Args and No-Args constructor.
 * The usage of Spring Data allows for table generation to be done directly from the fields in this class, so they will be described
 * with that in mind.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PasswordReset {

    /**
     * This field sets the id of a PasswordReset to a unique value that is incremented when a new PasswordReset Object is added to the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * A User Object that has requested a password change.
     */
    @JsonIgnoreProperties({"passwordReset"})
    @OneToOne
    @MapsId
    private User user;

    /**
     * A unique String that allows a user to reset their password via email.
     */
    @Column(nullable = false, unique = true)
    String token;
}
