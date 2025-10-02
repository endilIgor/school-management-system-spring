package com.nagi.school_management_system_spring.dto;

import com.nagi.school_management_system_spring.model.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private Long userId;
    private String email;
    private String name;
    private UserTypeEnum userType;
    private String token;
}
