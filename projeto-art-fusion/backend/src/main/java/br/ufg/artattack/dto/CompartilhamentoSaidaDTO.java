package br.ufg.artattack.dto;

import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.Compartilhamento;
import br.ufg.artattack.modelo.TipoPermissao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompartilhamentoSaidaDTO {

    public ArteDTO arteDTO;

    public List<TipoPermissao> permissoes;


    private CompartilhamentoSaidaDTO(Map.Entry<Arte,List<TipoPermissao>> par) {
        this.arteDTO =  new ArteDTO(par.getKey());
        this.permissoes = par.getValue();


    }

    public static List<CompartilhamentoSaidaDTO> createList(List<Compartilhamento> compartilhamentos){
        Map<Arte,List<TipoPermissao>> map = new HashMap<>();

        compartilhamentos.forEach(c->{
            if(map.containsKey(c.arte)){
                map.get(c.arte).add(c.tipoPermissao);
            }else{
                map.put(c.arte,new ArrayList<>(List.of(c.tipoPermissao)));
            }
        });

        return map.entrySet().stream().map(CompartilhamentoSaidaDTO::new).toList();
    }
}
