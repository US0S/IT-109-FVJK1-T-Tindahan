package com.campus.events.repository;

import com.campus.events.model.CampusEvent;
import com.campus.events.model.EventStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusEventRepository extends JpaRepository<CampusEvent, UUID> {
  List<CampusEvent> findByStatusOrderByStartAtAsc(EventStatus status);
}
