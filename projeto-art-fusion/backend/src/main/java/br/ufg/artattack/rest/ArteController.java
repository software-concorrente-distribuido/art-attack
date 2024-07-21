package br.ufg.artattack.rest;

import br.ufg.artattack.dto.*;
import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.dto.CompartilhamentoEntradaDTO;
import br.ufg.artattack.dto.CompartilhamentoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.TipoPermissao;
import br.ufg.artattack.modelo.Visibilidade;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.servico.ArteServico;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import br.ufg.artattack.servico.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    UsuarioServico usuarioServico;


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

    @GetMapping("/compartilhadasMinhas")
    public ResponseEntity<List<CompartilhamentoSaidaDTO>> compartilhadasAMim(){
        return ResponseEntity.ok(arteServico.obterCompartilhadasAMim());
    }




    @GetMapping("/recuperarSnapshot/{arteId}")
    public ResponseEntity<byte[]> recuperarSnapshot(@PathVariable Long arteId){
        return ResponseEntity.ok(arteServico.arteRepositorio.obterSnapshotPorArte(arteId));
    }

    @PostMapping("/editarTitulo")
    public ResponseEntity<ArteDTO> editarTitulo(@RequestBody EditarNomeArteDTO editarNomeArteDTO){
        return ResponseEntity.ok(arteServico.editarTituloArte(editarNomeArteDTO.titulo,editarNomeArteDTO.arteId));
    }

    @PostMapping("/compartilharPorEmail")
    public ResponseEntity<List<CompartilhamentoSaidaDTO>> compartilharArtePorEmail(@RequestBody CompartilhamentoPorEmailEntradaDTO compPorEMailDTO) {
        var usuario = usuarioServico.getUsuarioPeloEmail(compPorEMailDTO.usuarioBeneficiadoEmail);

        return ResponseEntity.ok(arteServico.compartilharParaAlguem(
                new CompartilhamentoEntradaDTO(compPorEMailDTO.arteId,usuario.getId(),compPorEMailDTO.permissoes)));
    }

    @GetMapping()
    public ResponseEntity<List<ArteDTO>> getMinhasArtes(){
        return ResponseEntity.ok(arteServico.obterMinhasArtes());
    }

    @GetMapping("/todasQueTenhoAcesso")
    public ResponseEntity<List<ArteDTO>> getArtesQueTenhoAcesso(){

            List<ArteDTO> resposta = new ArrayList<>();
            resposta.addAll(arteServico.obterMinhasArtes());
            resposta.addAll(arteServico.obterCompartilhadasAMim().stream().map(c->c.arteDTO).toList());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/editarVisibilidade")
    public ResponseEntity<ArteDTO> editarVisibilidade(@RequestBody EditarVisibilidadeDTO editarVisibilidadeDTO){
        return ResponseEntity.ok(arteServico.editarVisibilidade(editarVisibilidadeDTO));
    }

    @GetMapping("/compartilhadasPorUsuario/{usuarioId}")
    public ResponseEntity listarCompartilahdasPorUsuario(@PathVariable Long usuarioId){
        return ResponseEntity.ok(arteServico.obterCompartilhadasAoUsuario(usuarioId));
    }

    @PostMapping("/obterPorUsuarioVisibilidade")
    public ResponseEntity<List<ArteDTO>> obterPorVisibilidade(@RequestBody ArtePorUsrVisibilidadeDTO dto){
        return ResponseEntity.ok(arteServico.obterPorVisibilidade(dto));
    }

}
