package br.ufg.artattack.repositorio;

import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.TipoPermissao;
import br.ufg.artattack.modelo.Visibilidade;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface ArteRepositorio extends JpaRepository<Arte,Long> {


    @Query("SELECT a.snapshot from arte a where a.id = :id")
    public byte[] obterSnapshotPorArte(Long id);

    Boolean existsByIdAndAdministradorId(Long id, Long administradorId);

    Boolean existsByIdAndVisibilidade(Long id, Visibilidade visibilidade);

    boolean existsByTituloAndAdministradorId(String titulo, Long id);

    Arte findByIdAndAdministrador_Id(Long id, Long administradorId);

    @EntityGraph(attributePaths = {"administrador","administrador.email","administrador.nome"})
    List<Arte> findByAndAdministrador_Id(Long usuarioLogadoId);


    List<Arte> findByAdministrador_IdAndVisibilidadeIn(Long usuarioId, List<Visibilidade> visibilidades);
}
