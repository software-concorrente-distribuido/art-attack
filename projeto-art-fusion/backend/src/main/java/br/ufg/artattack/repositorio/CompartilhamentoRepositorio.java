package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Compartilhamento;
import br.ufg.artattack.modelo.TipoPermissao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompartilhamentoRepositorio extends JpaRepository<Compartilhamento,Long> {

    boolean existsByArte_IdAndUsuarioIdAndTipoPermissao(Long arteId, Long usuarioId, TipoPermissao tipoPermissao);

    @EntityGraph(attributePaths = {"arte","arte.administrador"})
    List<Compartilhamento> findAllByUsuarioId(Long usuarioId);

    List<Compartilhamento> findAllByUsuarioIdAndAndArteId(Long usuarioId, Long arteId);

    Boolean existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(Long arteId, Long usuarioId, List<TipoPermissao> tipoPermissaos);

    @EntityGraph(attributePaths = {"arte","arte.administrador","arte.titulo","arte.visibilidade"})
    List<Compartilhamento> findAllByUsuarioIdAndArteAdministradorIdNot(Long id1, Long id2);
}
