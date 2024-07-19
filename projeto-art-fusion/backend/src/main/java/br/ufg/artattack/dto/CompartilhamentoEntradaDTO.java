package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.TipoPermissao;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CompartilhamentoEntradaDTO {

    public Long arteId;

    public Long usuarioBeneficiadoId;

    public List<TipoPermissao> permissoes;

}
