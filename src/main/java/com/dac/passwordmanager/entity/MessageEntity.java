package com.dac.passwordmanager.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEntity {
    private String recipient;
    private String subject;
    private String body;
    private String url;
    private String buttonText;
    private String date;
    private String ip;
    private String location;
    private String device;

    public MessageEntity() {
    }
}
