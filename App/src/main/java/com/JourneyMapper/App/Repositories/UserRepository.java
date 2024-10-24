package com.JourneyMapper.App.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JourneyMapper.App.Models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	User findByEmail(String email);
	User findById(int id);
}
