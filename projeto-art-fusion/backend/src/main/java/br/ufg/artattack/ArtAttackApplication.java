package br.ufg.artattack;

import br.ufg.artattack.amqp.RabbitMQConfig;
import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
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
            String queueName = servicoRabbitMQ.obterQueueArte(art.getId());

            servicoRabbitMQ.createDurableQueue(queueName);

            servicoRabbitMQ.bindQueue(queueName, RabbitMQConfig.ALTERACOES_EXCHANGE_NAME,art.getId().toString()+".geral");

        });

    }

}

