package com.medical.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "specialties")
@Data // Tự tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    private String image;

    @Column(nullable = false)
    private boolean isActive = true;

    // Có thể thêm @OneToMany nếu muốn liên kết ngược với Doctor
}

