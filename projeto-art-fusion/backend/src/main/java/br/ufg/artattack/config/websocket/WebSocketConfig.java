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

        String ip = "18.228.226.210";
        String dnsAmazonResolver = "c2-18-228-226-210.sa-east-1.compute.amazonaws.com";


        // endpoint para conexão do socket. não tem nada a ver com os acima
        registry.addEndpoint("/artsocket").setAllowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8089",
                        "http://localhost:8080",
                        "http://"+ip,
                        "http://"+ip+":8089",
                        "http://"+ip+":3000",
                        "http://"+dnsAmazonResolver,
                        "http://"+dnsAmazonResolver+":8089",
                        "http://"+dnsAmazonResolver+":3000"
                        )
                .withSockJS()
        ;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(60 * 1000)
                .setSendBufferSizeLimit( 50*(512 * 1024))
                .setMessageSizeLimit(50*(512 * 1024)/2);
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