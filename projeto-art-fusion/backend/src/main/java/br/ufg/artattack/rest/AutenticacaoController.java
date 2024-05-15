package br.ufg.artattack.rest;

import br.ufg.artattack.modelo.Cliente;
import br.ufg.artattack.rest.dto.AutenticacaoDTO;
import br.ufg.artattack.rest.dto.ClienteDTO;
import br.ufg.artattack.servico.ClienteServico;
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
    ClienteServico clienteServico;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTServico jwtServico;


    @PostMapping("/login")
    public ResponseEntity conseguirToken(@RequestBody @Valid AutenticacaoDTO authDTO){

        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getSenha()));

        if(auth.getPrincipal() instanceof Cliente cliente)
            return ResponseEntity.ok().body(jwtServico.gerarJWT(new ClienteDTO(cliente)));
        else
            return ResponseEntity.internalServerError().build();
    }


}
