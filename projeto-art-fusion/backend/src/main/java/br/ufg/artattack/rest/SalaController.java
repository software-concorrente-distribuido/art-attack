package br.ufg.artattack.rest;

import br.ufg.artattack.amqp.AbrirSalaConsumidor;
import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.amqp.PropagarMensagensConsumidor;
import br.ufg.artattack.dto.AbrirSalaDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.servico.ArteServico;
import br.ufg.artattack.servico.SalaServico;
import br.ufg.artattack.servico.UsuarioServico;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/sala")
public class SalaController {


    @Autowired
    ArteServico arteServico;

    @Autowired
    SalaServico salaServico;

    @Autowired
    ServicoRabbitMQ servicoRabbitMQ;

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;


    @PostMapping("/abrir")
    public ResponseEntity<Sala> abrirSala(@RequestBody AbrirSalaDTO abrirSalaDTO) throws Exception {

        SalaAbertaWrapper salaAbertaWrapper = salaServico.abrirSala(abrirSalaDTO.arteId);

        String salaUsuarioQueueName = ServicoRabbitMQ.getSalaUsuarioQueueName(salaAbertaWrapper.salaNova.uuid,salaAbertaWrapper.integranteRequerinte.colaborador.id);

        String especificoBindingKey = ServicoRabbitMQ.getEspecificoBindingKey(salaAbertaWrapper);

        String salaGeralBindKey = ServicoRabbitMQ.getGeralBindingKey(salaAbertaWrapper.salaNova.arte.id);

        String queueArte = ServicoRabbitMQ.getArteQueueName(salaAbertaWrapper.salaNova.arte.id);

        //fila do usuario
        servicoRabbitMQ.createNonDurableQueueWithPriorities(salaUsuarioQueueName);

        //possibilidade de recepcionar mensagnes específicas pela especificoBindingKey
        servicoRabbitMQ.bindQueue(salaUsuarioQueueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME, especificoBindingKey);

        //possibilidade de receber mensagens gerais (da sala)
        servicoRabbitMQ.bindQueue(salaUsuarioQueueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME, salaGeralBindKey);

        /*
        Cria consumidor da fila do usuário.
        Este será responsável por retornar informações ao cliente por meio da binding key geral.
         */
        servicoRabbitMQ.createConsumer(salaUsuarioQueueName, new
                PropagarMensagensConsumidor(salaAbertaWrapper,simpMessagingTemplate));

        /*
        Cria consumidor da fila da arte.
        Este será responsável por enviar todas as alterações da fila da arte para o usuário por meio da binding key específica.
         */
//        servicoRabbitMQ.createReadOnlyConsumer(queueArte,
//                new AbrirSalaConsumidor( RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,especificoBindingKey,servicoRabbitMQ));


        return ResponseEntity.ok(salaAbertaWrapper.salaNova);

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
