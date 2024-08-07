package br.ufg.artattack.modelo;

import br.ufg.artattack.dto.*;

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

        this.integrantes =new ArrayList<>();

        this.titulo = arte.titulo;

        this.arte = new ArteDTO(arte);

        this.uuid = uuid;

    }

    public AddIntegranteWrapper addIntegrante(UsuarioDTO usuarioDTO, List<TipoPermissao> permissoes){

        Integrante adicionar;

        for (int i = 0; i < this.integrantes.size(); i++) {

            Integrante integrante = this.integrantes.get(i);

            //integrante já está lá
            if (integrante.colaborador.id.equals(usuarioDTO.getId())) {
                adicionar = new Integrante(usuarioDTO, permissoes);

                this.integrantes.get(i).onUnsubscribe.run();

                this.integrantes.set(i,adicionar);

                return new AddIntegranteWrapper(adicionar,false);
            }

        }

        adicionar = new Integrante(usuarioDTO,permissoes);

        this.integrantes.add(adicionar);

        return new AddIntegranteWrapper(adicionar,false);
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

    public List<Integrante> getIntegrantesSemSerDTO(){
        return integrantes;
    }


    public Integrante getIntegrante(Long usuarioId){
        return integrantes.stream().filter(i->i.colaborador.id.equals(usuarioId.toString())).toList().get(0);
    }


    public Integrante obterIntegrante(String userId) {

        for (Integrante integrante : this.integrantes)
            if(integrante.colaborador.id.equals(userId))
                return integrante;

        return null;
    }
}
