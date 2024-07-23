package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.exception.ProcessamentoException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;
import java.util.Queue;

//POJO
public class RabbitArtFusionConsumer {

    private SalaAbertaWrapper salaAbertaWrapper;

    private SimpMessagingTemplate simpMessagingTemplate;

    private String urlDestination;

    private Queue<AlteracaoSaidaDTO> cache = new LinkedList<>();

    public RabbitArtFusionConsumer(SalaAbertaWrapper salaAbertaWrapper, SimpMessagingTemplate simpMessagingTemplate){
        this.salaAbertaWrapper = salaAbertaWrapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.urlDestination = "/topic/alteracoes/"+ salaAbertaWrapper.salaNova.uuid + "/"+ salaAbertaWrapper.integranteRequerinte.colaborador.id;

    }


    public void handleMessage(AlteracaoSaidaDTO alteracaoSaidaDTO){


        try {

            simpMessagingTemplate.convertAndSend(urlDestination,alteracaoSaidaDTO.toJsonString());
        } catch (Exception e) {
            throw new ProcessamentoException(e);
        }
    }


}
