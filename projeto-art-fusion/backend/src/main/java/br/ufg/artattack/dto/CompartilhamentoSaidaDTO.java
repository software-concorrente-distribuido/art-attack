package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Compartilhamento;

public class CompartilhamentoSaidaDTO {

    public Long id;

    public ArteDTO arteCompartilhada;

    public UsuarioDTO usuarioBeneficiado;

    public String tipoPermissao;

    public CompartilhamentoSaidaDTO(Compartilhamento compartilhamento) {
        this.id = compartilhamento.getId();
        this.arteCompartilhada = new ArteDTO(compartilhamento.arte);
        usuarioBeneficiado = new UsuarioDTO(compartilhamento.usuario);
        this.tipoPermissao = compartilhamento.tipoPermissao.toString();
    }
}
