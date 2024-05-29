package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Arte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArteRepositorio extends JpaRepository<Arte,Long> {



}
