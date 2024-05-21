package br.ufg.artattack.servico;

import br.ufg.artattack.config.Ambiente;
import br.ufg.artattack.dto.UsuarioDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class JWTServico {

    @Autowired
    Ambiente ambiente;

    public String gerarJWT(UsuarioDTO usuarioDTO){

        try {
            Algorithm algoritmo = Algorithm.HMAC256(ambiente.getChaveSecreta());

            return  JWT.create()
                    .withIssuer(ambiente.getNomeAplicacao())
                    .withSubject(usuarioDTO.email)
                    .withExpiresAt(dataExpiracao())
                    .withPayload(Map.of("usuario",new ObjectMapper().writeValueAsString(usuarioDTO)))
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar JWT",exception);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void validarJWT(String jwt) throws JWTVerificationException{

        JWT.require(Algorithm.HMAC256(ambiente.getChaveSecreta()))
                .withIssuer(ambiente.getNomeAplicacao())
                .build()
                .verify(jwt);
    }



    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }

    public String obterSubject(String authorization) throws JWTDecodeException {

            return JWT.decode(authorization).getClaim("sub").asString();

    }

    public UsuarioDTO obterUsuarioDTO(String jwt) throws JsonProcessingException {

        this.validarJWT(jwt);

        String usuarioDTOJSON = "";

        usuarioDTOJSON = JWT.decode(jwt).getClaim("usuario").as(String.class);

        if(usuarioDTOJSON == null){
            throw new RuntimeException("Não foi possível encontrar o objeto usuário no payload do JWT!");
        }

        return  new ObjectMapper().readValue(usuarioDTOJSON,UsuarioDTO.class);

    }

}
