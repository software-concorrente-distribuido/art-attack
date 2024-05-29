package br.ufg.artattack.repositorio;

import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlteracaoRepositorio extends JpaRepository<Alteracao,Long> {

    List<Alteracao> findAlteracaoByArte_Id(Long arteId);


}

