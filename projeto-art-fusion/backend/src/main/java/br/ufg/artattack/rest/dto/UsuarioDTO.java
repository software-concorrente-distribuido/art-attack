package br.ufg.artattack.rest.dto;

import lombok.Getter;
import br.ufg.artattack.modelo.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UsuarioDTO {
    public String email;
    public String cpf;
    public String id;

    public UsuarioDTO(){}

    public UsuarioDTO(Usuario usuario){
        this.email = usuario.email;
        this.cpf = usuario.getCpf();
        this.id = usuario.getId().toString();
    }


}
