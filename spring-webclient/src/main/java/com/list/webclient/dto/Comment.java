package com.list.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Comment {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long postId;
        private Long id;
        private String name;
        private String email;
        private String body;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponse {
        private boolean success;
        private List<CommentDto> commentDtos = new ArrayList<>();
    }
}
