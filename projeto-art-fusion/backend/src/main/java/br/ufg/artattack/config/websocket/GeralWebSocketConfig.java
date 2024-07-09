package br.ufg.artattack.config.websocket;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class GeralWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //prefixo dos endpoints que o cliente vai receber mensagens
        config.enableSimpleBroker("/topic");

        //prefixo dos endpoints que o cliente vai enviar mensagens.
        config.setApplicationDestinationPrefixes("/envio");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // endpoint para conexão do socket. não tem nada a ver com os acima
        registry.addEndpoint("/artsocket").setAllowedOrigins("http://localhost:3000")
                .setAllowedOrigins("http://172.16.6.206:3000")
                .withSockJS();
    }

}