package br.ufg.artattack.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutenticacaoDTO {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String senha;

}
