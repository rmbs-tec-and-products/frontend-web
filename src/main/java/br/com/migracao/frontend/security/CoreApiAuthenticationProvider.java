package br.com.migracao.frontend.security;

import br.com.migracao.frontend.dto.usuario.UsuarioAutenticadoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CoreApiAuthenticationProvider
        implements AuthenticationProvider {

    private final RestClient coreApiAuthRestClient;

    public CoreApiAuthenticationProvider(
            @Qualifier("coreApiAuthRestClient")
            RestClient coreApiAuthRestClient
    ) {
        this.coreApiAuthRestClient =
                coreApiAuthRestClient;
    }

    @Override
    public Authentication authenticate(
            Authentication authentication
    ) throws AuthenticationException {

        String login =
                authentication.getName();

        String senha =
                authentication
                        .getCredentials()
                        .toString();

        try {
            UsuarioAutenticadoResponse usuario =
                    coreApiAuthRestClient
                            .get()
                            .uri("/api/v1/auth/me")
                            .headers(
                                    headers ->
                                            headers.setBasicAuth(
                                                    login,
                                                    senha,
                                                    StandardCharsets.UTF_8
                                            )
                            )
                            .retrieve()
                            .body(
                                    UsuarioAutenticadoResponse.class
                            );

            if (usuario == null) {
                throw new BadCredentialsException(
                        "Usuário ou senha inválidos."
                );
            }

            List<GrantedAuthority> authorities =
                    new ArrayList<>();

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_USER"
                    )
            );

            if ("ADMIN".equalsIgnoreCase(
                    usuario.perfil()
            )) {
                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_ADMIN"
                        )
                );
            }

            UserDetails principal =
                    User.withUsername(
                                    usuario.login()
                            )
                            .password("")
                            .authorities(
                                    authorities
                            )
                            .build();

            return UsernamePasswordAuthenticationToken
                    .authenticated(
                            principal,
                            null,
                            authorities
                    );

        } catch (RestClientResponseException exception) {

            int status =
                    exception
                            .getStatusCode()
                            .value();

            if (status == 401
                    || status == 403) {

                throw new BadCredentialsException(
                        "Usuário ou senha inválidos."
                );
            }

            throw new AuthenticationServiceException(
                    "Não foi possível validar o usuário.",
                    exception
            );

        } catch (RestClientException exception) {

            throw new AuthenticationServiceException(
                    "Não foi possível comunicar com o servidor.",
                    exception
            );
        }
    }

    @Override
    public boolean supports(
            Class<?> authentication
    ) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(
                        authentication
                );
    }
}