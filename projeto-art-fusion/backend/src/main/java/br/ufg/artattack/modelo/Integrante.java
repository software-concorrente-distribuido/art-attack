package br.ufg.artattack.modelo;

import br.ufg.artattack.dto.UsuarioDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Integrante{

    public UsuarioDTO colaborador;

    public List<TipoPermissao> permissoes;

    public CopyOnWriteArrayList<String> mensagensBuffer;

    public Integrante(UsuarioDTO colaborador, List<TipoPermissao> permissoes) {
        this.colaborador = colaborador;
        this.permissoes = permissoes;
        this.mensagensBuffer = new CopyOnWriteArrayList<>();
    }

    public Integrante(UsuarioDTO colaborador, List<TipoPermissao> permissoes, CopyOnWriteArrayList<String> mensagensBuffer) {
        this.colaborador = colaborador;
        this.permissoes = permissoes;
        this.mensagensBuffer = mensagensBuffer;
    }
}