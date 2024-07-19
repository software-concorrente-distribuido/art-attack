package br.ufg.artattack.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter){
        var rabbit = new RabbitTemplate(connectionFactory);
        rabbit.setMessageConverter(jackson2JsonMessageConverter);
        return rabbit;
    }


    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(ALTERACOES_EXCHANGE_NAME,true,false);
    }

//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange("teste.test",false,false);
//    }
//
//    @Bean
//    public Queue filaTeste(){
//        return QueueBuilder.nonDurable("fila.teste")
//                .deadLetterExchange("teste.test-dlx")
//                .build();
//    }
//
//    @Bean
//    public FanoutExchange fanoutdlx(){
//        return new FanoutExchange("teste.test-dlx");
//    }
//
//    @Bean
//    public Queue filadlq(){
//        return QueueBuilder.nonDurable("fila.teste-dlq").build();
//    }
//
//
//    @Bean
//    public Binding bind(){
//        return BindingBuilder.bind(filaTeste()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binddlq(){
//        return BindingBuilder.bind(filadlq()).to(fanoutdlx());
//    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer(connectionFactory);
    }


}
