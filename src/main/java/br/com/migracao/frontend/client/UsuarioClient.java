package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.usuario.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioClient {

    private final RestClient coreApiRestClient;

    public List<UsuarioResponse> listar(
            String pesquisa
    ) {
        List<UsuarioResponse> usuarios;

        if (pesquisa == null
                || pesquisa.isBlank()) {

            usuarios =
                    coreApiRestClient
                            .get()
                            .uri(
                                    "/api/v1/admin/usuarios"
                            )
                            .retrieve()
                            .body(
                                    new ParameterizedTypeReference<>() {
                                    }
                            );

        } else {

            usuarios =
                    coreApiRestClient
                            .get()
                            .uri(
                                    uriBuilder ->
                                            uriBuilder
                                                    .path(
                                                            "/api/v1/admin/usuarios"
                                                    )
                                                    .queryParam(
                                                            "pesquisa",
                                                            pesquisa
                                                    )
                                                    .build()
                            )
                            .retrieve()
                            .body(
                                    new ParameterizedTypeReference<>() {
                                    }
                            );
        }

        return usuarios == null
                ? List.of()
                : usuarios;
    }

    public UsuarioResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/admin/usuarios/{codigo}",
                        codigo
                )
                .retrieve()
                .body(
                        UsuarioResponse.class
                );
    }

    public UsuarioResponse cadastrar(
            String nome,
            String login,
            String email,
            String senha,
            String perfil
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/admin/usuarios"
                )
                .body(
                        new UsuarioCadastroRequest(
                                nome,
                                login,
                                email,
                                senha,
                                perfil
                        )
                )
                .retrieve()
                .body(
                        UsuarioResponse.class
                );
    }

    public UsuarioResponse atualizar(
            Integer codigo,
            String nome,
            String login,
            String email,
            String perfil
    ) {
        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/admin/usuarios/{codigo}",
                        codigo
                )
                .body(
                        new UsuarioAtualizacaoRequest(
                                nome,
                                login,
                                email,
                                perfil
                        )
                )
                .retrieve()
                .body(
                        UsuarioResponse.class
                );
    }

    public UsuarioResponse alterarStatus(
            Integer codigo,
            boolean ativo
    ) {
        return coreApiRestClient
                .patch()
                .uri(
                        "/api/v1/admin/usuarios/{codigo}/status",
                        codigo
                )
                .body(
                        new UsuarioStatusRequest(
                                ativo
                        )
                )
                .retrieve()
                .body(
                        UsuarioResponse.class
                );
    }

    public void redefinirSenha(
            Integer codigo,
            String senha
    ) {
        coreApiRestClient
                .patch()
                .uri(
                        "/api/v1/admin/usuarios/{codigo}/senha",
                        codigo
                )
                .body(
                        new UsuarioSenhaRequest(
                                senha
                        )
                )
                .retrieve()
                .toBodilessEntity();
    }

    private record UsuarioCadastroRequest(
            String nome,
            String login,
            String email,
            String senha,
            String perfil
    ) {
    }

    private record UsuarioAtualizacaoRequest(
            String nome,
            String login,
            String email,
            String perfil
    ) {
    }

    private record UsuarioStatusRequest(
            Boolean ativo
    ) {
    }

    private record UsuarioSenhaRequest(
            String senha
    ) {
    }
}