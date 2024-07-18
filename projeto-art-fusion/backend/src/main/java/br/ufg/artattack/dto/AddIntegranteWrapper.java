package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Integrante;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddIntegranteWrapper {

    public Integrante integrante;

    public boolean isNovoIntegrante;


}
