package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.exception.ProcessamentoException;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;
import java.util.Queue;

public class AbrirSalaConsumidor {

    private ServicoRabbitMQ servicoRabbitMQ;
    private RabbitAdmin rabbitAdmin;
    private String queue;


    public AbrirSalaConsumidor(String queue,ServicoRabbitMQ servicoRabbitMQ){
        this.servicoRabbitMQ = servicoRabbitMQ;
        this.rabbitAdmin  = servicoRabbitMQ.getRabbitAdmin();
        this.queue = queue;
    }

    public void handleMessage(AlteracaoSaidaDTO alteracaoSaidaDTO){

        try {
            //TODO

            //rabbitAdmin.enviarMensagensComPrioridadeParaFila(queue,alteracaoSaidaDTO);

        } catch (Exception e) {
            throw new ProcessamentoException(e);
        }

    }

}
