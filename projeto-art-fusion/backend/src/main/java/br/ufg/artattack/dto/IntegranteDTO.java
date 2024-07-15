package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Integrante;
import br.ufg.artattack.modelo.TipoPermissao;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class IntegranteDTO {

    public UsuarioDTO colaborador;

    public List<TipoPermissao> permissoes;


    public IntegranteDTO( Integrante integrante){
        colaborador = integrante.colaborador;
        permissoes = integrante.permissoes;
    }


}
