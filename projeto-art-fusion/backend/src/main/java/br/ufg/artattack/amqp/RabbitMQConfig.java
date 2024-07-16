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


//    @Bean
//    public Queue criaFila(){
//        return new Queue("alteracoes.geral",false);
//    }

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
    public FanoutExchange fanoutExchange(){

        return new FanoutExchange("teste.test",true,false);

    }

    @Bean
    public Queue filaTeste(){
        return QueueBuilder.durable("fila.teste").build();
    }

    @Bean
    public Binding bind(FanoutExchange fanoutExchange){
        return BindingBuilder.bind(filaTeste()).to(fanoutExchange);
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer(connectionFactory);
    }


}
