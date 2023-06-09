package com.list.webclient.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class User {

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class UserResponse {
        private Long userId;
        private Long id;
        private String title;
        private boolean completed;
    }

}
