package br.ufg.artattack.servico;

import br.ufg.artattack.dto.AlteracaoDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Service
public class AlteracaoServico {

    AlteracaoRepositorio alteracaoRepositorio;

    ArteRepositorio arteRepositorio;

    UsuarioRepositorio usuarioRepositorio;

    public AlteracaoServico(AlteracaoRepositorio alteracaoRepositorio,ArteRepositorio arteRepositorio,UsuarioRepositorio usuarioRepositorio ){
        this.alteracaoRepositorio = alteracaoRepositorio;
        this.arteRepositorio = arteRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public AlteracaoDTO salvarPayloadAlteracao(String payload) throws JsonProcessingException, DataIntegrityViolationException {

        var dto = new ObjectMapper().readValue(payload,AlteracaoDTO.class);

        var alteracao  = new Alteracao();

        alteracao.setDelta(dto.delta);

        alteracao.arte = arteRepositorio.getReferenceById(dto.arteId);
        alteracao.usuario = usuarioRepositorio.getReferenceById(dto.usuarioId);


        try {
            alteracao.dataCriacao = DateFormat.getDateInstance().parse(dto.dataCriacao);
        } catch (Exception e) {
            alteracao.dataCriacao = new Date();
            dto.dataCriacao = alteracao.dataCriacao.toString();
        }


        alteracao = alteracaoRepositorio.save(alteracao);

        dto.id = alteracao.getId();

        return dto;
    }


}
