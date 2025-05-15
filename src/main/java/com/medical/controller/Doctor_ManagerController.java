package com.medical.controller;

import com.medical.model.Doctor;
import com.medical.repository.DoctorRepository;
import com.medical.service.DoctorService;
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
public class Doctor_ManagerController {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctor_dashboard")
    public String doctor_home(Model model) {
        model.addAttribute("pageTitle", "Trang chủ bác sĩ");
        model.addAttribute("contentPage", "doctor/doctor_dashboard");
        return "doctor/doctor_manager";
    }

    @GetMapping("/doctor_pro")
    public String doctor_pro(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Doctor doctor = (Doctor) auth.getPrincipal(); // ép kiểu sang Doctor
        Long doctorId = doctor.getId(); // lấy ID từ entity

        Doctor doctorInfo = doctorRepository.findById(doctorId).orElse(null);
        model.addAttribute("doctor", doctorInfo); // không cần list
        model.addAttribute("pageTitle", "Thông tin bác sĩ");
        model.addAttribute("contentPage", "doctor/profile");
        return "doctor/doctor_manager";
    }

    @PostMapping("/doctor_pro/update")
    public String doctor_pro_update(Model model,
                                    RedirectAttributes redirectAttributes,
                                    @ModelAttribute("doctor") Doctor doctor,
                                    @RequestParam("imageFile") MultipartFile imageFile) {
        try {
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
                    return "redirect:/doctor_pro";
                }

                doctor.setImage(fileName);

            }
            doctorService.updateDoctor(doctor);

            redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản bác sĩ thành công!");
            return "redirect:/doctor_pro";
        }catch (Exception e){
            return "redirect:/doctor_pro";
        }
    }
}
