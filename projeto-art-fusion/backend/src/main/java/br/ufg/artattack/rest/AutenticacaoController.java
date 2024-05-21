package br.ufg.artattack.rest;

import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.dto.LoginRequestDTO;
import br.ufg.artattack.dto.UsuarioDTO;
import br.ufg.artattack.servico.UsuarioServico;
import br.ufg.artattack.servico.JWTServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTServico jwtServico;


    @PostMapping("/login")
    public ResponseEntity conseguirToken(@RequestBody @Valid LoginRequestDTO authDTO){

        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getSenha()));

        if(auth.getPrincipal() instanceof Usuario usuario)
            return ResponseEntity.ok().body(jwtServico.gerarJWT(new UsuarioDTO(usuario)));
        else
            return ResponseEntity.internalServerError().build();
    }


}
