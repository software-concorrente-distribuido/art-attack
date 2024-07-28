package br.ufg.artattack.socketmiddleware;

import br.ufg.artattack.modelo.Integrante;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.servico.JWTServico;
import br.ufg.artattack.servico.SalaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AmqpSocketMiddleware implements ChannelInterceptor {

    @Autowired
    private JWTServico jwtServico;

    @Autowired
    SalaServico salaServico;

    @Override

    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            try{
                String[] parts = accessor.getDestination().split("\\/");

                String salaUUID = parts[3];
                String userId = parts[4];

                Object userIdAuth = ((HashMap)((UsernamePasswordAuthenticationToken)accessor.getUser()).getCredentials()).get("id");

                if(!userIdAuth.toString().equals(userId))
                    throw new Exception();

                Sala salaDoIntegrante = salaServico.obterSala(salaUUID);

                Integrante integrante = salaDoIntegrante.obterIntegrante(userId);

                if(integrante==null)
                    throw new Exception();


                integrante.onSubscribe.run();

            }catch (Exception e){
                //skip
            }

        }
    }

}
