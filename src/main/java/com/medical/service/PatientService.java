package com.medical.service;

import com.medical.model.Doctor;
import com.medical.model.Patient;
import com.medical.model.Specialty;
import com.medical.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatientService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy bệnh nhân với tên đăng nhập: " + username));
    }


    public Patient registerPatient(Patient patient) {
        if (patientRepository.existsByUsername(patient.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        String encodedPassword = passwordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Patient updatePatient) {
        Patient existingPatient = patientRepository.findById(updatePatient.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));

        // Chỉ cập nhật các trường có thể sửa (tránh update username/password)
        existingPatient.setFullName(updatePatient.getFullName());
        existingPatient.setUsername(updatePatient.getUsername());
        existingPatient.setEmail(updatePatient.getEmail());
        existingPatient.setPhoneNumber(updatePatient.getPhoneNumber());
        existingPatient.setDateOfBirth(updatePatient.getDateOfBirth());

        // Cập nhật mật khẩu nếu người dùng nhập mới (không rỗng và khác rỗng)
        if (updatePatient.getPassword() != null && !updatePatient.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatePatient.getPassword());
            existingPatient.setPassword(encodedPassword);
        }
        // Nếu không nhập mật khẩu mới thì giữ nguyên mật khẩu cũ

        return patientRepository.save(existingPatient);
    }
} 