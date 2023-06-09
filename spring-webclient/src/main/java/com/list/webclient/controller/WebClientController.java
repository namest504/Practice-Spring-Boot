package com.list.webclient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
