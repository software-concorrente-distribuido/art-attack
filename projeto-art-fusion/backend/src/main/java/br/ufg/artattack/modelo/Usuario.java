package br.ufg.artattack.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "usuario")
@NoArgsConstructor
public class Usuario extends EntidadeJPA implements UserDetails {

    @Column(name = "ativo")
    public Boolean ativo;

    @Column(nullable = false)
    public String email;

    public String nome;

    @Column( nullable = false)
    private String senha;

    @OneToMany(mappedBy = "administrador")
    public List<Arte> artes;


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
        return this.ativo;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }
}
