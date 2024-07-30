package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Alteracao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlteracaoRepositorio extends JpaRepository<Alteracao,Long> {

    @EntityGraph(attributePaths = {"arte", "arte.id", "usuario.id"})
    List<Alteracao> findAlteracaoByArte_IdOrderByDataCriacao(Long arteId, Pageable pageable);

    @EntityGraph(attributePaths = {"arte","arte.id","usuario.id"})
    Long countAlteracaoByArteId(Long artId);

}

