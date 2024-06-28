package br.ufg.artattack.rest;

import br.ufg.artattack.dto.AlteracaoEntradaDTO;
import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.exception.ExcecaoDTO;
import br.ufg.artattack.servico.AlteracaoServico;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class SocketController {

    @Autowired
    AlteracaoServico alteracaoServico;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/alteracoes/{sala}")
    public void propagar(@Payload Message<AlteracaoEntradaDTO> msg, @DestinationVariable String sala)  {
        AlteracaoEntradaDTO alteracaoEntradaDTO = msg.getPayload();

        AlteracaoSaidaDTO alteracaoDTO;

        try {
            alteracaoDTO = alteracaoServico.salvarPayloadAlteracao(alteracaoEntradaDTO);
            simpMessagingTemplate.convertAndSend("/topic/alteracoes/"+ sala,alteracaoDTO.toJsonString());

        } catch (Exception e) {
            simpMessagingTemplate.convertAndSend("/topic/alteracoes/" + sala,"{}");

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
