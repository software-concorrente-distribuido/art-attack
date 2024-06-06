package br.ufg.artattack.servico;

import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.repositorio.ArteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ArteServico {

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    ArteRepositorio arteRepositorio;

    public ArteDTO criarArteDoUsuarioLogado(ArteDTO arteDTO){
        if(arteDTO.id!=null){
            throw new IllegalArgumentException("A arteDTO passada já possui um ID!");
        }
        Arte templateArte = new Arte();
        templateArte.usuario = usuarioServico.getUsuarioLogadoDB();
        templateArte.ativo = true;
        templateArte.titulo = arteDTO.titulo;
        templateArte.dataCriacao = new Date();
        var arteDoBanco = arteRepositorio.save(templateArte);

        arteDTO.usuarioNome = templateArte.usuario.nome;
        arteDTO.dataCriacao = templateArte.dataCriacao;
        arteDTO.usuarioId = templateArte.usuario.getId();
        arteDTO.id = arteDoBanco.getId();

        return arteDTO;
    }

}
