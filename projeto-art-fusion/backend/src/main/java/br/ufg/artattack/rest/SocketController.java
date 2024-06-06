package br.ufg.artattack.rest;

import br.ufg.artattack.modelo.Alteracao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/alteracoes")
    public String handle(@Payload String alteracaoJson)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Alteracao alteracao;
        try {
            alteracao =  objectMapper.readValue(alteracaoJson, Alteracao.class);
        } catch (JsonProcessingException e) {
            return "Mensagem de alteração inválida!";
        }


        return "Recebido!" ;

    }

}
