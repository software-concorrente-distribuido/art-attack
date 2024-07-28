package br.ufg.artattack.rest;

import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.amqp.UserConsumer;
import br.ufg.artattack.dto.*;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.servico.ArteServico;
import br.ufg.artattack.servico.SalaServico;
import br.ufg.artattack.servico.UsuarioServico;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

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

    @Autowired
    AlteracaoRepositorio alteracaoRepositorio;


    @PostMapping("/abrir")
    public ResponseEntity<Sala> abrirSala(@RequestBody AbrirSalaDTO abrirSalaDTO) throws Exception {

        SalaAbertaWrapper salaAbertaWrapper = salaServico.abrirSala(abrirSalaDTO.arteId);

        String salaUsuarioQueueName = ServicoRabbitMQ.getSalaUsuarioQueueName(salaAbertaWrapper.salaNova.uuid,salaAbertaWrapper.integranteRequerinte.colaborador.id);

        String especificoBindingKey = ServicoRabbitMQ.getEspecificoBindingKey(salaAbertaWrapper);

        String geralBindKey = ServicoRabbitMQ.getGeralBindingKey(salaAbertaWrapper.salaNova.arte.id);

        //fila do usuario
        servicoRabbitMQ.createNonDurableQueueWithPriorities(salaUsuarioQueueName);

        //possibilidade de recepcionar mensagnes específicas pela especificoBindingKey
        servicoRabbitMQ.bindQueue(salaUsuarioQueueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME, especificoBindingKey);

        //possibilidade de recepcionar mensagnes gerais pela chave geral
        servicoRabbitMQ.bindQueue(salaUsuarioQueueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME, geralBindKey);

        /*
        Cria consumidor da fila do usuário.
        Este será responsável por retornar informações ao cliente por meio da binding key geral.
        */
        salaAbertaWrapper.integranteRequerinte.onSubscribe = ()->{

            salaAbertaWrapper.integranteRequerinte.isInscrito = true;

            List<Alteracao> alteracoes = alteracaoRepositorio.findAlteracaoByArte_Id(salaAbertaWrapper.salaNova.arte.id);

            var containers = groupArray(alteracoes.stream().map(AlteracaoSaidaDTO::new).toList(),1500);

            containers.forEach(container->{

                try {

                    servicoRabbitMQ.getRabbitTemplate().convertAndSend(
                            RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,
                            especificoBindingKey,
                            new Message(new ObjectMapper().writeValueAsString(container).getBytes()),
                            msg->{
                                msg.getMessageProperties().setPriority(2);
                                return msg;
                            }
                    );
                } catch (JsonProcessingException e) {
                    //skip
                }

            });

            servicoRabbitMQ.createConsumerStandard(salaUsuarioQueueName, new UserConsumer(salaAbertaWrapper,simpMessagingTemplate));

        };

        salaAbertaWrapper.integranteRequerinte.onUnsubscribe = ()->{
            salaAbertaWrapper.integranteRequerinte.isInscrito = false;
            servicoRabbitMQ.stopConsumer(salaUsuarioQueueName);
            servicoRabbitMQ.purgeQueue(salaUsuarioQueueName);
            servicoRabbitMQ.deleteQueue(salaUsuarioQueueName);
        };



        return ResponseEntity.ok(salaAbertaWrapper.salaNova);

    }

    public static List<AlteracaoContainerDTO> groupArray(List<AlteracaoSaidaDTO> list, int groupSize) {
        int val = ((list.size() + groupSize - 1) / groupSize);

        if(val<1){
            val = 1;
        }

        return IntStream.range(0, val).mapToObj(i -> new AlteracaoContainerDTO(
                        list.subList(i * groupSize, Math.min(list.size(), (i + 1) * groupSize)),groupSize
                )).toList();
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



        /*
        Cria consumidor da fila da arte.
        Este será responsável por enviar todas as alterações da fila da arte para o usuário por meio da binding key específica.
         */
/*
//            servicoRabbitMQ.createReadOnlyConsumer(queueArte,
//                    new AbrirSalaConsumidor(
//                            RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,
//                            especificoBindingKey,
//                            servicoRabbitMQ)
//            );
 */


}
