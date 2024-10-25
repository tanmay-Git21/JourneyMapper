package com.JourneyMapper.App.Models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "maps_table")
public class Maps {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mapId;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@Column(nullable = false)
	private String mapTitle;

	@Column(nullable = false)
	private String mapDescription;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	
	@OneToMany(mappedBy = "map")
	 private Set<Checkpoints> checkpoints = new HashSet<>();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Maps() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Maps(int mapId, String mapTitle, String mapDescription, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.mapId = mapId;
		this.mapTitle = mapTitle;
		this.mapDescription = mapDescription;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getMapTitle() {
		return mapTitle;
	}

	public void setMapTitle(String mapTitle) {
		this.mapTitle = mapTitle;
	}

	public String getMapDescription() {
		return mapDescription;
	}

	public void setMapDescription(String mapDescription) {
		this.mapDescription = mapDescription;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(nullable = false, updatable = true)
	private LocalDateTime updatedAt;

}
