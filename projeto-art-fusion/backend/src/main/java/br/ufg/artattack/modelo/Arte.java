package br.ufg.artattack.modelo;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name="arte")
@Getter
public class Arte extends EntidadeJPA implements Serializable {

    public String titulo;

    @ManyToOne()
    public Usuario usuario;

    @Enumerated(EnumType.STRING)
    public Visibilidade visibilidade;

    @OneToMany(mappedBy = "arte", fetch = FetchType.LAZY)
    public List<Alteracao> alteracoes;


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
