package com.medical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu");
        model.addAttribute("contentPage", "about"); // Chỉ định about.html
        return "index"; // Trả về index.htm
    }
    @GetMapping("/about/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên hệ");
        model.addAttribute("contentPage", "contact"); // Chỉ định about.html
        return "index"; // Trả về index.htm
    }
}
