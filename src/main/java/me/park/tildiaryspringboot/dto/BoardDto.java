package me.park.tildiaryspringboot.dto;

import java.time.LocalDateTime;

public interface BoardDto {

    Long getBoardId();

    String getTitle();

    String getContent();

    String getEmotion();

    LocalDateTime getCreatedAt();

    LocalDateTime getModifiedAt();

    Long getUserId();

    String getUsername();

    String getNickname();

}
