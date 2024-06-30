package br.ufg.artattack.modelo;

import jakarta.persistence.*;

@Entity(name = "compartilhamento")
public class Compartilhamento extends EntidadeJPA{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arte_id")
    public Arte arte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    public Usuario usuario;

    @Enumerated(EnumType.STRING)
    public TipoPermissao tipoPermissao;


}
