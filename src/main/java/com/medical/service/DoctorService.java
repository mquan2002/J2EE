package com.medical.service;

import com.medical.model.Doctor;
import com.medical.model.Specialty;
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

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElse(null);
    }

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

    public Doctor updateDoctor(Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(updatedDoctor.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        // Chỉ cập nhật các trường có thể sửa (tránh update username/password)
        existingDoctor.setFullName(updatedDoctor.getFullName());
        existingDoctor.setUsername(updatedDoctor.getUsername());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        existingDoctor.setDateOfBirth(updatedDoctor.getDateOfBirth());

        if (updatedDoctor.getImage() != null ){
            existingDoctor.setImage(updatedDoctor.getImage());
        }
        // Cập nhật mật khẩu nếu người dùng nhập mới (không rỗng và khác rỗng)
        if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedDoctor.getPassword());
            existingDoctor.setPassword(encodedPassword);
        }
        // Nếu không nhập mật khẩu mới thì giữ nguyên mật khẩu cũ

        return doctorRepository.save(existingDoctor);
    }
} 