package br.ufg.artattack.rest.filtro;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.ufg.artattack.repositorio.ClienteRepositorio;
import br.ufg.artattack.servico.JWTServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroAPI extends OncePerRequestFilter {

    JWTServico jwtServico;

    ClienteRepositorio repositorio;

    FiltroAPI(JWTServico jwtServico, ClienteRepositorio repositorio){
        this.jwtServico = jwtServico;
        this.repositorio = repositorio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = obterJWT(request);

        String subject = "";

        if(authorization!=null) {

            subject = jwtServico.obterSubject(authorization);

            var usuario = repositorio.findByEmail(subject);

            SecurityContextHolder.getContext().setAuthentication(

                    new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities())

            );

        }

        filterChain.doFilter(request,response);
    }



    private String obterJWT(HttpServletRequest request) {
        String  jwt = request.getHeader("Authorization");

        if(jwt!=null)
            return  jwt.replace("Bearer ", "");

        return null;

    }

}
