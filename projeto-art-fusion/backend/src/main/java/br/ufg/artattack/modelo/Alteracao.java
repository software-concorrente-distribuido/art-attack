package br.ufg.artattack.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity( name="alteracao")
public class Alteracao extends EntidadeJPA {


    public String alteracaoJson;

    @ManyToOne()
    public Arte arte;


}
