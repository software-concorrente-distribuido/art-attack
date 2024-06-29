package br.ufg.artattack.rest;

import br.ufg.artattack.dto.AlteracaoEntradaDTO;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.exception.ExcecaoDTO;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.modelo.TipoPermissao;
import br.ufg.artattack.servico.AlteracaoServico;
import br.ufg.artattack.servico.SalaServico;
import br.ufg.artattack.servico.UsuarioServico;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Controller
public class SocketController {

    @Autowired
    AlteracaoServico alteracaoServico;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SalaServico salaServico;

    @Autowired
    private UsuarioServico usuarioServico;

    @MessageMapping("/alteracoes/{uuid}")
    public void propagar(@Payload Message<AlteracaoEntradaDTO> msg, @DestinationVariable String uuid)  {
        AlteracaoEntradaDTO alteracaoEntradaDTO = msg.getPayload();

        AlteracaoSaidaDTO alteracaoDTO;

        Sala sala = salaServico.obterSala(uuid);

        if(sala==null){
            return;
        }

        List<TipoPermissao> permissoes=  sala.obterPermissoesDoUsuario(usuarioServico.getUsuarioLogadoId());

        if(permissoes==null || !permissoes.contains(TipoPermissao.EDITAR)){
            return;
        }


        try {
            alteracaoDTO = alteracaoServico.salvarPayloadAlteracao(alteracaoEntradaDTO);
            simpMessagingTemplate.convertAndSend("/topic/alteracoes/"+ uuid,alteracaoDTO.toJsonString());

        } catch (Exception e) {
            simpMessagingTemplate.convertAndSend("/topic/alteracoes/" + uuid,"{}");

        }


    }

    @MessageMapping("/alteracoes")
    public void propagar(@Payload Message<String> msg) {

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            simpMessagingTemplate.convertAndSend("/topic/alteracoes",
                    objectMapper.writeValueAsString(new ExcecaoDTO("Erro no acesso às salas!","Tópico de inscrição faltando o ID da sala!")) );
        }catch (JsonProcessingException jsonExc){
            simpMessagingTemplate.convertAndSend("/topic/alteracoes", "{mensagem: 'Socket errado, coloque o ID da sala!'}");
        }
    }


}
