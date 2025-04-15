package com.medical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorController {
    @GetMapping("/doctor")
    public String doctor(Model model) {
        model.addAttribute("pageTitle", "Bác sĩ");
        model.addAttribute("contentPage", "doctor");
        return "index"; // Trả về index.htm
    }
}
