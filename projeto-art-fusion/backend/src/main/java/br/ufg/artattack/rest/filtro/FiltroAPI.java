package br.ufg.artattack.rest.filtro;
import br.ufg.artattack.rest.dto.UsuarioDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import br.ufg.artattack.servico.JWTServico;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroAPI extends OncePerRequestFilter {

    JWTServico jwtServico;

    UsuarioRepositorio repositorio;

    FiltroAPI(JWTServico jwtServico, UsuarioRepositorio repositorio){
        this.jwtServico = jwtServico;
        this.repositorio = repositorio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = obterJWT(request);

        if(authorization!=null) {

            jwtServico.validarJWT(authorization);

            UsuarioDTO usuarioDTO = jwtServico.obterUsuarioDTO(authorization);

            SecurityContextHolder.getContext().setAuthentication(

                    new UsernamePasswordAuthenticationToken(usuarioDTO,null, List.of(new SimpleGrantedAuthority("ROLE_USUARIO_LOGADO")))

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
