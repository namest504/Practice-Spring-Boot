package com.list.springsecurityjwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.list.springsecurityjwt.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto {
        private String name;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class LoginResponseDto {
        private Boolean success;
        private Long id;
        private String accessToken;
        private String refreshToken;
//        private Boolean isAlreadyLogin;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequestDto {
        private String name;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class RegisterResponseDto {
        private Boolean success;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InquireRequestDto {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class MemberResponseDto {
        private Boolean success;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePasswordRequestDto {
        private String rawPassword;
        private String newPassword;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class TestResponseDto {
        private Boolean success;
        private String name;
    }



}
