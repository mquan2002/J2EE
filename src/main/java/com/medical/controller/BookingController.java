package com.medical.controller;

import com.medical.model.Appointment;
import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.Specialty;
import com.medical.repository.DoctorRepository;
import com.medical.repository.SpecialtyRepository;
import com.medical.service.AppointmentService;
import com.medical.service.DoctorService;
import com.medical.service.PatientService;
import com.medical.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.*;
import java.util.List;


@Controller
public class BookingController {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private SpecialtyService specialtyService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/booking")
    public String booking(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Specialty> specialties = specialtyRepository.findAll();

        model.addAttribute("doctors", doctors);
        model.addAttribute("specialties", specialties);
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("pageTitle", "Đặt lịch khám bệnh");
        model.addAttribute("contentPage", "booking"); // chú ý booking.html
        return "index"; // load index.html
    }

    @PostMapping("/booking/create")
    public String booking(@ModelAttribute("appointment") Appointment appointment,
                          @RequestParam("specialtyId") Long specialtyId,
                          @RequestParam("doctorId") Long doctorId,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // Lấy bệnh nhân đang đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Patient patient = null;

        if (authentication != null && authentication.getPrincipal() instanceof Patient) {
            patient = (Patient) authentication.getPrincipal();
        }

        if (patient == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "redirect:/booking";
        }

        appointment.setPatient(patient);

        // Lấy danh sách chuyên khoa để hiển thị nếu cần reload form
        List<Specialty> specialties = specialtyRepository.findAll();
        model.addAttribute("specialties", specialties);

        // Lấy danh sách bác sĩ để hiển thị nếu cần reload form
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("specialties", specialties);
        try {
            // Tìm chuyên khoa theo ID
            Specialty specialty = specialtyService.findById(specialtyId);
            if (specialty == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy chuyên khoa.");
                return "redirect:/booking";
            }

            // Gán chuyên khoa cho appointment
            appointment.setSpecialty(specialty);

            // Tìm bác sĩ theo ID
            Doctor doctor = doctorService.findById(doctorId);
            if (doctor == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bác sĩ.");
                return "redirect:/booking";
            }

            // Gán chuyên khoa cho bác sĩ
            appointment.setDoctor(doctor);


            appointmentService.addAppointment(appointment);
            appointment.setPatient(patient);
            redirectAttributes.addFlashAttribute("success", "Đăng ký lịch khám thành công!");
            return "redirect:/booking_success"; // Redirect sau khi đặt lịch thành công
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + e.getMessage());
                return "redirect:/booking";
            }
    }

    @GetMapping("/booking_success")
    public String booking_success(Model model) {
        model.addAttribute("pageTitle", "Đăng ký thành công");
        model.addAttribute("contentPage", "booking_success");
        return "index"; // Trả về index.htm
    }
}



