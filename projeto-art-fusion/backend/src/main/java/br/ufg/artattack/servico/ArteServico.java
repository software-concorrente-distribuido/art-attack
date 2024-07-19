package br.ufg.artattack.servico;

import br.ufg.artattack.dto.*;
import br.ufg.artattack.exception.ProcessamentoException;
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

        if(Boolean.TRUE.equals(compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                dto.arteId,
                Long.valueOf(usuarioServico.getUsuarioLogadoDTO().id),
                dto.permissoes
                )))
            return true;

        return  false;
    }

    public List<CompartilhamentoSaidaDTO> compartilharParaAlguem(CompartilhamentoEntradaDTO dto) {

        if(dto.permissoes==null)
            throw new IllegalArgumentException("Requisição sem permissões!");

        if(Boolean.FALSE.equals(podeCompartilhar(dto)))
            throw new UnsupportedOperationException("Usuário não possui permissão para compartilhar a arte com os acessos requisitados ou os IDs são inválidos!");


        if(Boolean.TRUE.equals(compartilhamentoRepositorio.existsByArte_IdAndUsuarioIdAndTipoPermissaoIn(
                dto.arteId,
                dto.usuarioBeneficiadoId,
                dto.permissoes)))
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

            return CompartilhamentoSaidaDTO.createList(compartilhamentosNovos);
    }

    public List<CompartilhamentoSaidaDTO> obterCompartilhadasAMim() {
        return this.obterCompartilhadasAoUsuario(usuarioServico.getUsuarioLogadoId());
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

    public ArteDTO editarTituloArte(String novoTitulo, String arteId) {
    var arte= arteRepositorio.findByIdAndAdministrador_Id(Long.valueOf(arteId),usuarioServico.getUsuarioLogadoId());
    if(arte==null)
        throw new ProcessamentoException("Arte inexistente para o par arteId - administradorId (do token)");

    arte.titulo = novoTitulo;

    arteRepositorio.save(arte);

    return new ArteDTO(arte);
    }

    public List<ArteDTO> obterMinhasArtes() {
        return arteRepositorio.findByAndAdministrador_Id(
                usuarioServico.getUsuarioLogadoId()).stream().map(ArteDTO::new).toList();
    }

    public ArteDTO editarVisibilidade(EditarVisibilidadeDTO dto) {
        var arte = arteRepositorio.findById(dto.arteId).orElseThrow();
        arte.visibilidade = dto.visibilidade;
        arteRepositorio.save(arte);
        return new ArteDTO(arte);
    }


    public List<CompartilhamentoSaidaDTO> obterCompartilhadasAoUsuario(Long id) {
        var compartilhamentos = compartilhamentoRepositorio.
                findAllByUsuarioIdAndArteAdministradorIdNot(id, id);

        return CompartilhamentoSaidaDTO.createList(compartilhamentos);
    }

    public List<ArteDTO> obterPorVisibilidade(ArtePorUsrVisibilidadeDTO dto) {
        dto.visibilidades.remove(Visibilidade.PRIVADO);
        return arteRepositorio.findByAdministrador_IdAndVisibilidadeIn(dto.usuarioId,dto.visibilidades).stream().map(
                ArteDTO::new
        ).toList();

    }
}
