package com.campus.events.controller;

import com.campus.events.model.*;
import com.campus.events.repository.CampusEventRepository;
import com.campus.events.repository.UserRepository;
import com.campus.events.repository.TicketTierRepository;
import com.campus.events.repository.TicketOrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/campus-events")
public class CampusEventController {
  public record EventRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String venue,
    String posterUrl,
    Instant startAt,
    Instant endAt,
    UUID organizerId,
    EventStatus status
  ) {}

  private final CampusEventRepository events;
  private final UserRepository users;
  private final TicketTierRepository tiers;
  private final TicketOrderRepository orders;

  public CampusEventController(CampusEventRepository events, UserRepository users, TicketTierRepository tiers, TicketOrderRepository orders) {
    this.events = events;
    this.users = users;
    this.tiers = tiers;
    this.orders = orders;
  }

  @GetMapping("/published")
  public java.util.List<CampusEvent> published() {
    return events.findByStatusOrderByStartAtAsc(EventStatus.PUBLISHED);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ATTENDEE','EVENT_ORGANIZER','SUPER_ADMIN')")
  public java.util.List<CampusEvent> findAll() {
    return events.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ATTENDEE','EVENT_ORGANIZER','SUPER_ADMIN')")
  public CampusEvent findOne(@PathVariable UUID id) {
    return events.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public CampusEvent create(@Valid @RequestBody EventRequest request) {
    User organizer = users.findById(request.organizerId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organizer not found"));
    CampusEvent event = new CampusEvent();
    event.setTitle(request.title());
    event.setDescription(request.description());
    event.setVenue(request.venue());
    event.setPosterUrl(request.posterUrl());
    event.setStartAt(request.startAt());
    event.setEndAt(request.endAt());
    event.setOrganizer(organizer);
    event.setStatus(request.status() == null ? EventStatus.PENDING_APPROVAL : request.status());
    return events.save(event);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public CampusEvent update(@PathVariable UUID id, @RequestBody EventRequest request) {
    CampusEvent event = events.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    if (request.title() != null) event.setTitle(request.title());
    if (request.description() != null) event.setDescription(request.description());
    if (request.venue() != null) event.setVenue(request.venue());
    if (request.posterUrl() != null) event.setPosterUrl(request.posterUrl());
    if (request.startAt() != null) event.setStartAt(request.startAt());
    if (request.endAt() != null) event.setEndAt(request.endAt());
    if (request.status() != null) event.setStatus(request.status());
    event.setUpdatedAt(Instant.now());
    return events.save(event);
  }

  @PostMapping("/{id}/approve")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public CampusEvent approve(@PathVariable UUID id) {
    CampusEvent event = events.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    event.setStatus(EventStatus.PUBLISHED);
    return events.save(event);
  }

  @PostMapping("/{id}/reject")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public CampusEvent reject(@PathVariable UUID id) {
    CampusEvent event = events.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    event.setStatus(EventStatus.REJECTED);
    return events.save(event);
  }

  @GetMapping("/reports/sales")
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public java.util.List<java.util.Map<String, Object>> salesReport() {
    return events.findAll().stream().map(event -> {
      long revenue = tiers.findAll().stream()
        .filter(tier -> tier.getEvent().getId().equals(event.getId()))
        .mapToLong(tier -> orders.findAll().stream()
          .filter(order -> order.getTier().getId().equals(tier.getId()))
          .filter(order -> order.getStatus() == TicketStatus.PAID || order.getStatus() == TicketStatus.USED)
          .mapToLong(order -> (long) order.getQuantity() * tier.getPriceInCentavos())
          .sum())
        .sum();
      return java.util.Map.<String, Object>of(
        "eventId", event.getId(),
        "title", event.getTitle(),
        "status", event.getStatus(),
        "revenueInCentavos", revenue
      );
    }).toList();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public void delete(@PathVariable UUID id) {
    events.deleteById(id);
  }
}
