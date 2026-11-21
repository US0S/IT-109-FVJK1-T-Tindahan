package com.campus.events.controller;

import com.campus.events.model.TicketTier;
import com.campus.events.repository.CampusEventRepository;
import com.campus.events.repository.TicketTierRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ticket-tiers")
public class TicketTierController {
  public record TierRequest(UUID eventId, String name, int priceInCentavos, int capacity) {}

  private final TicketTierRepository tiers;
  private final CampusEventRepository events;

  public TicketTierController(TicketTierRepository tiers, CampusEventRepository events) {
    this.tiers = tiers;
    this.events = events;
  }

  @GetMapping
  public java.util.List<TicketTier> findAll() {
    return tiers.findAll();
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public TicketTier create(@RequestBody TierRequest request) {
    var event = events.findById(request.eventId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    TicketTier tier = new TicketTier();
    tier.setEvent(event);
    tier.setName(request.name());
    tier.setPriceInCentavos(request.priceInCentavos());
    tier.setCapacity(request.capacity());
    return tiers.save(tier);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public TicketTier update(@PathVariable UUID id, @RequestBody TierRequest request) {
    TicketTier tier = tiers.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tier not found"));
    if (request.name() != null) tier.setName(request.name());
    if (request.priceInCentavos >= 0) tier.setPriceInCentavos(request.priceInCentavos());
    if (request.capacity > 0) tier.setCapacity(request.capacity());
    return tiers.save(tier);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public void delete(@PathVariable UUID id) {
    tiers.deleteById(id);
  }
}
