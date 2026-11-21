package com.campus.events;

import com.campus.events.model.*;
import com.campus.events.repository.CampusEventRepository;
import com.campus.events.repository.TicketTierRepository;
import com.campus.events.repository.UserRepository;
import java.time.Instant;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
  private final UserRepository users;
  private final CampusEventRepository events;
  private final TicketTierRepository tiers;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(UserRepository users, CampusEventRepository events, TicketTierRepository tiers, PasswordEncoder passwordEncoder) {
    this.users = users;
    this.events = events;
    this.tiers = tiers;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(ApplicationArguments args) {
    User superAdmin = users.findByUsername("super_admin").orElseGet(() -> {
      User user = new User();
      user.setUsername("super_admin");
      user.setEmail("super.admin@campus.edu");
      user.setPassword(passwordEncoder.encode("CampusAdmin1!"));
      user.setRole(Role.SUPER_ADMIN);
      return users.save(user);
    });
    if (superAdmin.getEmail() == null) {
      superAdmin.setEmail("super.admin@campus.edu");
      users.save(superAdmin);
    }

    User organizer = users.findByUsername("event_organizer").orElseGet(() -> {
      User user = new User();
      user.setUsername("event_organizer");
      user.setEmail("organizer@campus.edu");
      user.setPassword(passwordEncoder.encode("Organizer1!"));
      user.setRole(Role.EVENT_ORGANIZER);
      return users.save(user);
    });
    if (organizer.getEmail() == null) {
      organizer.setEmail("organizer@campus.edu");
      users.save(organizer);
    }

    User attendee = users.findByUsername("campus_attendee").orElseGet(() -> {
      User user = new User();
      user.setUsername("campus_attendee");
      user.setEmail("attendee@campus.edu");
      user.setPassword(passwordEncoder.encode("Attendee1!"));
      user.setRole(Role.ATTENDEE);
      return users.save(user);
    });
    if (attendee.getEmail() == null) {
      attendee.setEmail("attendee@campus.edu");
      users.save(attendee);
    }

    if (events.count() == 0) {
      CampusEvent event = new CampusEvent();
      event.setTitle("University Intramurals 2026");
      event.setDescription("University-wide intramural sports and campus celebration.");
      event.setVenue("University Gymnasium");
      event.setStartAt(Instant.parse("2026-12-10T08:00:00Z"));
      event.setEndAt(Instant.parse("2026-12-12T18:00:00Z"));
      event.setStatus(EventStatus.PUBLISHED);
      event.setOrganizer(organizer);
      event = events.save(event);

      TicketTier general = new TicketTier();
      general.setEvent(event);
      general.setName("General Admission");
      general.setPriceInCentavos(15000);
      general.setCapacity(500);
      tiers.save(general);

      TicketTier vip = new TicketTier();
      vip.setEvent(event);
      vip.setName("VIP");
      vip.setPriceInCentavos(30000);
      vip.setCapacity(100);
      tiers.save(vip);
    }
  }
}
