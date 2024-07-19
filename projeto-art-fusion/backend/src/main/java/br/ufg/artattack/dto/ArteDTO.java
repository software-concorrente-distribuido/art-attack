package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.Snapshot;

import java.util.Date;


public class ArteDTO {

    public String titulo;

    public Long id;

    public UsuarioDTO administrador;

    public Snapshot snapshot;

    public Date dataCriacao;

    public String visibilidade;

    public ArteDTO(){

    }

    public ArteDTO(Arte arte){
        this.id = arte.getId();
        this.dataCriacao = arte.dataCriacao;
        this.titulo = arte.titulo;
        var user = arte.administrador;
        this.visibilidade = arte.visibilidade.toString();
        if(user!=null){
           this.administrador = new UsuarioDTO(arte.administrador);
        }
    }




}
