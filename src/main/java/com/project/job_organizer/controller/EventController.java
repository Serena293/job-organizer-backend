package com.project.job_organizer.controller;

import com.project.job_organizer.model.EventEntity;
import com.project.job_organizer.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity event) {
        EventEntity savedEvent = eventService.createEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    // READ - tutti gli eventi dell'utente loggato
    @GetMapping
    public ResponseEntity<List<EventEntity>> getMyEvents() {
        List<EventEntity> events = eventService.getMyEvents();
        return ResponseEntity.ok(events);
    }

    // READ - eventi in una data specifica (es. /api/events/by-date?date=12-09-2025)
    @GetMapping("/by-date")
    public ResponseEntity<List<EventEntity>> getMyEventsByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<EventEntity> events = eventService.getMyEventsByDate(date);
        return ResponseEntity.ok(events);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(
            @PathVariable Long id,
            @RequestBody EventEntity updatedEvent) {
        EventEntity event = eventService.updateEvent(id, updatedEvent);
        return ResponseEntity.ok(event);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
