package com.quizfang.quizfang.domain.dto.request.admin.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Fullname must not be blank")
    @Size(min = 3, max = 100, message = "Fullname's length must be from 3 to 100")
    private String fullName;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password's length must be from 3 to 100")
    private String password;

    @NotNull(message = "Role must not be blank")
    private String role;

    private String about;

}
