package com.medical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {
    @GetMapping("/booking")
    public String booking(Model model) {
        model.addAttribute("pageTitle", "Đặt Lịch");
        model.addAttribute("contentPage", "booking"); // Chỉ định booking.html
        return "index"; // Trả về index.htm
    }
}


