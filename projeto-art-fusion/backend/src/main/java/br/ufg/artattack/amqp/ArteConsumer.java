package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.modelo.Alteracao;
import br.ufg.artattack.repositorio.AlteracaoRepositorio;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Date;

public class ArteConsumer implements MessageListener {

    AlteracaoRepositorio alteracaoRepositorio;
    ArteRepositorio arteRepositorio;
    UsuarioRepositorio usuarioRepositorio;

    public ArteConsumer(AlteracaoRepositorio alteracaoRepositorio, ArteRepositorio arteRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.alteracaoRepositorio = alteracaoRepositorio;
        this.arteRepositorio = arteRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void onMessage(Message message) {
        try{
            var mapper = new ObjectMapper();
            AlteracaoSaidaDTO dto = mapper.readValue(new String(message.getBody()),AlteracaoSaidaDTO.class);

            var alteracao = new Alteracao();

            alteracao.setDelta(dto.delta);

            alteracao.arte = arteRepositorio.getReferenceById(dto.arteId);

            alteracao.usuario = usuarioRepositorio.getReferenceById(dto.usuarioId);

            alteracao.dataCriacao = new Date();

            alteracao = alteracaoRepositorio.save(alteracao);

            dto.id = alteracao.getId();

        }catch (Exception e){
            //skip
        }

    }
}
