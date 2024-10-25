package com.JourneyMapper.App.Models;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "checkpoints_table")
public class Checkpoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkpointId;

    @ManyToOne
    @JoinColumn(name = "mapId", nullable = false)
    private Maps map;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true, length = 255) // Optional field
    private String videoLink;

    @Column(nullable = true, length = 255) // Optional field
    private String referenceLink;

    @Column(nullable = false)
    private int checkpointOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Default constructor
    public Checkpoints() {
        super();
    }

    // Parameterized constructor
    public Checkpoints(int checkpointId, Maps map, String title, String videoLink, String referenceLink, int order, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.checkpointId = checkpointId;
        this.map = map;
        this.title = title;
        this.videoLink = videoLink;
        this.referenceLink = referenceLink;
        this.checkpointOrder = order;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(int checkpointId) {
        this.checkpointId = checkpointId;
    }

    public Maps getMap() {
        return map;
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public int getCheckpointOrder() {
        return checkpointOrder;
    }

    public void setCheckpointOrder(int checkpointOrder) {
        this.checkpointOrder = checkpointOrder;
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
}
