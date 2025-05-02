package com.medical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Doctor_ManagerController {
    @GetMapping("/doctor_dashboard")
    public String doctor_home(Model model) {
        model.addAttribute("pageTitle", "Trang chủ bác sĩ");
        model.addAttribute("contentPage", "doctor/doctor_dashboard");
        return "doctor/doctor_manager";
    }
    @GetMapping("/doctor_pro")
    public String doctor_pro(Model model) {
        model.addAttribute("pageTitle", "Thông tin bác sĩ");
        model.addAttribute("contentPage", "doctor/profile");
        return "doctor/doctor_manager";
    }
}
