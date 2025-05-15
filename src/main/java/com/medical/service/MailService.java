package com.medical.service;

//import com.medical.model.ContactVerification;
//import com.medical.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactEmail(String fullName, String email, String messageContent){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("tuanman1216@gmail.com");
        message.setSubject("Liên hệ từ " + fullName);
        message.setText("Email người gửi: " + email + "\n\nNội dung:\n" + messageContent);
        message.setFrom("tuanman1216@gmail.com");

        mailSender.send(message);
    }
}
