package br.ufg.artattack.modelo;

import br.ufg.artattack.dto.UsuarioDTO;

import java.util.List;

public class Integrante{

    public UsuarioDTO colaborador;

    public List<TipoPermissao> permissoes;

    public boolean isInscrito;

    public Runnable onSubscribe;

    public Runnable onUnsubscribe;

    public Integrante(UsuarioDTO colaborador, List<TipoPermissao> permissoes) {
        this.colaborador = colaborador;
        this.permissoes = permissoes;
    }

}