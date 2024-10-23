package com.JourneyMapper.App.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	// BCryptPasswordEncoder instance
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// Method to hash the password
	public String hashPassword(String upComingPassword) {
		try {
			// Hash the password
			return passwordEncoder.encode(upComingPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null if hashing fails
		}
	}

	public boolean checkPassword(String plainPassword,String hashedPassword) {

		try {
			return passwordEncoder.matches(plainPassword,hashedPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
