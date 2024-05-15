package br.ufg.artattack.servico;

import br.ufg.artattack.modelo.Cliente;
import br.ufg.artattack.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClienteServico implements UserDetailsService {

    @Autowired
    public ClienteRepositorio clienteRepositorio;

    public Cliente getClientePeloEmail(String email){
        return this.clienteRepositorio.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clienteRepositorio.findByEmail(username);
    }
}
