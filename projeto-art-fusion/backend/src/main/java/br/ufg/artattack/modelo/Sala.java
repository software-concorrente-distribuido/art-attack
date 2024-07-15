package br.ufg.artattack.modelo;

import br.ufg.artattack.dto.AlteracaoSaidaDTO;
import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.dto.IntegranteDTO;
import br.ufg.artattack.dto.UsuarioDTO;

import java.util.*;

public class Sala {



    public String titulo;
    public  String uuid;

    public ArteDTO arte;

    private List<Integrante> integrantes;


    public Sala(UUID uuid, Arte arte){

        integrantes = new ArrayList<>();

        this.titulo = arte.titulo;

        this.uuid = uuid.toString();

    }
    public Sala(String uuid, Arte arte){

        integrantes =new ArrayList<>();


        this.titulo = arte.titulo;
        this.arte = new ArteDTO(arte);

        this.uuid = uuid;

    }
    public void addIntegrante(UsuarioDTO usuarioDTO, List<TipoPermissao> permissoes){

        for (int i = 0; i < this.integrantes.size(); i++) {

            Integrante integrante = this.integrantes.get(i);

            //integrante já está lá
            if (integrante.colaborador.id.equals(usuarioDTO.getId())) {
                this.integrantes.set(i, new Integrante(usuarioDTO, permissoes,integrante.mensagensBuffer));

                return;
            }

        }

        this.integrantes.add(new Integrante(usuarioDTO,permissoes));
    }

    public List<TipoPermissao> obterPermissoesDoUsuario(Long idUsuario){

        for (Integrante integrante : this.integrantes)
            if(integrante.colaborador.id.equals(idUsuario.toString()))
                return integrante.permissoes;

        return null;

    }

    //usado pelo ObjectMapper
    public List<IntegranteDTO> getIntegrantes() {
        return integrantes.stream().map(IntegranteDTO::new).toList();
    }

    public void colocarNoBufferDoIntegrante(Long usuarioId, String alteracao){

        Integrante integrante = getIntegrante(usuarioId);

        integrante.mensagensBuffer.add(alteracao);

    }

    public Integrante getIntegrante(Long usuarioId){
        return integrantes.stream().filter(i->i.colaborador.id.equals(usuarioId.toString())).toList().get(0);
    }


}
