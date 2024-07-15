package br.ufg.artattack.dto;

import br.ufg.artattack.exception.ProcessamentoException;
import lombok.Getter;
import br.ufg.artattack.modelo.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UsuarioDTO {
    public String email;
    public String id;

    public Boolean ativo;

    public UsuarioDTO(){}

    public UsuarioDTO(Usuario usuario){
        if(usuario==null){
            throw new ProcessamentoException("Nenhum usuário foi fornecido!");
        }
        this.email = usuario.email;
        this.id = usuario.getId().toString();
        this.ativo = usuario.ativo;
    }




}
