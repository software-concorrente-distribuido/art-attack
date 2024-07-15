package br.ufg.artattack.servico;

import br.ufg.artattack.dto.AbrirSalaDTO;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.Sala;
import br.ufg.artattack.modelo.Snapshot;
import br.ufg.artattack.modelo.TipoPermissao;
import br.ufg.artattack.repositorio.CompartilhamentoRepositorio;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SalaServico {

    private static Map<String, Sala> salas = new HashMap<>();

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    ArteServico arteServico;

    @Autowired
    CompartilhamentoRepositorio compartilhamentoRepositorio;

    public Sala abrirSala(Long arteId) throws BadRequestException {

        if(!podeAbrirSala(arteId)){
            throw new BadRequestException("O usuário não pode abrir sala dessa arte!");
        }

        for(Map.Entry<String, Sala> s : salas.entrySet()){

            //sala já existente
            if(Objects.equals(s.getValue().arte.id, arteId)){

                //atualizar usuario
                s.getValue().addIntegrante(
                        usuarioServico.getUsuarioLogadoDTO(),
                        arteServico.permissoesPorArteUsuario(arteId, usuarioServico.getUsuarioLogadoId())
                );

                return s.getValue();

            }

        }

        return criarSala(arteId);
    }
    private Sala criarSala(Long idArte) throws NoSuchElementException {
        String uuid = UUID.randomUUID().toString();

        Arte arte =  arteServico.arteRepositorio.findById(idArte).orElseThrow( () -> new NoSuchElementException("Arte não encontrada"));

        Sala sala = new Sala(uuid,arte);

        sala.addIntegrante(
                usuarioServico.getUsuarioLogadoDTO(),
                arteServico.permissoesPorArteUsuario(idArte, usuarioServico.getUsuarioLogadoId())
        );

        salas.put(uuid, sala);
        return sala;
    }

    public Sala obterSala(String uuid) {
        var sala =  salas.get(uuid);

        if(sala==null)
            throw new IllegalArgumentException("Sala já fechada");


        return sala;
    }

    public Sala fecharSala(String uuid){
        Sala sala = salas.remove(uuid);

        if(sala==null)
            throw new IllegalArgumentException("Não foi possível remover a sala, pois ela não está aberta!");

        return sala;
    }

    public Sala obterSalaPorIdArte(String arteId) throws Exception {

        for (Map.Entry<String, Sala> entry : salas.entrySet()) {

            Sala s = entry.getValue();

            if (Objects.equals(s.arte.id, Long.valueOf(arteId))) {
                return  s;
            }

        }

        throw new Exception("Não foi possível encontrar a susa Arte pelo ID fornecido");

    }

    public boolean podeAbrirSala(Long arteId) {

        return
                arteServico.isArtePublica(arteId) ||

                        compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                                arteId,
                                Long.valueOf(usuarioServico.getUsuarioLogadoDTO().id),
                                List.of(TipoPermissao.VISUALIZAR, TipoPermissao.EDITAR));


    }


    // Classe Sala pode ser uma classe interna ou uma classe separada


}
