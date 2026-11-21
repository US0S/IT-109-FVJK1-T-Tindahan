package com.campus.events.controller;

import com.campus.events.model.*;
import com.campus.events.repository.TicketOrderRepository;
import com.campus.events.repository.TicketTierRepository;
import com.campus.events.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ticket-orders")
public class TicketOrderController {
  public record OrderRequest(UUID tierId, int quantity, TicketStatus status, String paymentReference) {}
  public record VerifyRequest(UUID ticketUuid) {}

  private final TicketOrderRepository orders;
  private final TicketTierRepository tiers;
  private final UserRepository users;

  public TicketOrderController(TicketOrderRepository orders, TicketTierRepository tiers, UserRepository users) {
    this.orders = orders;
    this.tiers = tiers;
    this.users = users;
  }

  @GetMapping
  public java.util.List<TicketOrder> findAll() {
    return orders.findAll();
  }

  @PostMapping
  public TicketOrder create(@RequestBody OrderRequest request, @AuthenticationPrincipal User attendee) {
    TicketTier tier = tiers.findById(request.tierId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tier not found"));
    if (tier.getSoldCount() + request.quantity() > tier.getCapacity()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket capacity exceeded");
    }
    TicketOrder order = new TicketOrder();
    order.setAttendee(attendee);
    order.setTier(tier);
    order.setQuantity(request.quantity());
    order.setStatus(request.status() == null ? TicketStatus.UNPAID : request.status());
    if (order.getStatus() == TicketStatus.PAID) order.setPaidAt(Instant.now());
    tier.setSoldCount(tier.getSoldCount() + request.quantity());
    tiers.save(tier);
    return orders.save(order);
  }

  @PostMapping("/verify")
  @PreAuthorize("hasAnyRole('EVENT_ORGANIZER','SUPER_ADMIN')")
  public TicketOrder verify(@RequestBody VerifyRequest request) {
    TicketOrder order = orders.findByTicketUuid(request.ticketUuid())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    if (order.getStatus() == TicketStatus.USED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket has already been used");
    }
    if (order.getStatus() != TicketStatus.PAID) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket is not paid");
    }
    order.setStatus(TicketStatus.USED);
    order.setUsedAt(Instant.now());
    return orders.save(order);
  }
}
