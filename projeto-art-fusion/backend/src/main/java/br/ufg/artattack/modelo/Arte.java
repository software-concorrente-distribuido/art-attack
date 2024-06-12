package br.ufg.artattack.modelo;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name="arte")
@Getter
public class Arte extends EntidadeJPA implements Serializable {

    public String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    public Usuario usuario;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] snapshot;

    @Enumerated(EnumType.STRING)
    public Visibilidade visibilidade;


    public Arte(){
        this.visibilidade = Visibilidade.PRIVADO;
        this.dataCriacao = new Date();
    }


    public Arte(Long id){
        this.setId( id);
    }

    public enum Visibilidade{
        PUBLICO("PUBLICO"),
        PRIVADO("PRIVADO");

        String valor;

        Visibilidade(String valor){
            this.valor = valor;
        }

    }

}
