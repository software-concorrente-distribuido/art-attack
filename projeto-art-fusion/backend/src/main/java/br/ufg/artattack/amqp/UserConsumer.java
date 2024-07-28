package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.SalaAbertaWrapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

//POJO
public class UserConsumer implements MessageListener {

    private SalaAbertaWrapper salaAbertaWrapper;

    private SimpMessagingTemplate simpMessagingTemplate;

    private String urlDestination;


    public UserConsumer(SalaAbertaWrapper salaAbertaWrapper, SimpMessagingTemplate simpMessagingTemplate){
        this.salaAbertaWrapper = salaAbertaWrapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.urlDestination = "/topic/alteracoes/"+ salaAbertaWrapper.salaNova.uuid + "/"+ salaAbertaWrapper.integranteRequerinte.colaborador.id;

    }

    @Override
    public void onMessage(Message message) {
        try {
            simpMessagingTemplate.convertAndSend(urlDestination,new String(message.getBody()));
        } catch (Exception e) {
            //skiip
        }
    }

}
