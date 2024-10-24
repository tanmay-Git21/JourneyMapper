package com.JourneyMapper.App.Models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


//This class is and entity which helps to register user to database 
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;  // Changed to camelCase

    @Column(nullable = false)
    private String firstName;  // Changed to camelCase

    @Column(nullable = false)
    private String lastName;   // Changed to camelCase

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;    // Consider using hashed passwords

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // Changed to camelCase

    @Column(updatable = true, nullable = false)
    private LocalDateTime updatedAt; // Changed to camelCase

    @OneToMany
    private Set<Maps> maps = new HashSet<>();

    // No-arg constructor
    public User() {
        super();
        this.createdAt = LocalDateTime.now(); // Initialize createdAt
        this.updatedAt = LocalDateTime.now(); // Initialize updatedAt
    }

    // Parameterized constructor
    public User(String firstName, String lastName, String email, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // Consider hashing the password
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.updatedAt = LocalDateTime.now(); 
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.updatedAt = LocalDateTime.now(); 
    }

    public String getEmail() {
        return email;
    }
// This method needs to do email validation like 1 . having only lower cases  2. having @ and .com  3. avoid duplication
    public void setEmail(String email) {
    	
        this.email = email;
        this.updatedAt = LocalDateTime.now(); 
    }

    public String getPassword() {
        return password; // Return hashed password
    }
// This method need to password validation like 1 . having at least on number , 2 having password Strength greater that 8 , 3 having special character
    public void setPassword(String password) {
        this.password = password; // Hash password before setting
        this.updatedAt = LocalDateTime.now(); 
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // No need for a setter for updatedAt as it will be updated in other setters like when user changes firstName, lastName, email, password.
}
