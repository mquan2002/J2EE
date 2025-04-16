package com.medical.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "specialty")
@Data // Tự tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "image")
    private String image;

    // Có thể thêm @OneToMany nếu muốn liên kết ngược với Doctor
}

