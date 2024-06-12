package br.ufg.artattack.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlteracaoDTO {

    public Long id;

    public String dataCriacao;

    public Object delta;

    public Long arteId;

    public Long usuarioId;

    public String toJsonString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

}
