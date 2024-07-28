package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Usuario;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlteracaoRepositorio extends JpaRepository<Alteracao,Long> {

    @EntityGraph(attributePaths = {"arte","arte.id","usuario.id"})
    List<Alteracao> findAlteracaoByArte_Id(Long arteId);


}

