package br.ufg.artattack.rest;

import br.ufg.artattack.dto.AlteracaoDTO;
import br.ufg.artattack.servico.AlteracaoServico;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
public class SocketController {

    @Autowired
    AlteracaoServico alteracaoServico;


    @MessageMapping("/alteracoes")
    public String propagar(@Payload String payload)  {
        AlteracaoDTO alteracaoDTO;
        try {
            alteracaoDTO = alteracaoServico.salvarPayloadAlteracao(payload);
        } catch (Exception e) {
            return "{}";
        }

        return alteracaoDTO.toJsonString();

    }

}
