package br.ufg.artattack.dto;

import br.ufg.artattack.exception.ProcessamentoException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AlteracaoContainerDTO {

    public Long numeroLote;

    private int tamanhoMaximo;

    private List<AlteracaoSaidaDTO> alteracoes;

    public AlteracaoContainerDTO(int numeroLote, int tamanhoMaximo){
        this.numeroLote = (long) numeroLote;
        this.tamanhoMaximo = tamanhoMaximo;
        alteracoes = new LinkedList<>();
    }

    public AlteracaoContainerDTO(List<AlteracaoSaidaDTO> alteracoes,int numeroLote){
        this.numeroLote = (long)numeroLote;
        this.alteracoes = alteracoes;
    }

    public void addAlteracao(AlteracaoSaidaDTO alteracaoSaidaDTO) throws ProcessamentoException{
        if(alteracoes.size()==tamanhoMaximo){
            throw new ProcessamentoException("Tamanho maximo atingdo");
        }
        alteracoes.add(alteracaoSaidaDTO);
    }

    public List<AlteracaoSaidaDTO> getAlteracoes() {
        return alteracoes;
    }
}
