package com.campus.events.repository;

import com.campus.events.model.TicketOrder;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketOrderRepository extends JpaRepository<TicketOrder, UUID> {
  Optional<TicketOrder> findByTicketUuid(UUID ticketUuid);
}
