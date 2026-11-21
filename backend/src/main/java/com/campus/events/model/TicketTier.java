package com.campus.events.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

@Entity
@Table(name = "ticket_tiers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TicketTier {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private CampusEvent event;

  @Column(nullable = false)
  private String name;
  private int priceInCentavos;
  private int capacity;
  private int soldCount;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public CampusEvent getEvent() { return event; }
  public void setEvent(CampusEvent event) { this.event = event; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public int getPriceInCentavos() { return priceInCentavos; }
  public void setPriceInCentavos(int priceInCentavos) { this.priceInCentavos = priceInCentavos; }
  public int getCapacity() { return capacity; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public int getSoldCount() { return soldCount; }
  public void setSoldCount(int soldCount) { this.soldCount = soldCount; }
}
