package br.ufg.artattack.config.websocket;

import br.ufg.artattack.rest.middleware.JwtSocketMiddleware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtSocketMiddleware jwtSocketMiddleware;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // endpoint para conexão do socket. não tem nada a ver com os acima
        registry.addEndpoint("/artsocket").setAllowedOrigins("http://localhost:3000")
                .withSockJS();
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
    }

}