package br.ufg.artattack.rest;


import br.ufg.artattack.websocket.RegistrarAlteracaoMessage;
import br.ufg.artattack.websocket.ResponderAlteracaoMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketAlteracoesController {

    @MessageMapping("/alteracao")
        public String hadleSocket(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "-->" + message + "<--" + " "+ System.currentTimeMillis();
    }
}
