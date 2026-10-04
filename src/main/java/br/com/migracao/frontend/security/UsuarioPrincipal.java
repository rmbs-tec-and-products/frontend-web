package br.com.migracao.frontend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal
        implements UserDetails, Serializable {

    private final Integer codigo;
    private final String nome;
    private final String login;
    private final String perfil;

    public UsuarioPrincipal(
            Integer codigo,
            String nome,
            String login,
            String perfil
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.login = login;
        this.perfil = perfil;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getPerfil() {
        return perfil;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if ("ADMIN".equalsIgnoreCase(perfil)) {

            return List.of(
                    new SimpleGrantedAuthority(
                            "ROLE_USER"
                    ),
                    new SimpleGrantedAuthority(
                            "ROLE_ADMIN"
                    )
            );
        }

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_USER"
                )
        );
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return login;
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
        return true;
    }
}