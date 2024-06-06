package br.ufg.artattack.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcecaoDTO {
    String mensagem;
    String causa;
    public ExcecaoDTO(Exception e){
        this.mensagem = e.getMessage();
        this.causa = e.getCause()!=null? e.getCause().toString() : e.getClass().getSimpleName();
    }
}
