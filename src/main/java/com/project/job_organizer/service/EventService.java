package com.project.job_organizer.service;

import com.project.job_organizer.model.EventEntity;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.EventRepository;
import com.project.job_organizer.repository.UserRepository;
import com.project.job_organizer.controller.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    public EventEntity createEvent(EventEntity event) {
        UserEntity user = getLoggedUser();
        event.setUser(user);
        return eventRepository.save(event);
    }


    public List<EventEntity> getMyEvents() {
        UserEntity user = getLoggedUser();
        return eventRepository.findByUser(user);
    }

    public List<EventEntity> getMyEventsByDate(LocalDate date) {
        UserEntity user = getLoggedUser();
        return eventRepository.findByUserAndEventDate(user, date);
    }


    public EventEntity updateEvent(Long eventId, EventEntity updatedEvent) {
        UserEntity user = getLoggedUser();

        EventEntity existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this event");
        }

        existing.setEventTitle(updatedEvent.getEventTitle());
        existing.setEventDescription(updatedEvent.getEventDescription());
        existing.setEventDate(updatedEvent.getEventDate());

        return eventRepository.save(existing);
    }


    public void deleteEvent(Long eventId) {
        UserEntity user = getLoggedUser();

        EventEntity existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this event");
        }

        eventRepository.delete(existing);
    }

    private UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
