package com.list.webclient.controller;

import com.list.webclient.dto.User.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/apitest")
    public Mono<UserResponse> basicApiTest() {
        return WebClient.create("https://jsonplaceholder.typicode.com")
                .get()
                .uri("/todos/1")
                .retrieve()
                .bodyToMono(UserResponse.class);
    }


}
