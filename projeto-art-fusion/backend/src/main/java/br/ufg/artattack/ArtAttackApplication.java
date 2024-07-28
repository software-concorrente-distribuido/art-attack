package br.ufg.artattack;

import br.ufg.artattack.amqp.ArteConsumer;
import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.LinkedHashMap;

@SpringBootApplication
@EnableScheduling
public class ArtAttackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtAttackApplication.class, args);
    }

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ArteRepositorio arteRepositorio;

    @Autowired
    ServicoRabbitMQ servicoRabbitMQ;

    @Autowired
    AlteracaoRepositorio alteracaoRepositorio;


    @PostConstruct
    public void gerarDados(){
        if(!usuarioRepositorio.findAll().isEmpty()) return;
        var cliente = new Usuario();
        cliente.nome = "João Gabriel Tavares";
        cliente.email = "joao@gmail.com";
        cliente.setSenha("$2a$12$xiQJXM2SGyP5r2FevTXGcevF.KU5G.GtdE/zzktHB.5ldWamg2M.m");//senha123
        cliente.dataCriacao = new Date();
        cliente.ativo = true;
        usuarioRepositorio.save(cliente);

    }


    @PostConstruct
    public void garantirQueuesARte(){

        arteRepositorio.findAll().forEach(art->{
            String queueName = ServicoRabbitMQ.getArteQueueName(art.getId());

            servicoRabbitMQ.createDurableQueue(queueName);

            servicoRabbitMQ.bindQueue(queueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,ServicoRabbitMQ.getGeralBindingKey(art.getId()));

            servicoRabbitMQ.stopConsumer(queueName);

            servicoRabbitMQ.createConsumerStandard(queueName,new ArteConsumer(
                    alteracaoRepositorio,
                    arteRepositorio,
                    usuarioRepositorio
            ));


        });

    }

}

