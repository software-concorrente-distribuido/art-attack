package br.ufg.artattack.config.filtro;
import br.ufg.artattack.dto.UsuarioDTO;
import br.ufg.artattack.exception.ExcecaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import br.ufg.artattack.servico.JWTServico;
import org.springframework.http.HttpStatus;
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
        try{
            String authorization = obterJWT(request);

            if(authorization==null){
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(null,null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
                );
                filterChain.doFilter(request,response);
                return;
            }


                jwtServico.validarJWT(authorization);

                UsuarioDTO usuarioDTO = jwtServico.obterUsuarioDTO(authorization);

                SecurityContextHolder.getContext().setAuthentication(

                        new UsernamePasswordAuthenticationToken(usuarioDTO,null, List.of(new SimpleGrantedAuthority("ROLE_USUARIO_LOGADO")))

                );


            filterChain.doFilter(request,response);

        }catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setHeader("Content-type","application/json");
            response.getWriter().write(convertObjectToJson(new ExcecaoDTO(e)));
        }

    }



    private String obterJWT(HttpServletRequest request) {
        String  jwt = request.getHeader("Authorization");

        if(jwt!=null)
            return  jwt.replace("Bearer ", "");

        return null;

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
