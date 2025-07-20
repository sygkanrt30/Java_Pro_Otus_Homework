package ru.otus.homework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientPageController {
    @GetMapping("${client.page.path}")
    public String clientPage() {
        return "client";
    }
}
