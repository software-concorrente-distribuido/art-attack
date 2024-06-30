package br.ufg.artattack.servico;

import br.ufg.artattack.config.WebConfigurantions;
import br.ufg.artattack.modelo.Usuario;
import br.ufg.artattack.repositorio.UsuarioRepositorio;
import br.ufg.artattack.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioServico implements UserDetailsService {

    @Autowired
    public UsuarioRepositorio usuarioRepositorio;

    @Autowired
    WebConfigurantions autenticacaoConfiguracoes;


    public Usuario getUsuarioPeloEmail(String email){
        return this.usuarioRepositorio.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails = usuarioRepositorio.findByEmail(username);
        if(userDetails == null){
            throw new UsernameNotFoundException("Usuário ou senha inválida");
        }

        return userDetails;
    }

    /**
     *
     * @return UsuarioDTO contido no JWT
     */
    public UsuarioDTO getUsuarioLogadoDTO(){
        return (UsuarioDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Usuario getUsuarioLogadoDB(){
        return usuarioRepositorio.findById(Long.valueOf(((UsuarioDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id)).orElse(null);
    }

    public Long getUsuarioLogadoId(){
        return Long.valueOf(getUsuarioLogadoDTO().getId());
    }

    public Usuario criarUsuario(Usuario usuario) throws ServerException {

        Usuario maybeExist = this.usuarioRepositorio.findByEmail(usuario.getUsername());

        if(maybeExist!=null){
            throw new ServerException("Email já utilizado!");
        }
        usuario.ativo = true;
        usuario.setSenha(autenticacaoConfiguracoes.passwordEncoder().encode(usuario.getPassword()));
        usuario.dataCriacao = new Date();

        return this.usuarioRepositorio.save(usuario);
    }

    public Usuario getReferenceById(Long id){
        return usuarioRepositorio.getReferenceById(id);
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepositorio.findById(id);
    }

}
