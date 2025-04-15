package com.medical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpecialtyController {
    @GetMapping("/specialty")
    public String specialty(Model model) {
        model.addAttribute("pageTitle", "Chuyên Khoa");
        model.addAttribute("contentPage", "specialty");
        return "index"; // Trả về index.htm
    }
}
