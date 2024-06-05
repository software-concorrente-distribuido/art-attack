package br.ufg.artattack.rest;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/loops")
    public String handle(@Payload String mensagem){

        return "Recebido!" + mensagem;

    }

}
