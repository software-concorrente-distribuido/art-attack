package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Alteracao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

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

    public AlteracaoSaidaDTO(Alteracao alteracao)  {
        ObjectMapper objectMapper = new ObjectMapper();
        this.id = alteracao.getId();
        this.arteId = alteracao.arte.getId();
        this.dataCriacao = alteracao.dataCriacao.getTime();
        try {
            this.delta = objectMapper.readValue(alteracao.getDelta(), HashMap.class);
        } catch (JsonProcessingException e) {
            this.delta = "";
        }
        this.usuarioId = alteracao.usuario.getId();
    }

    public String toJsonString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

}
