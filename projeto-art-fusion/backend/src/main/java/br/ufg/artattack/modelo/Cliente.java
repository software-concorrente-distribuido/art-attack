package br.ufg.artattack.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "cliente")
@NoArgsConstructor

public class Cliente extends EntidadeJPA implements UserDetails {

    @Column(nullable = false)
    public String email;

    public String nome;

    @Column(nullable = true,length = 11)
    @Getter
    private String cpf;

    @Column( nullable = false)
    private String senha;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USUARIO_LOGADO"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isAtivo();
    }
    public void setCpf(String cpf) {
        cpf = cpf.replace(".","").replace("-","");
        this.cpf = cpf;
    }



    public void setSenha(String senha) {
        this.senha = senha;
    }
}
