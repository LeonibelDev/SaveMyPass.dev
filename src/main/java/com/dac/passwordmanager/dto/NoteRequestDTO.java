package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequestDTO {
    private String title;
    private String content;
    private String category;

    public NoteRequestDTO() {
    }
}
