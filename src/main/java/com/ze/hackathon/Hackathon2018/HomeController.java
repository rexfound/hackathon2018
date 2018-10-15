package com.ze.hackathon.Hackathon2018;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewSelection(Model result) {
        return "home";
    }
}
