package com.JourneyMapper.App.Controllers;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JourneyMapper.App.Models.Maps;
import com.JourneyMapper.App.Models.User;
import com.JourneyMapper.App.Repositories.MapsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
public class MapsController {

	@Autowired
	private MapsRepository mapRepository;

	// For this method this input is expected

//	{
//	    "mapTitle": "My First Map",
//	    "mapDescription": "This map covers the basics of Java programming."
//	}
	@PostMapping("/createmap")
	public ResponseEntity<?> createMap(HttpServletRequest request, @RequestBody Maps mapRequest) {
		// Retrieve the user from the session
		HttpSession session = request.getSession(false); // false to avoid creating a new session if one doesn't exist

		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			// Retrieve User object from session
			User user = (User) session.getAttribute("loggedUser");

			// Create a new Map entity and populate it
			Maps map = new Maps();
			map.setUser(user); // Set the user relationship
			map.setMapTitle(mapRequest.getMapTitle()); // Data from the request
			map.setMapDescription(mapRequest.getMapDescription()); // Data from the request
			map.setCreatedAt(LocalDateTime.now()); // Set the creation time
			map.setUpdatedAt(LocalDateTime.now()); // Set the update time

			// Save the new map to the database
			mapRepository.save(map);

			return new ResponseEntity<>("Map created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while creating the map", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getmaps")
	public ResponseEntity<?> getAllMaps(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		// Check if the session exists and if the user is logged in
		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			// Retrieve User object from session
			User user = (User) session.getAttribute("loggedUser");

			// Fetch maps created by the logged-in user
			List<Maps> loggedUserMaps = mapRepository.findByUser_UserId(user.getUserId());

			// Check if userMaps is empty
			if (loggedUserMaps.isEmpty()) {
				return new ResponseEntity<>("No maps found for this user", HttpStatus.NOT_FOUND);
			}

			// Return the list of maps in the response body
			return new ResponseEntity<>(loggedUserMaps, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while retrieving the maps",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// this expects /toupdatemap?mapId=5
	@GetMapping("/toupdatemap")
	@Transactional
	public ResponseEntity<?> toUpdateMap(HttpServletRequest request, @RequestParam int mapId) {
		HttpSession session = request.getSession(false);

		// Check if the session exists and if the user is logged in
		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {

			Maps maptoBeUpdated = mapRepository.findByMapId(mapId);
			if (maptoBeUpdated == null) {
				return new ResponseEntity<>("No map found for this user", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(maptoBeUpdated, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while retrieving the map data",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/updatemap")
	@Transactional
	public ResponseEntity<?> updateMap(@RequestBody Maps mapToBeUpdated, HttpServletRequest request,
			@RequestParam int mapId) {

		HttpSession session = request.getSession(false);

		// Check if the session exists and if the user is logged in
		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}
		try {
			// Retrieve User object from session
			User user = (User) session.getAttribute("loggedUser");
			Maps map = mapRepository.findByMapId(mapId);
			if (map == null) {
				return new ResponseEntity<>("No map found for this user", HttpStatus.NOT_FOUND);
			}

			map.setUser(user); // Set the user relationship
			map.setMapTitle(mapToBeUpdated.getMapTitle()); // Data from the request
			map.setMapDescription(mapToBeUpdated.getMapDescription()); // Data from the request
			map.setCreatedAt(mapToBeUpdated.getCreatedAt()); // Set the creation time
			map.setUpdatedAt(LocalDateTime.now()); // Set the update time

			// Save the new map to the database
			mapRepository.save(map);

			return new ResponseEntity<>("Map updated successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while creating the map", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/todeletemap")
	@Transactional
	public ResponseEntity<?> deleteMap(HttpServletRequest request, @RequestParam int mapId) {
	    HttpSession session = request.getSession(false);

	    // Check if the session exists and if the user is logged in
	    if (session == null || session.getAttribute("loggedUser") == null) {
	        return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
	    }

	    try {
	        // Check if the map exists before deletion
	        Maps mapToBeDeleted = mapRepository.findById(mapId).orElse(null);
	        if (mapToBeDeleted == null) {
	            return new ResponseEntity<>("Map not found", HttpStatus.NOT_FOUND);
	        }

	        // Perform deletion
	        mapRepository.deleteById(mapId);
	        return new ResponseEntity<>("Map deleted successfully", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while deleting the map", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@DeleteMapping("/deletemaps")
	@Transactional
	public ResponseEntity<?> deleteUserMaps(HttpServletRequest request, @RequestParam int mapId) {
	    HttpSession session = request.getSession(false);

	    // Check if the session exists and if the user is logged in
	    if (session == null || session.getAttribute("loggedUser") == null) {
	        return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
	    }

	    try {
	        // Retrieve User object from session
	        User user = (User) session.getAttribute("loggedUser");

	        // Delete all checkpoints associated with the specified map
	        mapRepository.deleteByUser_UserId(mapId);

	        // Return a confirmation response
	        return new ResponseEntity<>("All checkpoints associated with the map have been deleted successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while deleting the checkpoints", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



}
