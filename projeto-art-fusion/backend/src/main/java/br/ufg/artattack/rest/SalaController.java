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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

@RestController()
@RequestMapping("api/sala")
public class SalaController {

    private final ConcurrentHashMap<Long, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

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

        Long arteId = abrirSalaDTO.arteId;

        semaphoreMap.putIfAbsent(arteId, new Semaphore(1));

        Semaphore semaphore = semaphoreMap.get(arteId);

        semaphore.acquire();

        try{

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

                Long qntdAlteracao = alteracaoRepositorio.countAlteracaoByArteId(salaAbertaWrapper.salaNova.arte.id);

                salaAbertaWrapper.integranteRequerinte.isInscrito = true;

                // Definindo os tamanhos dos grupos
                int mainGroupSize = 7500;
                int subGroupSize = 1500;

                final boolean[] consumerCriado = {false};

                // Processar em lotes de 15 mil
                for (int i = 0,j=0; i < qntdAlteracao; i += mainGroupSize,j++) {
                    // Obter um grupo de 15 mil alterações
                    List<Alteracao> alteracoes = alteracaoRepositorio.findAlteracaoByArte_IdOrderByDataCriacao(salaAbertaWrapper.salaNova.arte.id, PageRequest.of(
                            j,mainGroupSize
                    ));

                    // Converter para AlteracaoSaidaDTO e agrupar em subgrupos de 1500
                    var subGroups = groupArray(alteracoes.stream().map(AlteracaoSaidaDTO::new).toList(), subGroupSize);

                    subGroups.forEach(subGroup -> {
                        try {
                            servicoRabbitMQ.getRabbitTemplate().convertAndSend(
                                    RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,
                                    especificoBindingKey,
                                    new Message(new ObjectMapper().writeValueAsString(subGroup).getBytes()),
                                    msg -> {
                                        msg.getMessageProperties().setPriority(2);
                                        return msg;
                                    }
                            );

                            if (!consumerCriado[0]) {
                                servicoRabbitMQ.createConsumer(salaUsuarioQueueName, new UserConsumer(salaAbertaWrapper, simpMessagingTemplate));
                                consumerCriado[0] = true;
                            }

                        } catch (JsonProcessingException e) {
                            // Log or handle exception as needed
                        }
                    });
                }

            };

            salaAbertaWrapper.integranteRequerinte.onUnsubscribe = ()->{
                salaAbertaWrapper.integranteRequerinte.isInscrito = false;
                servicoRabbitMQ.stopConsumer(salaUsuarioQueueName);
                servicoRabbitMQ.purgeQueue(salaUsuarioQueueName);
                servicoRabbitMQ.deleteQueue(salaUsuarioQueueName);
            };



            return ResponseEntity.ok(salaAbertaWrapper.salaNova);

        }catch (Exception e){

            throw new Exception(e);

        }finally {
            semaphore.release();
            semaphoreMap.remove(arteId, semaphore); // Descomente se desejar remover o semáforo
        }
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

}
