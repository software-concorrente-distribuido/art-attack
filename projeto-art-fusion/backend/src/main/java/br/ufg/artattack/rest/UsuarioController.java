package br.ufg.artattack.rest;

import br.ufg.artattack.config.AutenticacaoConfiguracoes;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.rest.dto.UsuarioDTO;
import br.ufg.artattack.servico.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServico usuarioServico;


    @GetMapping("/{id}")
    public ResponseEntity conseguirUsuarioDTO(@PathVariable Long id){
    var cliente = usuarioServico.usuarioRepositorio.findById(id).orElse(new Usuario());
    return ResponseEntity.ok(new UsuarioDTO(cliente));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody Usuario usuario){

        Usuario usrDB = null;
        try {
            usrDB = usuarioServico.criarUsuario(usuario);
            return ResponseEntity.ok(new UsuarioDTO(usrDB));
        } catch (ServerException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/isAlive")
    public ResponseEntity<UsuarioDTO> ping(){

        return ResponseEntity.ok(usuarioServico.getUsuarioLogadoDTO());

    }


}
