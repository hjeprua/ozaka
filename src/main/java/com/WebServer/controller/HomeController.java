package com.WebServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/home", "/", ""})
    public String homePage() {
        return "home";
    }

    @GetMapping("/download")
    public String download() {
        return "download";
    }

    @GetMapping("/forum")
    public String forum() {
        return "forum";
    }

    @GetMapping("/server")
    public String ip() {
        return "NJVI";
    }
}
