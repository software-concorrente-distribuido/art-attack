package br.ufg.artattack.modelo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity(name="arte")
public class Arte extends EntidadeJPA implements Serializable {

    public String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "administrador_id")
    public Usuario administrador;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] snapshot;

    @Enumerated(EnumType.STRING)
    public Visibilidade visibilidade;


    public Arte(){
        this.visibilidade = Visibilidade.PRIVADO;
        this.dataCriacao = new Date();
    }

    public Arte(boolean template){
        if(template){
            return;
        }
        this.visibilidade = Visibilidade.PRIVADO;
        this.dataCriacao = new Date();
    }


    public Arte(Long id){
        this.setId( id);
    }



}
