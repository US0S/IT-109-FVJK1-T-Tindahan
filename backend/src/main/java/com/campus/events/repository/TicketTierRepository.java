package com.campus.events.repository;

import com.campus.events.model.TicketTier;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTierRepository extends JpaRepository<TicketTier, UUID> {}
