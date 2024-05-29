package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Arte;
import lombok.Getter;

import java.util.Date;

@Getter
public class ArteDTO {

    String titulo;

    Long id;

    Long usuarioId;

    Date dataCriacao;

    public ArteDTO(){

    }

    public ArteDTO(Arte arte){
        this.id = arte.getId();
        this.dataCriacao = arte.dataCriacao;
        this.titulo = arte.titulo;
        this.usuarioId = arte.usuario !=null ? arte.usuario.getId() : null;
    }

    static ArteDTO toDTO(Arte arte){
        return new ArteDTO(arte);
    }

}
