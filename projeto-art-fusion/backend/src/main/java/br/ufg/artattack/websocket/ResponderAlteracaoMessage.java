package br.ufg.artattack.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponderAlteracaoMessage {
    String resultado;

    public ResponderAlteracaoMessage(){}

    public ResponderAlteracaoMessage(RegistrarAlteracaoMessage registrarAlteracaoMessage){

        this.resultado = registrarAlteracaoMessage.json;

    }

}
