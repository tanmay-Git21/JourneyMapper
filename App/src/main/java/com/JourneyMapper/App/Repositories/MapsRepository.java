package com.JourneyMapper.App.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JourneyMapper.App.Models.Maps;

public interface MapsRepository extends JpaRepository<Maps, Integer> {

	
	 List<Maps> findByUser_UserId(int userId);
	 Maps findByMapId(int mapId);
	 Maps deleteByMapId(int mapId);
	 List<Maps> deleteByUser_UserId(int userId);
}

