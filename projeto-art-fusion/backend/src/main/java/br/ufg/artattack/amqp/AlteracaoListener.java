package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class AlteracaoListener {


    @RabbitListener(queues = "fila.teste")
    public void recebeMensagem(AlteracaoSaidaDTO msg){

        System.out.println(msg);
    }

}
