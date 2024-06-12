package br.ufg.artattack.rest;

import br.ufg.artattack.exception.ExcecaoDTO;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.dto.UsuarioDTO;
import br.ufg.artattack.servico.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioServico usuarioServico;


    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listar(){

        LinkedList<UsuarioDTO> usuarioDTOs = new LinkedList<>(usuarioServico.usuarioRepositorio.findAll().stream().map(UsuarioDTO::new).toList());

        return ResponseEntity.ok(usuarioDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity conseguirUsuarioDTO(@PathVariable Long id){
    var cliente = usuarioServico.usuarioRepositorio.findById(id).orElse(null);

    if(cliente==null){
        throw new RuntimeException("Usuario não encontrado no banco!");
    }

    return ResponseEntity.ok(new UsuarioDTO(cliente));
    }

    @PostMapping("/criar")
    public ResponseEntity createUser(@RequestBody Usuario usuario){
        System.out.println("************************* Chamando");
        Usuario usrDB = null;
        try {
            usrDB = usuarioServico.criarUsuario(usuario);
            return ResponseEntity.ok(new UsuarioDTO(usrDB));
        } catch (ServerException e) {
            return ResponseEntity.badRequest().body(new ExcecaoDTO(e));
        }


    }

    @GetMapping("/isAlive")
    public ResponseEntity<UsuarioDTO> ping(){

        return ResponseEntity.ok(usuarioServico.getUsuarioLogadoDTO());

    }
    @PostMapping("/isAlive")
    public ResponseEntity<UsuarioDTO> pingPost(){

        return ResponseEntity.ok(usuarioServico.getUsuarioLogadoDTO());

    }


}
