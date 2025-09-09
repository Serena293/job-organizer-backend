package com.project.job_organizer.controller;

import com.project.job_organizer.model.NotesEntity;
import com.project.job_organizer.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private  final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @GetMapping
    public ResponseEntity<List<NotesEntity>> getAllNotes() {
        List<NotesEntity> notes = noteService.getAllNotes();
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @PostMapping
    public ResponseEntity<NotesEntity> addNotes(@RequestBody NotesEntity notesEntity) {
        NotesEntity saved = noteService.createNote(notesEntity);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesEntity> updateNote(@PathVariable Long id, @RequestBody NotesEntity notesEntity) {
        NotesEntity updated = noteService.updateNote(id, notesEntity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotesEntity> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }


}
