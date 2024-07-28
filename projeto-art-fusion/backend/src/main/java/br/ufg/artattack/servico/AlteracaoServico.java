package br.ufg.artattack.servico;

import br.ufg.artattack.dto.AlteracaoEntradaDTO;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlteracaoServico {

    AlteracaoRepositorio alteracaoRepositorio;

    ArteRepositorio arteRepositorio;

    UsuarioRepositorio usuarioRepositorio;

    UsuarioServico usuarioServico;


    public AlteracaoServico(AlteracaoRepositorio alteracaoRepositorio,
                            ArteRepositorio arteRepositorio,
                            UsuarioRepositorio usuarioRepositorio,
                            UsuarioServico usuarioServico
                            ){
        this.alteracaoRepositorio = alteracaoRepositorio;
        this.arteRepositorio = arteRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioServico = usuarioServico;
    }

    public AlteracaoSaidaDTO gerarPayloadAlteracaoSaida(AlteracaoEntradaDTO payload){

        AlteracaoSaidaDTO dto = new AlteracaoSaidaDTO(payload);

        long idUsuarioLogado = Long.parseLong(usuarioServico.getUsuarioLogadoDTO().id);

        dto.dataCriacao = new Date().getTime();

        dto.usuarioId = idUsuarioLogado;

//        dto.id = alteracao.getId();

        return dto;
    }


}
