package br.ufg.artattack.servico;

import br.ufg.artattack.dto.AlteracaoEntradaDTO;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;

@Service
public class AlteracaoServico {

    AlteracaoRepositorio alteracaoRepositorio;

    ArteRepositorio arteRepositorio;

    UsuarioRepositorio usuarioRepositorio;

    UsuarioServico usuarioServico;


    private EntityManager entityManager;

    public AlteracaoServico(AlteracaoRepositorio alteracaoRepositorio,
                            ArteRepositorio arteRepositorio,
                            UsuarioRepositorio usuarioRepositorio,
                            UsuarioServico usuarioServico,
                            EntityManager entityManager
                            ){
        this.alteracaoRepositorio = alteracaoRepositorio;
        this.arteRepositorio = arteRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioServico = usuarioServico;
        this.entityManager= entityManager;
    }

    public AlteracaoSaidaDTO salvarPayloadAlteracao(AlteracaoEntradaDTO payload) throws JsonProcessingException, DataIntegrityViolationException {

        AlteracaoSaidaDTO dto = new AlteracaoSaidaDTO(payload);

        Long idUsuarioLogado = Long.valueOf(usuarioServico.getUsuarioLogadoDTO().id);

        var alteracao  = new Alteracao();

        alteracao.setDelta(dto.delta);

        alteracao.arte = arteRepositorio.getReferenceById(dto.arteId);

        alteracao.usuario = usuarioRepositorio.getReferenceById(idUsuarioLogado);

        alteracao.dataCriacao = new Date();

        alteracao = alteracaoRepositorio.save(alteracao);

        dto.dataCriacao = alteracao.dataCriacao.getTime();

        dto.usuarioId = idUsuarioLogado;

        dto.id = alteracao.getId();

        return dto;
    }


}
