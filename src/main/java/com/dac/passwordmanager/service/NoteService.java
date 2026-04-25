package com.dac.passwordmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dac.passwordmanager.dto.response.NoteResponseDTO;
import com.dac.passwordmanager.entity.Note;
import com.dac.passwordmanager.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository noteRepository;

    /*
     * Create a new note
     * 
     * @param Note
     * 
     * @return Note
     */
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    /*
     * Get a note by ID
     * 
     * @param UUID
     * 
     * @return Optional<Note>
     */
    public Optional<Note> getNoteById(UUID id) {
        return noteRepository.findById(id);
    }

    /*
     * Get all notes
     * 
     * @param userId
     * 
     * @return List<Note>
     */
    public List<NoteResponseDTO> getAllNotes(Long userId) {
        List<Note> notes = noteRepository.findByUserId(userId);
        return notes.stream().map(note -> {
            NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
            noteResponseDTO.setId(note.getId());
            noteResponseDTO.setTitle(note.getTitle());
            noteResponseDTO.setContent(note.getContent());
            noteResponseDTO.setCategory(note.getCategory());
            noteResponseDTO.setCreatedAt(note.getCreatedAt());
            noteResponseDTO.setUpdatedAt(note.getUpdatedAt());
            return noteResponseDTO;
        }).collect(Collectors.toList());
    }

    /*
     * Delete one note
     * 
     * @param UUID
     */
    public void deleteNote(UUID id) {
        noteRepository.deleteById(id);
    }
}
