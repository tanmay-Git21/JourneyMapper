package com.JourneyMapper.App.Repositories;

import com.JourneyMapper.App.Models.Checkpoints;
import com.JourneyMapper.App.Models.Maps;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckpointsRepository extends JpaRepository<Checkpoints, Integer> {
    
    // Custom query method to count checkpoints for a specific map
	Checkpoints findByMap_MapIdAndCheckpointId(int mapId, int checkpointId);
	List<Checkpoints> findByMap_MapId(int mapId);
	Checkpoints findByCheckpointId(int checkpointId);
    int countByMap(Maps map);
    
    
    
    
 // Correct method for deleting a checkpoint by its ID
    void deleteByCheckpointId(int checkpointId); // Correct method name

    // Method to delete checkpoints by map ID
    void deleteByMap_MapId(int mapId); // This one is also correct
	
}
