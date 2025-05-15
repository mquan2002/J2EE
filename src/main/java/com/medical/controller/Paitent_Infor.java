package com.medical.controller;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.repository.DoctorRepository;
import com.medical.repository.PatientRepository;
import com.medical.service.DoctorService;
import com.medical.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
public class Paitent_Infor {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @GetMapping("/patient_profile")
    public String patient_profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Patient patient = (Patient) auth.getPrincipal();
        Long patientId = patient.getId();

        Patient patient_info = patientRepository.findById(patientId).orElse(null);
        model.addAttribute("patient", patient_info);
        model.addAttribute("pageTitle", "Thông tin bệnh nhân");
        model.addAttribute("contentPage", "patient_profile");
        return "index";
    }

    @PostMapping("/patient_profile/update")
    public String patient_profile_update(@ModelAttribute("patient") Patient patient,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        try {
            patientService.updatePatient(patient);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
            return "redirect:/patient_profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
            return "redirect:/patient_profile";
        }
    }

}
