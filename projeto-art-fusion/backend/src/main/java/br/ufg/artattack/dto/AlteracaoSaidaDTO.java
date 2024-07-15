package br.ufg.artattack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AlteracaoSaidaDTO {


    public Long id;

    public Long dataCriacao;

    public Object delta;

    public Long arteId;

    public Long usuarioId;

    public AlteracaoSaidaDTO(AlteracaoEntradaDTO payload) {
        this.arteId = payload.arteId;
        this.delta = payload.delta;
    }

    public String toJsonString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

}
