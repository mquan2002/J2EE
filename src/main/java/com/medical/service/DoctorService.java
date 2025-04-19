package com.medical.service;

import com.medical.model.Doctor;
import com.medical.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DoctorService implements UserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bác sĩ với tên đăng nhập: " + username));
    }

    public Doctor registerDoctor(Doctor doctor) {
        if (doctorRepository.existsByUsername(doctor.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        String encodedPassword = passwordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        return doctorRepository.save(doctor);
    }

    public List<String> getAllSpecialties() {
        return Arrays.asList(
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
        );
    }
} 