package com.JourneyMapper.App.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.JourneyMapper.App.Models.LoginUser;
import com.JourneyMapper.App.Models.User;
import com.JourneyMapper.App.Repositories.UserRepository;
import com.JourneyMapper.App.Services.PasswordService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// This class has two methods 
// registerUser - which registers the user 
// loginUser - which helps to login user as per credentials

@RestController
public class UserController {

	@Autowired
	private UserRepository dataMan; // to transport data

	@Autowired
	private PasswordService passwordService;// to hash a password

	// This methods uses request body to get data and make into user object and
	// hashes password and then saves it
	// This is the input it expects
//	{
//		  "firstName": "John",
//		  "lastName": "Doc",
//		  "email": "john.doe@example.com",
//		  "password": "yourPassword1233323"
//		}
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Validated User newUser, BindingResult result) {

		// Check if there are validation errors
		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
		}

		try {
			// Hash the password before saving the user
			String passwordToBeHashed = newUser.getPassword();
			String finalHashedPassword = passwordService.hashPassword(passwordToBeHashed);

			if (finalHashedPassword != null) {
				newUser.setPassword(finalHashedPassword);
			} else {
				return new ResponseEntity<>("Password hashing failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Save the new user
			User savedUser = dataMan.save(newUser);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

		} catch (DataIntegrityViolationException e) {
			// Handle unique constraint violations (e.g., duplicate email)
			return new ResponseEntity<>("Email is already in use", HttpStatus.CONFLICT);

		} catch (Exception e) {
			// Log the error for debugging purposes and return a generic error message
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while registering the user",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// This method first takes the email finds the user in table user_table and
	// gives the hashed password of found user to checkPassword service ,
	// also the entered password by user is also given to checkPassoword if the
	// password is matched the user gets logged in or the error is shown
	// This is the input that it expects
//	{
//		  "login_email": "tanmay@gmail.com",
//		  "login_password": "123456"
//		}
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody @Validated LoginUser loginForm, BindingResult result, HttpServletRequest request) {
	    // Check if there are validation errors
	    if (result.hasErrors()) {
	        return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
	    }

	    try {
	        // Trim and lower case the email for consistency

	        String emailToCheck = loginForm.getLogin_email().trim().toLowerCase();
	        User loggedUserEmail = dataMan.findByEmail(emailToCheck);

	        // Debugging information
	        System.out.println("Trying to log in with email: " + emailToCheck);
	        System.out.println("Found user: " + loggedUserEmail);

	        // Check if user is found
	        if (loggedUserEmail == null) {
	            return new ResponseEntity<>("No such user found", HttpStatus.NOT_FOUND);
	        }

	        String hashedUserPassword = loggedUserEmail.getPassword();

	        // Check if the passwords match
	        boolean passCheck = passwordService.checkPassword(loginForm.getLogin_password(), hashedUserPassword);

	        if (passCheck) {
	            // Create a session
	            HttpSession session = request.getSession(true); // true means to create a new session if it doesn't exist
	            
	            // Store the User object in the session
	            session.setAttribute("loggedUser", loggedUserEmail);
	            session.setAttribute("isLoggedIn", true);

	            return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Wrong email or password", HttpStatus.UNAUTHORIZED);
	        }
	    } catch (Exception e) {
	        // Log the exception
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
