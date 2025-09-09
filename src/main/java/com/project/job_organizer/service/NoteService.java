package com.project.job_organizer.service;

import com.project.job_organizer.controller.UserPrincipal;
import com.project.job_organizer.model.NotesEntity;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.NoteRepository;
import com.project.job_organizer.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    private UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    }
    //CRUD
    public NotesEntity createNote(NotesEntity note) {
        UserEntity user = getLoggedUser();
        note.setUser(user);
        return noteRepository.save(note);
    }

    public List<NotesEntity> getAllNotes() {
        UserEntity user = getLoggedUser();
        return noteRepository.findAll();
    }


    public NotesEntity updateNote(Long noteId, NotesEntity updatedNote) {
        UserEntity user = getLoggedUser();

        NotesEntity existingNote = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!existingNote.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this note");
        }
        existingNote.setNoteTitle(updatedNote.getNoteTitle());
        existingNote.setNoteContent(updatedNote.getNoteContent());
        return noteRepository.save(existingNote);
    }

     public void  deleteNote(Long noteId) {
        UserEntity user = getLoggedUser();

        NotesEntity existingNote = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!existingNote.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this note");
        }
        noteRepository.delete(existingNote);
     }


}

