package com.list.webclient.controller;

import com.list.webclient.dto.User.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.list.webclient.dto.Comment.*;

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

    @GetMapping("/apitest2")
    public  Flux<CommentDto> queryApiTest() {

        Flux<CommentDto> commentDtoFlux = WebClient.create("https://jsonplaceholder.typicode.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/comments/")
                        .queryParam("postId", "1")
                        .build())
                .retrieve()
                .bodyToFlux(CommentDto.class);

        return commentDtoFlux;
    }
}
