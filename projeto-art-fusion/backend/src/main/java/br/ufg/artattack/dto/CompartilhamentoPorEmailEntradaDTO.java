package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.TipoPermissao;

import java.util.List;

public class CompartilhamentoPorEmailEntradaDTO {

    public Long arteId;

    public String usuarioBeneficiadoEmail;

    public List<TipoPermissao> permissoes;

}
