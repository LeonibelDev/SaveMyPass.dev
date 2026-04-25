package com.dac.passwordmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dac.passwordmanager.entity.Note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByUserId(Long userId);

    Optional<Note> findById(UUID id);

    void deleteById(UUID id);
}
