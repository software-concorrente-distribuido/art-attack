package br.ufg.artattack.rest;

import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
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
    ArteServico arteServico;


    @PostMapping("/criar")
    public ResponseEntity<ArteDTO> criarArte(@RequestBody ArteDTO arteDTO){

        return ResponseEntity.ok(arteServico.criarArteDoUsuarioLogado(arteDTO));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<ArteDTO>> listarArtes(){
        return ResponseEntity.ok(arteServico.findAllDTOs());
    }


    @GetMapping("/recuperarAlteracoes/{arteId}")
    public ResponseEntity<List<Alteracao>> recuperarAlteracoes(@PathVariable Long arteId){

        return ResponseEntity.ok(alteracaoRepositorio.findAlteracaoByArte_Id(arteId));
    }

    @GetMapping("/recuperarSnapshot/{arteId}")
    public ResponseEntity<byte[]> recuperarSnapshot(@PathVariable Long arteId){
        return ResponseEntity.ok(arteServico.arteRepositorio.obterSnapshotPorArte(arteId));
    }


}
