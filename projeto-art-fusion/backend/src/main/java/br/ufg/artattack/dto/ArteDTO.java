package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Arte;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public class ArteDTO {

    public String titulo;

    public Long id;

    public Long usuarioId;

    public String usuarioNome;

    public Date dataCriacao;

    public ArteDTO(){

    }

    public ArteDTO(Arte arte){
        this.id = arte.getId();
        this.dataCriacao = arte.dataCriacao;
        this.titulo = arte.titulo;
        var user = arte.usuario;
        if(user!=null){
            this.usuarioId = user.getId();
            this.usuarioNome =user.getUsername();
        }
    }




}
