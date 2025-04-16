package com.medical.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "doctor")
@Data // Tự tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    private Long id;

    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Date Dob;



    @Column(name = "image")
    private String image;


    @ManyToOne
    @JoinColumn(name = "specialty_id") // tên cột FK trong bảng doctor
    private Specialty specialty;
}
