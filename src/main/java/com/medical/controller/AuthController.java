package com.medical.controller;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.Specialty;
import com.medical.service.DoctorService;
import com.medical.service.PatientService;
import com.medical.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;

@Controller
public class AuthController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterChoice() {
        return "auth/register-choice";
    }

    @GetMapping("/register/doctor")
    public String showDoctorRegistrationForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("specialties", Arrays.asList(
            "Nội khoa",
            "Nhi khoa",
            "Da liễu",
            "Thần kinh",
            "Tim mạch",
            "Tiêu hóa",
            "Hô hấp",
            "Nội tiết",
            "Thận",
            "Xương khớp"
        ));
        return "auth/register-doctor";
    }

    @GetMapping("/register/patient")
    public String showPatientRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "auth/register-patient";
    }

    @PostMapping("/register/doctor")
    public String registerDoctor(@ModelAttribute Doctor doctor, RedirectAttributes redirectAttributes) {
        try {
            // Tìm hoặc tạo mới chuyên khoa
            Specialty specialty = specialtyService.findByName(doctor.getSpecialty().getName());
            if (specialty == null) {
                specialty = new Specialty();
                specialty.setName(doctor.getSpecialty().getName());
                specialty = specialtyService.save(specialty);
            }
            doctor.setSpecialty(specialty);
            
            doctorService.registerDoctor(doctor);
            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản bác sĩ thành công!");
            return "redirect:/register/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "redirect:/register/doctor";
        }
    }

    @PostMapping("/register/patient")
    public String registerPatient(Patient patient, RedirectAttributes redirectAttributes) {
        try {
            patientService.registerPatient(patient);
            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản bệnh nhân thành công!");
            return "redirect:/register/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "redirect:/register/patient";
        }
    }

    @GetMapping("/register/success")
    public String showRegisterSuccess() {
        return "auth/register-success";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Kiểm tra role và chuyển hướng
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
                return "redirect:/doctor/dashboard";
            } else {
                return "redirect:/patient/dashboard";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại: Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
} 