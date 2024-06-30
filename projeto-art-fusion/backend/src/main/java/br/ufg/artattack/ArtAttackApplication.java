package br.ufg.artattack;

import br.ufg.artattack.modelo.Usuario;
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

}

/*
 1. cadastrar adm
 2. login adm
 3. atualizar adm
 4. alterar senha adm
 5. resetar senha adm
 6. cadastrar rifa
 7. listar rifa
 8. atualizar rifa
 9. exluir rifa
 10. encontrar rifa por ID
 11. encontrar rifa por nome
 12. reservar bilhete
 13. reserva por telefone
 14. validar reserva por telefone, id e número único de bilhete
 15. sortear bilhetes
 16. pesquisar sorteios ja feitos [filtros]
 17. rankings de reservas e reservadores [filtros]
 ____
 18. lógica de recuperar senha
 19. listar reservas [filtro]
 20. lógicas de pagamento




 */
