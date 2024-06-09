package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Arte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArteRepositorio extends JpaRepository<Arte,Long> {


    @Query("SELECT a.snapshot from arte a where a.id = :id")
    public byte[] obterSnapshotPorArte(Long id);

}
