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

import com.JourneyMapper.App.Models.Checkpoints;
import com.JourneyMapper.App.Models.Maps;
import com.JourneyMapper.App.Models.User;
import com.JourneyMapper.App.Repositories.CheckpointsRepository;
import com.JourneyMapper.App.Repositories.MapsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
public class CheckpointsController {

	@Autowired
	private CheckpointsRepository checkpointRepository;

	@Autowired
	MapsRepository mapRepo;

	@PostMapping("createcheckpoint")
	public ResponseEntity<?> createCheckpoint(HttpServletRequest request, @RequestBody Checkpoints checkpoint,
			@RequestParam int mapId) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = (User) session.getAttribute("loggedUser");
			Maps map = mapRepo.findByMapId(mapId);

			if (map == null) {
				return new ResponseEntity<>("User has no map", HttpStatus.NO_CONTENT);
			}

			// Calculate the next order number
			int nextOrder = checkpointRepository.countByMap(map) + 1;

			Checkpoints newCheckpoint = new Checkpoints();
			newCheckpoint.setMap(map);
			newCheckpoint.setTitle(checkpoint.getTitle());
			newCheckpoint.setCreatedAt(LocalDateTime.now());
			newCheckpoint.setUpdatedAt(LocalDateTime.now());
			newCheckpoint.setCheckpointOrder(nextOrder); // Set the incremented order value

			if (checkpoint.getReferenceLink() != null) {
				newCheckpoint.setReferenceLink(checkpoint.getReferenceLink());
			}
			if (checkpoint.getVideoLink() != null) {
				newCheckpoint.setVideoLink(checkpoint.getVideoLink());
			}

			checkpointRepository.save(newCheckpoint);

			return new ResponseEntity<>("Checkpoint created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while creating the checkpoint",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getcheckpoints")
	public ResponseEntity<?> getAllCheckpoints(HttpServletRequest request, @RequestParam int mapId) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}
		try {
			User user = (User) session.getAttribute("loggedUser");

			List<Checkpoints> allCheckpoints = checkpointRepository.findByMap_MapId(mapId);

			if (allCheckpoints.isEmpty()) {
				return new ResponseEntity<>("No Checkpoints found for this user", HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(allCheckpoints, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while creating the checkpoint",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tobeupdatecheckpoint")
	@Transactional
	public ResponseEntity<?> getCheckpointInfoForUpdate(HttpServletRequest request, @RequestParam int checkpointId,
			@RequestParam int mapId) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			User user = (User) session.getAttribute("loggedUser");

			Checkpoints checkpointToBeUpdated = checkpointRepository.findByMap_MapIdAndCheckpointId(mapId,
					checkpointId);

			if (checkpointToBeUpdated == null) {
				return new ResponseEntity<>("No Checkpoints found for this user", HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(checkpointToBeUpdated, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while fetching data to update for the checkpoint",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("updatecheckpoint")
	@Transactional
	public ResponseEntity<?> updateCheckpoint(HttpServletRequest request, @RequestParam int checkpointId,
			@RequestParam int mapId, @RequestBody Checkpoints checkpointData) {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			User user = (User) session.getAttribute("loggedUser");
			Maps map = mapRepo.findByMapId(mapId);

			// Find the checkpoint to be updated
			Checkpoints checkpointToBeUpdated = checkpointRepository.findByMap_MapIdAndCheckpointId(mapId,
					checkpointId);

			if (checkpointToBeUpdated == null) {
				return new ResponseEntity<>("No Checkpoints found for this user", HttpStatus.NOT_FOUND);
			}

			// Update fields only if they are not null in checkpointData
			if (checkpointData.getTitle() != null) {
				checkpointToBeUpdated.setTitle(checkpointData.getTitle());
			}
			if (checkpointData.getVideoLink() != null) {
				checkpointToBeUpdated.setVideoLink(checkpointData.getVideoLink());
			}
			if (checkpointData.getReferenceLink() != null) {
				checkpointToBeUpdated.setReferenceLink(checkpointData.getReferenceLink());
			}

			// Always update the map and checkpoint order
			checkpointToBeUpdated.setMap(map);
			checkpointToBeUpdated.setCheckpointOrder(checkpointData.getCheckpointOrder());

			// Set updatedAt to the current time
			checkpointToBeUpdated.setUpdatedAt(LocalDateTime.now());

			// Save the updated checkpoint
			checkpointRepository.save(checkpointToBeUpdated);

			return new ResponseEntity<>("Checkpoint updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating the checkpoint",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/todeletecheckpoint")
	@Transactional
	public ResponseEntity<?> deleteCheckpoint(HttpServletRequest request, @RequestParam int checkpointId) {
		HttpSession session = request.getSession(false);

		// Check if the session exists and if the user is logged in
		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			// Check if the checkpoint exists before deletion
			Checkpoints checkpointToBeDeleted = checkpointRepository.findById(checkpointId).orElse(null);
			if (checkpointToBeDeleted == null) {
				return new ResponseEntity<>("Checkpoint not found", HttpStatus.NOT_FOUND);
			}

			// Perform deletion
			checkpointRepository.deleteByCheckpointId(checkpointId);
			return new ResponseEntity<>("Checkpoint deleted successfully", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while deleting the checkpoint",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deletecheckpoints")
	@Transactional
	public ResponseEntity<?> deleteMapsCheckpoints(HttpServletRequest request, @RequestParam int mapId) {
		HttpSession session = request.getSession(false);

		// Check if the session exists and if the user is logged in
		if (session == null || session.getAttribute("loggedUser") == null) {
			return new ResponseEntity<>("User is not logged in", HttpStatus.UNAUTHORIZED);
		}

		try {
			// Retrieve User object from session
//			User user = (User) session.getAttribute("loggedUser");

			// Delete all maps created by the logged-in user
			checkpointRepository.deleteByMap_MapId(mapId);

			// Return a confirmation response
			return new ResponseEntity<>("All checkpoints created by the user have been deleted successfully",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while deleting the checkpoints",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
