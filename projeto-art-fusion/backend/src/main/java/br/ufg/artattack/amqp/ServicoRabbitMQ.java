package br.ufg.artattack.amqp;

import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServicoRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer listenerContainer;

    @Autowired
    private Channel channel;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ArteRepositorio arteRepositorio;

    @Autowired
    AlteracaoRepositorio alteracaoRepositorio;


    private final Map<String, SimpleMessageListenerContainer> containers = new HashMap<>();

    public void createArteQueueAndConsumer(Long arteId){

        String queueName = ServicoRabbitMQ.getArteQueueName(arteId);

        this.createDurableQueue(queueName);

        this.bindQueue(queueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,ServicoRabbitMQ.getGeralBindingKey(arteId));

        this.stopConsumer(queueName);

        this.createConsumer(queueName,new ArteConsumer(
                alteracaoRepositorio,
                arteRepositorio,
                usuarioRepositorio
        ));

    }


    public static String getGeralBindingKey(Long arteId){
        return arteId.toString()+ ".geral";
    }

    public static String getEspecificoBindingKey(Long arteId, String integranteiId){

        return arteId + ".especifico." + integranteiId;

    }

    public static String getEspecificoBindingKey(SalaAbertaWrapper salaAbertaWrapper){
        return getEspecificoBindingKey(salaAbertaWrapper.salaNova.arte.id, salaAbertaWrapper.integranteRequerinte.colaborador.id);
    }

    public static String getSalaUsuarioQueueName(String salaUuid, String integranteId) {

        return salaUuid+"."+ integranteId;

    }


    public String getArteIdFromEspecificoBindingKey(String especificoBindingKey){

        return  especificoBindingKey.split("\\.")[0];

    }

    public boolean queueExists(String queueName){
        return rabbitAdmin.getQueueProperties(queueName) != null;
    }


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

    public String createDurableQueue(String queueName) {
        rabbitAdmin.declareQueue(new Queue(queueName, true));
        return "Queue created: " + queueName;
    }

    public String createNonDurableQueueWithPriorities(String queueName){
        rabbitAdmin.declareQueue(QueueBuilder.nonDurable(queueName).withArgument("x-max-priority", 2).build());
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
    public String purgeQueue(String queueName) {
        rabbitAdmin.purgeQueue(queueName);
        return "Queue purged: " + queueName;
    }

    public String deleteExchange(String exchangeName) {
        rabbitAdmin.deleteExchange(exchangeName);
        return "Exchange deleted: " + exchangeName;
    }

    public String sendMessage(String exchangeName,String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        return "Message sent to exchange: " + exchangeName + " with routing key: " + routingKey;
    }


    public String createConsumer(String queueName, MessageListener listener) {
        SimpleMessageListenerContainer listenerRuntime = new SimpleMessageListenerContainer();
        listenerRuntime.setConnectionFactory(rabbitAdmin.getRabbitTemplate().getConnectionFactory());
        listenerRuntime.addQueues(new Queue(queueName));
        listenerRuntime.setMessageListener(listener);
        listenerRuntime.start();

        containers.put(queueName, listenerRuntime);

        return "Consumer created for queue: " + queueName;
    }


    public void stopConsumer(String queueName) {
        SimpleMessageListenerContainer container = containers.get(queueName);
        if (container != null) {
            container.stop();
            containers.remove(queueName);
        }
    }

    public String createReadOnlyConsumer(String queueName, MessageListener listenerAdapter) {

        SimpleMessageListenerContainer listenerRuntime = new SimpleMessageListenerContainer();
        listenerRuntime.setConnectionFactory(rabbitAdmin.getRabbitTemplate().getConnectionFactory());
        listenerRuntime.addQueues(new Queue(queueName));
        listenerRuntime.setMessageListener(listenerAdapter);
        listenerRuntime.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        listenerRuntime.start();

        containers.put(queueName, listenerRuntime);

        return "Consumer created for queue: " + queueName;
    }


    public static String getArteQueueName(String id) {
        return "arte."+id;
    }

    public static String getArteQueueName(Long id) {
        return "arte."+id.toString();
    }

    public RabbitAdmin getRabbitAdmin() {
        return rabbitAdmin;
    }


    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }
}
