package br.ufg.artattack.servico;

import br.ufg.artattack.dto.ArteDTO;
import br.ufg.artattack.dto.CompartilhamentoEntradaDTO;
import br.ufg.artattack.dto.CompartilhamentoSaidaDTO;
import br.ufg.artattack.dto.UsuarioDTO;
import br.ufg.artattack.modelo.Arte;
import br.ufg.artattack.modelo.Compartilhamento;
import br.ufg.artattack.modelo.TipoPermissao;
import br.ufg.artattack.modelo.Visibilidade;
import br.ufg.artattack.repositorio.ArteRepositorio;
import br.ufg.artattack.repositorio.CompartilhamentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArteServico {

    @Autowired
    UsuarioServico usuarioServico;

    @Autowired
    public ArteRepositorio arteRepositorio;

    @Autowired
    public CompartilhamentoRepositorio compartilhamentoRepositorio;



    public ArteDTO criarArteDoUsuarioLogado(ArteDTO arteDTO){
        if(arteDTO.id!=null){
            throw new IllegalArgumentException("A arteDTO passada já possui um ID!");
        }
        Arte templateArte = new Arte();
        templateArte.administrador = usuarioServico.getUsuarioLogadoDB();
        templateArte.titulo = arteDTO.titulo;
        templateArte.dataCriacao = new Date();

        if(arteRepositorio.existsByTituloAndAdministradorId(templateArte.titulo,templateArte.administrador.getId()))
            throw new IllegalArgumentException("Esse título já existe para o usuário "+ templateArte.administrador.email);

        var arteDoBanco = arteRepositorio.save(templateArte);

        atribuirAcessos(Arrays.stream(TipoPermissao.values()).toList(),arteDoBanco.getId(),templateArte.administrador.getId());

        arteDTO.administrador = new UsuarioDTO(templateArte.administrador);
        arteDTO.dataCriacao = templateArte.dataCriacao;
        arteDTO.id = arteDoBanco.getId();

        return arteDTO;
    }

    public void atribuirAcessos(List<TipoPermissao> tipoPermissaos, Long arteId, Long usuarioId ){
        ArrayList<Compartilhamento> compartilhamentos = new ArrayList<>();

        tipoPermissaos.forEach(tp->{
            Compartilhamento compartilhamento = new Compartilhamento();
            compartilhamento.arte = arteRepositorio.getReferenceById(arteId);
            compartilhamento.usuario = usuarioServico.getReferenceById(usuarioId);
            compartilhamento.dataCriacao= new Date();
            compartilhamento.tipoPermissao = tp;
            compartilhamentos.add(compartilhamento);
        });

        compartilhamentoRepositorio.saveAll(compartilhamentos);

    }

    public List<ArteDTO> findAllDTOs() {
        return arteRepositorio.findAll().stream().map(ArteDTO::new).toList();
    }

    public Boolean podeCompartilhar(CompartilhamentoEntradaDTO dto ) {

        //existe algum par arte-usuario-permissao que tenha a permissao em uma das requisicoes de dto.permissao?

        if(compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                dto.arteId,
                Long.valueOf(usuarioServico.getUsuarioLogadoDTO().id),
                dto.permissoes
                ))
            return true;

        return  false;
    }

    public List<CompartilhamentoSaidaDTO> compartilharParaAlguem(CompartilhamentoEntradaDTO dto) {

        if(dto.permissoes==null)
            throw new IllegalArgumentException("Requisição sem permissões!");

        if(Boolean.FALSE.equals(podeCompartilhar(dto)))
            throw new UnsupportedOperationException("Usuário não possui permissão para compartilhar a arte com os acessos requisitados ou os IDs são inválidos!");


        if(compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                dto.arteId,
                dto.usuarioBeneficiadoId,
                dto.permissoes))
            throw new IllegalArgumentException("Arte já foi compartilhada à esse usuário com essa permissão! ");


        ArrayList<Compartilhamento> compartilhamentosNovos = new ArrayList<>();

        dto.permissoes.forEach(p->{
            Compartilhamento compartilhamento = new Compartilhamento();
            try{
                compartilhamento.arte = arteRepositorio.findById(dto.arteId).orElseThrow();
                compartilhamento.usuario = usuarioServico.findById(dto.usuarioBeneficiadoId).orElseThrow();
            }catch (NoSuchElementException exception){
                throw new NoSuchElementException("ID da arte ou do beneficiado inválido!");
            }

            compartilhamento.dataCriacao = new Date();
            compartilhamento.tipoPermissao = p;
            compartilhamentosNovos.add(compartilhamento);
        });

            compartilhamentoRepositorio.saveAll(compartilhamentosNovos);

            return compartilhamentosNovos.stream().map(c->new CompartilhamentoSaidaDTO(c)).toList();

    }

    public List obterCompartilhadasPorUsuario(Long usuarioId) {
        List<Compartilhamento> compartilhamentos = compartilhamentoRepositorio.findAllByUsuarioId(usuarioId);
        return compartilhamentos.stream().map(c->new CompartilhamentoSaidaDTO(c)).toList();
    }

    public List<TipoPermissao> permissoesPorArteUsuario(Long arteId, Long usuarioId){

        return compartilhamentoRepositorio.findAllByUsuarioIdAndAndArteId(
                usuarioId,
                arteId
        ).stream().map(compartilhamento -> compartilhamento.tipoPermissao).toList();
    }


    public boolean isIdAdministrador(Long arteId,Long administradorId) {
        return arteRepositorio.existsByIdAndAdministradorId(arteId,administradorId);
    }

    public boolean isArtePublica(Long idArte) {
        return arteRepositorio.existsByIdAndVisibilidade(idArte, Visibilidade.PUBLICO);
    }
}
