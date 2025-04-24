package com.medical.controller;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.Specialty;
import com.medical.repository.SpecialtyRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class AuthController {
    @Autowired
    private SpecialtyRepository specialtyRepository;

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

//    @GetMapping("/register/doctor")
//    public String showDoctorRegistrationForm(Model model) {
//        model.addAttribute("doctor", new Doctor());
//        model.addAttribute("specialties", Arrays.asList(
//            "Nội khoa",
//            "Nhi khoa",
//            "Da liễu",
//            "Thần kinh",
//            "Tim mạch",
//            "Tiêu hóa",
//            "Hô hấp",
//            "Nội tiết",
//            "Thận",
//            "Xương khớp"
//        ));
//        return "auth/register-doctor";
//    }

    @GetMapping("/register/patient")
    public String showPatientRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "auth/register-patient";
    }
    @GetMapping("/register/doctor")
    public String showRegisterDoctor(Model model) {
        List<Specialty> specialties = specialtyRepository.findAll();
        model.addAttribute("specialties", specialties);
        model.addAttribute("doctor", new Doctor());
        return "auth/register-doctor";
    }
    @PostMapping("/register/doctor")
    public String registerDoctor( @ModelAttribute("doctor") Doctor doctor,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 @RequestParam("specialtyId") Long specialtyId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        // Lấy danh sách chuyên khoa để hiển thị nếu cần reload form
        List<Specialty> specialties = specialtyRepository.findAll();
        model.addAttribute("specialties", specialties);

        try {
            // Tìm chuyên khoa theo ID
            Specialty specialty = specialtyService.findById(specialtyId);
            if (specialty == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy chuyên khoa.");
                return "redirect:/register/doctor";
            }

            // Gán chuyên khoa cho bác sĩ
            doctor.setSpecialty(specialty);

            // Xử lý ảnh upload
            if (!imageFile.isEmpty()) {
                // Lấy tên file từ MultipartFile
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

                // Thư mục lưu ảnh
                String uploadDir = "src/main/resources/static/uploads/doctor";
                // Lưu tên file vào đối tượng Doctor
                doctor.setImage(fileName);

                // Tạo đường dẫn thư mục nếu chưa tồn tại
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file lên server
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // Thêm thông báo lỗi nếu không thể lưu ảnh
                    redirectAttributes.addFlashAttribute("error", "Không thể lưu ảnh: " + e.getMessage());
                    return "redirect:/register/doctor";
                }

                doctor.setImage(fileName);
            }

            // Gọi service lưu bác sĩ
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