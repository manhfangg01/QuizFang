package com.quizfang.quizfang.domain.dto.request.admin.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequest {

    @NotNull(message = "ID must not be blank")
    private Long id;

    @NotBlank(message = "Fullname must not be blank")
    @Size(min = 3, max = 100, message = "Fullname's length must be from 3 to 100")
    private String fullName;

    @NotNull(message = "Role must not be blank")
    private String role;

    private String about;
}
