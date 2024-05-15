package br.ufg.artattack.rest;

import br.ufg.artattack.modelo.Cliente;
import br.ufg.artattack.rest.dto.ClienteDTO;
import br.ufg.artattack.servico.ClienteServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
public class ClienteProjectionController  {

    @Autowired
    ClienteServico clienteServico;
    @GetMapping("/{id}")
    public ResponseEntity conseguirClienteDTO(@PathVariable Long id){
    var cliente = clienteServico.clienteRepositorio.findById(id).orElse(new Cliente());
    return ResponseEntity.ok(new ClienteDTO(cliente));
    }


}
