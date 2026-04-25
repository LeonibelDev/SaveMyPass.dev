package com.dac.passwordmanager.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dac.passwordmanager.entity.Note;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteResponseDTO {
    private UUID id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NoteResponseDTO() {
    }

    public static NoteResponseDTO NoteResponseDTO(Note note) {
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(note.getId());
        noteResponseDTO.setTitle(note.getTitle());
        noteResponseDTO.setContent(note.getContent());
        noteResponseDTO.setCategory(note.getCategory());
        noteResponseDTO.setCreatedAt(note.getCreatedAt());
        noteResponseDTO.setUpdatedAt(note.getUpdatedAt());

        return noteResponseDTO;
    }
}
