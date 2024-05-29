package br.ufg.artattack.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.io.File;

@Entity(name="snapshot")
public class Snapshot extends EntidadeJPA{

    File documento;

    @ManyToOne
    Arte arte;

}
