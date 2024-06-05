package br.ufg.artattack.config;

import br.ufg.artattack.servico.JWTServico;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthConfig.class);



    @Autowired
    private JWTServico jwtServico;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                            List<String> authorization = accessor.getNativeHeader("X-Authorization");
                            logger.debug("X-Authorization: {}", authorization);

                            String accessToken = authorization.get(0).split(" ")[1];
                            Authentication authentication;
                            try {
                        var usuarioDTO= jwtServico.obterUsuarioDTO(accessToken);

                        authentication=new UsernamePasswordAuthenticationToken(usuarioDTO,null, List.of(new SimpleGrantedAuthority("ROLE_USUARIO_LOGADO")));

                        accessor.setUser(authentication);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                return message;
            }
        });
    }
}