package br.ufg.artattack.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "art-attack")
@Configuration
@Getter
public class Ambiente {


    private String chaveSecreta;

    private String nomeAplicacao;

    public void setNomeAplicacao(String valor) {

        this.nomeAplicacao = valor;
    }

    public void setChaveSecreta(String valor) {

        this.chaveSecreta = valor;
    }
}
