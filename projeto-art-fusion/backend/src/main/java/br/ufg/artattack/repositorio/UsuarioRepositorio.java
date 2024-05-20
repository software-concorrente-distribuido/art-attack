package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

    Usuario findByEmail(String email);

}
