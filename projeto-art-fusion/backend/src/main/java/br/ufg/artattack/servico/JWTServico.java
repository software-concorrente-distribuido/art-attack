package br.ufg.artattack.servico;

import br.ufg.artattack.config.Ambiente;
import br.ufg.artattack.rest.dto.ClienteDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTServico {

    @Autowired
    Ambiente ambiente;


    public String gerarJWT(ClienteDTO clienteDTO){

        try {
            Algorithm algoritmo = Algorithm.HMAC256(ambiente.getChaveSecreta());
            return  JWT.create()
                    .withIssuer(ambiente.getNomeAplicacao())
                    .withSubject(clienteDTO.email)
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar JWT",exception);
        }

    }

    public String obterSubject(String token){
        try {

            return JWT.require(Algorithm.HMAC256(ambiente.getChaveSecreta()))
                    .withIssuer(ambiente.getNomeAplicacao())
                    .build()
                    .verify(token)
                    .getSubject();


        }catch (JWTVerificationException jwtVerificationException){

            throw new JWTVerificationException("Token invalido ou expirado"+ " "+ jwtVerificationException.getMessage());
        }
    }


    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }

}
