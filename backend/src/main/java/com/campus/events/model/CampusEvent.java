package com.campus.events.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "campus_events")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CampusEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "text")
  private String description;

  @Column(nullable = false)
  private String venue;
  private String posterUrl;
  private Instant startAt;
  private Instant endAt;

  @Enumerated(EnumType.STRING)
  private EventStatus status = EventStatus.PENDING_APPROVAL;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id", nullable = false)
  private User organizer;

  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getVenue() { return venue; }
  public void setVenue(String venue) { this.venue = venue; }
  public String getPosterUrl() { return posterUrl; }
  public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
  public Instant getStartAt() { return startAt; }
  public void setStartAt(Instant startAt) { this.startAt = startAt; }
  public Instant getEndAt() { return endAt; }
  public void setEndAt(Instant endAt) { this.endAt = endAt; }
  public EventStatus getStatus() { return status; }
  public void setStatus(EventStatus status) { this.status = status; }
  public User getOrganizer() { return organizer; }
  public void setOrganizer(User organizer) { this.organizer = organizer; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
