package br.ufg.artattack.rest;

import br.ufg.artattack.dto.AbrirSalaDTO;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.servico.SalaServico;
import br.ufg.artattack.util.RandomWordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("api/sala")
public class SalaController {


    @Autowired
    SalaServico salaServico;

    @PostMapping("/abrir")
    public ResponseEntity<Sala> abrirSala(@RequestBody AbrirSalaDTO abrirSalaDTO) throws Exception {

        Sala sala = salaServico.criarSala(abrirSalaDTO.arteId);

        return ResponseEntity.ok(sala);
    }

    @GetMapping("/obterPorArte/{arteId}")
    public ResponseEntity<Sala> obterSalaPorArte(@PathVariable String arteId) throws Exception {
        return ResponseEntity.ok(salaServico.obterSalaPorIdArte(arteId));
    }

    @GetMapping("/obter/{uui}")
    public ResponseEntity<Sala> obterSala(@PathVariable String uui){
        return ResponseEntity.ok(salaServico.obterSala(uui));
    }

    @PostMapping("/fechar/{uuid}")
    public ResponseEntity<Sala> fecharSala(@PathVariable String uuid){
        return ResponseEntity.ok(salaServico.fecharSala(uuid));

    }






}
