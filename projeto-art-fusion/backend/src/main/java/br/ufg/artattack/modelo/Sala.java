package br.ufg.artattack.modelo;

import br.ufg.artattack.dto.ArteDTO;

import java.util.UUID;

public class Sala {

    public String titulo;
    public  String uuid;

    public ArteDTO arte;

    public Sala(UUID uuid, Arte arte){

        this.titulo = arte.titulo;

        this.uuid = uuid.toString();

    }
    public Sala(String uuid, Arte arte){

        this.titulo = arte.titulo;
        this.arte = new ArteDTO(arte);

        this.uuid = uuid;

    }

}
