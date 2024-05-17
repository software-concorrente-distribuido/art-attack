package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente,Long> {

    Cliente findByEmail(String email);

}
