package br.ufg.artattack.amqp;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ALTERACOES_EXCHANGE_NAME = "alteracoes";


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaRabbitAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        var rabbit = new RabbitTemplate(connectionFactory);
        rabbit.setMessageConverter(new SimpleMessageConverter());
        return rabbit;
    }


    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(ALTERACOES_EXCHANGE_NAME,true,false);
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer(connectionFactory);
    }

    @Bean
    public Channel rabbitChannel(ConnectionFactory connectionFactory) throws Exception {
        return connectionFactory.createConnection().createChannel(true);
    }


}
