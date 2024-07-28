package br.ufg.artattack.config.websocket;

import br.ufg.artattack.socketmiddleware.AmqpSocketMiddleware;
import br.ufg.artattack.socketmiddleware.JwtSocketMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtSocketMiddleware jwtSocketMiddleware;

    @Autowired
    private AmqpSocketMiddleware amqpSocketMiddleware;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {


        // endpoint para conexão do socket. não tem nada a ver com os acima
        registry.addEndpoint("/artsocket").setAllowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8089",
                        "http://localhost:8080",
                        "http://52.67.57.216:8089",
                        "http://52.67.57.216",
                        "http://ec2-52-67-57-216.sa-east-1.compute.amazonaws.com",
                        "http://ec2-52-67-57-216.sa-east-1.compute.amazonaws.com:8089",
                        "http://ec2-52-67-57-216.sa-east-1.compute.amazonaws.com:3000",
                        "http://ec2-52-67-57-216.sa-east-1.compute.amazonaws.com:8080"
                        )
                .withSockJS()
                .setHttpMessageCacheSize(1024000)
        ;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(60 * 1000)
                .setSendBufferSizeLimit(200 * 1024 * 1024)
                .setMessageSizeLimit(200 * 1024 * 1024);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //prefixo dos endpoints que o cliente vai enviar mensagens.
        config.setApplicationDestinationPrefixes("/envio");

        //prefixo dos endpoints que o cliente vai receber mensagens
        config.enableSimpleBroker("/topic");

    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtSocketMiddleware);
        registration.interceptors(amqpSocketMiddleware);
    }

}