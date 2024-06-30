package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.TipoPermissao;

import java.util.List;

public class CompartilhamentoEntradaDTO {

    public Long arteId;

    public Long usuarioBeneficiadoId;

    public List<TipoPermissao> permissoes;

}
