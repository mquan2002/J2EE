package com.medical.service;

import com.medical.model.Appointment;
import com.medical.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;



    public Appointment addAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);
    }

    // Lấy tất cả lịch hẹn
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Lấy lịch hẹn theo ID
    public Appointment getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null); // trả về null nếu không tìm thấy
    }

    // Xóa lịch hẹn theo ID
    public void deleteAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }

    // Cập nhật thông tin lịch hẹn
    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
}
