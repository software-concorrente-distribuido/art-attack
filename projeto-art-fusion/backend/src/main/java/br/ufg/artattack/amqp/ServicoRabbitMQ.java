package br.ufg.artattack.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicoRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer listenerContainer;

    public String createExchange( String exchangeName, String type) {
        Exchange exchange;
        if ("direct".equalsIgnoreCase(type)) {
            exchange = new DirectExchange(exchangeName);
        } else if ("topic".equalsIgnoreCase(type)) exchange = new TopicExchange(exchangeName);
        else if ("fanout".equalsIgnoreCase(type)) {
            exchange = new FanoutExchange(exchangeName);
        } else {
            return "Invalid exchange type";
        }
        rabbitAdmin.declareExchange(exchange);
        return "Exchange created: " + exchangeName;
    }

    public String createQueue( String queueName) {
        rabbitAdmin.declareQueue(new Queue(queueName, true));
        return "Queue created: " + queueName;
    }

    public String bindQueue(String queueName,String exchangeName,String routingKey) {
        rabbitAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null));
        return "Queue bound to exchange: " + exchangeName + " with routing key: " + routingKey;
    }

    public String deleteQueue(String queueName) {
        rabbitAdmin.deleteQueue(queueName);
        return "Queue deleted: " + queueName;
    }

    public String deleteExchange(String exchangeName) {
        rabbitAdmin.deleteExchange(exchangeName);
        return "Exchange deleted: " + exchangeName;
    }

    public String sendMessage(String exchangeName,String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        return "Message sent to exchange: " + exchangeName + " with routing key: " + routingKey;
    }

    public String createConsumer(String queueName) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(new Object() {
            public void handleMessage(String message) {
                System.out.println("Received message: " + message);
            }
        });
        listenerContainer.addQueues(new Queue(queueName));
        listenerContainer.setMessageListener(listenerAdapter);
        listenerContainer.start();
        return "Consumer created for queue: " + queueName;
    }
}
