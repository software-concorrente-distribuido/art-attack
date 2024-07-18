package br.ufg.artattack.amqp;


import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.SalaAbertaWrapper;
import br.ufg.artattack.exception.ProcessamentoException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//POJO
public class UsuarioSalaSocketEnviador {

    private SalaAbertaWrapper salaAbertaWrapper;

    private SimpMessagingTemplate simpMessagingTemplate;

    private String urlDestination;

    private Queue<AlteracaoSaidaDTO> cache = new LinkedList<>();

    public UsuarioSalaSocketEnviador(SalaAbertaWrapper salaAbertaWrapper, SimpMessagingTemplate simpMessagingTemplate){
        this.salaAbertaWrapper = salaAbertaWrapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.urlDestination = "/topic/alteracoes/"+ salaAbertaWrapper.salaNova.uuid + "/"+ salaAbertaWrapper.integranteRequerinte.colaborador.id;

    }


    public void handleMessage(AlteracaoSaidaDTO alteracaoSaidaDTO){

        //alteracaoSaidaDTO.delta
        // se algum delta de cache tiver alteração no mesmo pixel -> executar merge


        cache.offer(alteracaoSaidaDTO);
        if(cache.size()>5){
            cache.poll();
        }

        try {

            simpMessagingTemplate.convertAndSend(urlDestination,alteracaoSaidaDTO.toJsonString());
        } catch (Exception e) {
            throw new ProcessamentoException(e);
        }
    }


}
