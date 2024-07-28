package br.ufg.artattack.servico;

import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.dto.AddIntegranteWrapper;
import br.ufg.artattack.dto.IntegranteDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.modelo.*;
import br.ufg.artattack.repositorio.CompartilhamentoRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class SalaServico {

    private static Map<String, Sala> salas = new HashMap<>();

    @Autowired
    RabbitAdmin rabbitAdmin;


    @Autowired
    ServicoRabbitMQ servicoRabbitMQ;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;

    @Value("${spring.rabbitmq.host}")
    String host;


    @Scheduled(fixedRate = 30000) // 3s em milissegundos
    public void limparSalasOciosas() throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.add("Accept-Language", "pt-BR,pt;q=0.9");
        headers.add("Cache-Control", "max-age=0");
        headers.add("Connection", "keep-alive");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        var urlBuilder = new StringBuilder().append("http://").append(host).append(":").append("15672").append("/api/queues/%2F/");

//        String url = "http://localhost:15672/api/queues/%2F/";

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));

        ArrayList<String> uuids = new ArrayList<>();

        for (Map.Entry<String, Sala> par : salas.entrySet()) {

            String arteQueueName = ServicoRabbitMQ.getArteQueueName(par.getValue().arte.id);

            ResponseEntity<String> response = restTemplate.exchange(new URI(urlBuilder+arteQueueName), HttpMethod.GET, entity, String.class);

            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

            var ideSince = jsonNode.get("idle_since").toString();

            if(ideSince==null){
                return;
            }

            ideSince = ideSince.replace("\"","");

            var instanteParado = LocalDateTime.parse(ideSince, DateTimeFormatter.ISO_ZONED_DATE_TIME).atZone(ZoneId.of("UTC"));

            var agora = LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo"));

            if(Duration.between(instanteParado,agora).toSeconds() >120 ){

                for (IntegranteDTO integrante : par.getValue().getIntegrantes()) {

                    String salaUsuarioQueueName= ServicoRabbitMQ.getSalaUsuarioQueueName(par.getKey(),integrante.colaborador.id);
                    servicoRabbitMQ.stopConsumer(salaUsuarioQueueName);
                    servicoRabbitMQ.deleteQueue(salaUsuarioQueueName);
                    servicoRabbitMQ.purgeQueue(salaUsuarioQueueName);

                }
                uuids.add(par.getKey());

            }
        }

        for (String uuid : uuids) {
            salas.remove(uuid);
        }

    }

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    ArteServico arteServico;

    @Autowired
    CompartilhamentoRepositorio compartilhamentoRepositorio;

    public SalaAbertaWrapper abrirSala(Long arteId) throws BadRequestException {

        Integrante integrante;

        if(!podeAbrirSala(arteId)){
            throw new BadRequestException("O usuário não pode abrir sala dessa arte!");
        }

        for(Map.Entry<String, Sala> s : salas.entrySet()){

            //sala já existente
            if(Objects.equals(s.getValue().arte.id, arteId)){

                AddIntegranteWrapper addIntegranteWrapper = s.getValue().addIntegrante(
                        usuarioServico.getUsuarioLogadoDTO(),
                        arteServico.permissoesPorArteUsuario(arteId, usuarioServico.getUsuarioLogadoId())
                );

                //atualizar ou adiciona usuario
                integrante =addIntegranteWrapper.integrante;

                return new SalaAbertaWrapper(s.getValue(),integrante, addIntegranteWrapper.isNovoIntegrante);

            }

        }

        return criarSala(arteId);
    }

    private SalaAbertaWrapper criarSala(Long idArte) throws NoSuchElementException {
        String uuid = UUID.randomUUID().toString();

        Arte arte =  arteServico.arteRepositorio.findById(idArte).orElseThrow( () -> new NoSuchElementException("Arte não encontrada"));

        Sala sala = new Sala(uuid,arte);

        AddIntegranteWrapper addIntegranteWrapper = sala.addIntegrante(
                usuarioServico.getUsuarioLogadoDTO(),
                arteServico.permissoesPorArteUsuario(idArte, usuarioServico.getUsuarioLogadoId())
        );

        Integrante integranteNovo = addIntegranteWrapper.integrante;

        salas.put(uuid, sala);

        return new SalaAbertaWrapper(sala,integranteNovo, addIntegranteWrapper.isNovoIntegrante);
    }

    public Sala obterSala(String uuid) {
        var sala =  salas.get(uuid);

        if(sala==null)
            throw new IllegalArgumentException("Sala já fechada");


        return sala;
    }

    public Sala fecharSala(String uuid){
        Sala sala = salas.remove(uuid);

        if(sala==null)
            throw new IllegalArgumentException("Não foi possível remover a sala, pois ela não está aberta!");

        return sala;
    }

    public Sala obterSalaPorIdArte(String arteId) throws Exception {

        for (Map.Entry<String, Sala> entry : salas.entrySet()) {

            Sala s = entry.getValue();

            if (Objects.equals(s.arte.id, Long.valueOf(arteId))) {
                return  s;
            }

        }

        throw new Exception("Não foi possível encontrar a susa Arte pelo ID fornecido");

    }

    public boolean podeAbrirSala(Long arteId) {

        return
                arteServico.isArtePublica(arteId) ||

                        compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                                arteId,
                                Long.valueOf(usuarioServico.getUsuarioLogadoDTO().id),
                                List.of(TipoPermissao.VISUALIZAR, TipoPermissao.EDITAR));


    }


    // Classe Sala pode ser uma classe interna ou uma classe separada


}
