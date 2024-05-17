package br.ufg.artattack.rest.dto;

import lombok.Getter;
import br.ufg.artattack.modelo.Cliente;

@Getter
public class ClienteDTO   {
    public String email;
    public String cpf;

    public String id;
    public ClienteDTO(){}

    public ClienteDTO(Cliente cliente){
        this.email = cliente.email;
        this.cpf = cliente.getCpf();
        this.id = cliente.getId().toString();
    }

}
