package com.dac.passwordmanager.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.dac.passwordmanager.entity.User;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.passwordmanager.dto.NoteRequestDTO;
import com.dac.passwordmanager.dto.response.ApiResponseDTO;
import com.dac.passwordmanager.dto.response.NoteResponseDTO;
import com.dac.passwordmanager.entity.Note;
import com.dac.passwordmanager.service.NoteService;
import com.dac.passwordmanager.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> addNote(@RequestBody NoteRequestDTO noteDTO,
            @AuthenticationPrincipal Claims claims) {
        Long userId = claims.get("userId", Long.class);
        Optional<User> user = userService.getUserById(userId);

        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setCategory(noteDTO.getCategory());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setUser(user.get());

        Note savedNote = noteService.save(note);

        return ResponseEntity
                .ok(ApiResponseDTO.ok("Note added successfully", NoteResponseDTO.NoteResponseDTO(savedNote)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getAllNotes(@AuthenticationPrincipal Claims claims) {

        Long userId = claims.get("userId", Long.class);
        List<NoteResponseDTO> notes = noteService.getAllNotes(userId);

        return ResponseEntity
                .ok(ApiResponseDTO.ok("Notes retrieved successfully", notes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> getNoteById(@PathVariable UUID id,
            @AuthenticationPrincipal Claims claims) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok(ApiResponseDTO.ok("Note retrieved successfully", NoteResponseDTO.NoteResponseDTO(note.get())));
    }

}
