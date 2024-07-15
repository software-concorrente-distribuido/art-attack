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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;


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


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @MessageMapping("/alteracoes/{uuid}")
    public void propagar(@Payload Message<AlteracaoEntradaDTO> msg, @DestinationVariable String uuid, Principal principal)  {



        try {
            AlteracaoEntradaDTO alteracaoEntradaDTO = msg.getPayload();

            Sala sala = salaServico.obterSala(uuid);

            verificarPermissoesSala(alteracaoEntradaDTO, sala);

            AlteracaoSaidaDTO alteracaoDTO = alteracaoServico.salvarPayloadAlteracao(alteracaoEntradaDTO);

            simpMessagingTemplate.convertAndSend("/topic/alteracoes/"+ uuid,alteracaoDTO.toJsonString());

            org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(alteracaoDTO.toJsonString().getBytes());

            rabbitTemplate.convertAndSend("alteracoes.geral",alteracaoDTO);


        } catch (Exception e) {
            simpMessagingTemplate.convertAndSend("/topic/alteracoes/" + uuid,new ExcecaoDTO(e).stringfy());

        }


    }

    private void verificarPermissoesSala(AlteracaoEntradaDTO alteracaoEntradaDTO, Sala sala) throws IllegalArgumentException {

        if(sala==null){
            throw new IllegalArgumentException("Sala não encontrada!");
        }
        if( !Objects.equals(sala.arte.id, alteracaoEntradaDTO.arteId)){
            throw new IllegalArgumentException("Requisição de alteração inválida! ID da arte da sala não bate com ID da arte de alteração");
        }

        List<TipoPermissao> permissoes=  sala.obterPermissoesDoUsuario(usuarioServico.getUsuarioLogadoId());

        if(permissoes==null || !permissoes.contains(TipoPermissao.EDITAR)){
            throw new IllegalArgumentException("Usuário sem permissão necessária para alterações!");
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
