package br.ufg.artattack.rest;

import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.dto.CompartilhamentoEntradaDTO;
import br.ufg.artattack.dto.CompartilhamentoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.servico.ArteServico;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Message;

import java.util.List;

@RestController
@RequestMapping("/api/arte")
public class ArteController {

    @Autowired
    AlteracaoRepositorio alteracaoRepositorio;

    @Autowired
    ArteServico arteServico;

    @Autowired
    ServicoRabbitMQ servicoRabbitMQ;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/criar")
    public ResponseEntity<ArteDTO> criarArte(@RequestBody ArteDTO arteDTO){

        ArteDTO art = arteServico.criarArteDoUsuarioLogado(arteDTO);

        String queueName = "arte."+art.id.toString();

        servicoRabbitMQ.createDurableQueue(queueName);

        servicoRabbitMQ.bindQueue(queueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,art.id.toString()+".geral");

        return ResponseEntity.ok(art);
    }

    @PostMapping("/compartilhar")
    public ResponseEntity<List<CompartilhamentoSaidaDTO>> compartilharArte(@RequestBody CompartilhamentoEntradaDTO compartilhamentoEntradaDTO) throws Exception {
        return ResponseEntity.ok(arteServico.compartilharParaAlguem(compartilhamentoEntradaDTO));
    }

    @GetMapping("/compartilhadasPorUsuario/{usuarioId}")
    public ResponseEntity compartilhadasAMim(@PathVariable Long usuarioId){

        return ResponseEntity.ok(arteServico.obterCompartilhadasPorUsuario(usuarioId));
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
