package com.stampbot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebRouter {

    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
