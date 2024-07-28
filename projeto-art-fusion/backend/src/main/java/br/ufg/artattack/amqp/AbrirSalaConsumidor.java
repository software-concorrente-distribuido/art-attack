package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

public class AbrirSalaConsumidor implements ChannelAwareMessageListener {

    private ServicoRabbitMQ servicoRabbitMQ;
    private String exchange;
    private String bindingKey;

    public AbrirSalaConsumidor(String exchange,String bindingKey, ServicoRabbitMQ servicoRabbitMQ){
        this.servicoRabbitMQ = servicoRabbitMQ;
        this.exchange =exchange;
        this.bindingKey = bindingKey;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        var rabbitTemplate = servicoRabbitMQ.getRabbitTemplate();
        try {
//            AlteracaoSaidaDTO alteracaoSaidaDTO = new ObjectMapper().readValue(new String(message.getBody()), AlteracaoSaidaDTO.class);

            rabbitTemplate.convertAndSend(
                    exchange,
                    bindingKey,
                    message,
                    msg->{
                        msg.getMessageProperties().setPriority(10);
                        return msg;
                    }
            );
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);


        } catch (Exception e) {
            //skip
        }
    }

}
