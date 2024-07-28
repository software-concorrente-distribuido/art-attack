package br.ufg.artattack.socketmiddleware;

import br.ufg.artattack.dto.UsuarioDTO;
import br.ufg.artattack.exception.ErroEsperadoException;
import br.ufg.artattack.exception.ProcessamentoException;
import br.ufg.artattack.servico.JWTServico;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class JwtSocketMiddleware implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtSocketMiddleware.class);

    @Autowired
    private JWTServico jwtServico;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor!=null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("X-Authorization");

            logger.debug("X-Authorization: {}", authorization);

            if(authorization==null)
                throw new ErroEsperadoException("Não foi possível obter o token de autorização para conectar");

            String accessToken = authorization.get(0).split(" ")[1];
            Authentication authentication;
            HashMap<String,String> credentials;
            try {
                var usuarioDTO= jwtServico.obterUsuarioDTO(accessToken);

                credentials = new HashMap<>();
                credentials.put("username",usuarioDTO.email);
                credentials.put("id",usuarioDTO.id);

                authentication=new UsernamePasswordAuthenticationToken(usuarioDTO,credentials,List.of(new SimpleGrantedAuthority("ROLE_USUARIO_LOGADO")));

                SecurityContextHolder.getContext().setAuthentication(

                        authentication

                );

                accessor.setUser(authentication);
            } catch (JsonProcessingException e) {

                throw new ProcessamentoException(e);

            }catch (ErroEsperadoException e){

                credentials = new HashMap<>();

                //TODO trocar esse UUID por algo gerado pelo IP do requerinte

                credentials.put("username",UUID.randomUUID().toString());

                authentication = new UsernamePasswordAuthenticationToken(new UsuarioDTO(),null,List.of(new SimpleGrantedAuthority("ROLE_CONVIDADO")));

                accessor.setUser(authentication);
            }

        }

        return message;

    }


}
