package br.ufg.artattack.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String senha;

}
