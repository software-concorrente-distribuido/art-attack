package br.ufg.artattack.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import org.hibernate.annotations.ColumnTransformer;


@Entity( name="alteracao")
public class Alteracao extends EntidadeJPA {

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String  delta;

    @ManyToOne()
    public Arte arte;


    @ManyToOne
    public Usuario usuario;

    public String getDelta() {
        return delta;
    }

    public JsonNode getJsonDelta() {
        try {
            return new ObjectMapper().readTree(delta);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void setDelta(Object delta) throws JsonProcessingException {
        if(delta instanceof  String dt){
            this.delta = dt;
            return;
        }

        this.delta = new ObjectMapper().writeValueAsString(delta);
    }
}
