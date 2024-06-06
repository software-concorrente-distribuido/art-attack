package br.ufg.artattack.rest;

import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.servico.ArteServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arte")
public class ArteController {

    @Autowired
    AlteracaoRepositorio alteracaoRepositorio;

    @Autowired
    ArteRepositorio arteRepositorio;



    @Autowired
    ArteServico arteServico;


    @PostMapping("/criar")
    public ResponseEntity criarArte(@RequestBody ArteDTO arteDTO){

        return ResponseEntity.ok(arteServico.criarArteDoUsuarioLogado(arteDTO));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<ArteDTO>> listarArtes(){

        List<Arte> arteList = arteRepositorio.findAll();


        return ResponseEntity.ok(arteList.stream().map(ArteDTO::new).toList());
    }


    @GetMapping("/recuperarAlteracoes/{arteId}")
    public ResponseEntity recuperarAlteracoes(@RequestParam Long arteId){

        return ResponseEntity.ok(alteracaoRepositorio.findAlteracaoByArte_Id(arteId));
    }


}
