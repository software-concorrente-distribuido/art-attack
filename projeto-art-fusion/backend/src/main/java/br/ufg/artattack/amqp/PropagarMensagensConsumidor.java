package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.exception.ProcessamentoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;
import java.util.Queue;

//POJO
public class PropagarMensagensConsumidor implements MessageListener {

    private SalaAbertaWrapper salaAbertaWrapper;

    private SimpMessagingTemplate simpMessagingTemplate;

    private String urlDestination;


    public PropagarMensagensConsumidor(SalaAbertaWrapper salaAbertaWrapper, SimpMessagingTemplate simpMessagingTemplate){
        this.salaAbertaWrapper = salaAbertaWrapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.urlDestination = "/topic/alteracoes/"+ salaAbertaWrapper.salaNova.uuid + "/"+ salaAbertaWrapper.integranteRequerinte.colaborador.id;

    }

    @Override
    public void onMessage(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String alteracaoString = new String(message.getBody());
        try {
            AlteracaoSaidaDTO alteracaoSaidaDTO = objectMapper.readValue(alteracaoString, AlteracaoSaidaDTO.class);
            simpMessagingTemplate.convertAndSend(urlDestination,alteracaoSaidaDTO.toJsonString());
        } catch (Exception e) {
            //skiip
        }
    }

}
