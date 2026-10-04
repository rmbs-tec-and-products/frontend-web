package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.usuario.UsuarioAutenticadoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestClient coreApiRestClient;

    public UsuarioAutenticadoResponse autenticar(
            String username,
            String password
    ) {
        return coreApiRestClient
                .get()
                .uri("/api/v1/auth/me")
                .headers(headers ->
                        headers.setBasicAuth(
                                username,
                                password
                        )
                )
                .retrieve()
                .body(
                        UsuarioAutenticadoResponse.class
                );
    }
}