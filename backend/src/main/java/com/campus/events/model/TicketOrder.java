package com.campus.events.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ticket_orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TicketOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attendee_id", nullable = false)
  private User attendee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tier_id", nullable = false)
  private TicketTier tier;

  @Column(unique = true, nullable = false)
  private UUID ticketUuid = UUID.randomUUID();
  private int quantity = 1;

  @Enumerated(EnumType.STRING)
  private TicketStatus status = TicketStatus.UNPAID;
  private String paymentReference;
  private Instant paidAt;
  private Instant usedAt;
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public User getAttendee() { return attendee; }
  public void setAttendee(User attendee) { this.attendee = attendee; }
  public TicketTier getTier() { return tier; }
  public void setTier(TicketTier tier) { this.tier = tier; }
  public UUID getTicketUuid() { return ticketUuid; }
  public void setTicketUuid(UUID ticketUuid) { this.ticketUuid = ticketUuid; }
  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public TicketStatus getStatus() { return status; }
  public void setStatus(TicketStatus status) { this.status = status; }
  public String getPaymentReference() { return paymentReference; }
  public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
  public Instant getPaidAt() { return paidAt; }
  public void setPaidAt(Instant paidAt) { this.paidAt = paidAt; }
  public Instant getUsedAt() { return usedAt; }
  public void setUsedAt(Instant usedAt) { this.usedAt = usedAt; }
  public Instant getCreatedAt() { return createdAt; }
}
