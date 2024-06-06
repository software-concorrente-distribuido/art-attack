package br.ufg.artattack.dto;

import lombok.Getter;
import br.ufg.artattack.modelo.Usuario;

@Getter
public class UsuarioDTO {
    public String email;
    public String id;

    public Boolean ativo;

    public UsuarioDTO(){}

    public UsuarioDTO(Usuario usuario){
        if(usuario==null){
            throw new RuntimeException("Nenhum usuário foi fornecido!");
        }
        this.email = usuario.email;
        this.id = usuario.getId().toString();
        this.ativo = usuario.ativo;
    }


}
