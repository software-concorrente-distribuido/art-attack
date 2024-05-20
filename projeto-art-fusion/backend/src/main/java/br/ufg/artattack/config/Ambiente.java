package br.ufg.artattack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "art-attack")
@Configuration
@Getter
@Setter
public class Ambiente {


    private String chaveSecreta;

    private String nomeAplicacao;

    private Boolean mvc;

}
