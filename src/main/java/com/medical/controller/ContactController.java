package com.medical.controller;

//import com.medical.model.ContactVerification;
//import com.medical.repository.ContactRepository;
import com.medical.service.MailService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class ContactController {

    @Autowired
    private MailService mailService;

    // xử lý khi người dùng gửi form liên hệ
    @PostMapping("/contact")
    public String handleContact(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String message,
            Model model) {
        mailService.sendContactEmail(fullName, email, message);
        model.addAttribute("success", true);
        model.addAttribute("contentPage", "contact");
        return "index";
    }

    // hiển thị form liên hệ
    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("contentPage", "contact");
        return "index"; // trả về index, thymeleaf chèn fragment vào
    }
}
