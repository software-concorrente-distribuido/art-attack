package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.exception.ProcessamentoException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class AlteracaoListener {


//    @RabbitListener(queues = "fila.teste")
//    public void recebeMensagem(AlteracaoSaidaDTO msg){
//
//        System.out.println("A tentar::: " + msg.toJsonString());
//
//        if(msg.toJsonString().contains("erro")){
//            throw new ProcessamentoException(msg.arteId.toString());
//        }
//
//    }

}
