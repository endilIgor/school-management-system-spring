package com.nagi.school_management_system_spring.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;
import com.nagi.school_management_system_spring.validation.annotation.ValidCPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory!!")
    private String name;

    @Email(message = "Email should be valid!!")
    @NotBlank(message = "Email is mandatory!!")
    private String email;

    @NotBlank(message = "Password is mandatory!!")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters!!")
    private String password;

    @NotBlank(message = "Phone Number is mandatory!!")
    @Size(min = 10, max = 15, message = "Phone Number must be between 10 and 15 characters!!")
    private String phoneNumber;

    @NotBlank(message = "CPF is mandatory!!")
    @ValidCPF
    private String cpf;

    @NotBlank(message = "Address is mandatory!!")
    private String address;

    @NotNull(message = "Birth Date is mandatory!!")
    private LocalDate birthDate;

    private String photo;

    @NotNull(message = "User Type is mandatory!!")
    @Enumerated(EnumType.STRING)
    private UserTypeEnum usertype;

    @NotNull(message = "Active status is mandatory!!")
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
