package br.ufg.artattack;

import br.ufg.artattack.amqp.ServicoRabbitMQ;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

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
    public void garantirQueuesArte(){

        arteRepositorio.findAll().forEach(art-> servicoRabbitMQ.createArteQueueAndConsumer(art.getId()));

    }

}

