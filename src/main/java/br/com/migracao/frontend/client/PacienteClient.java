package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.paciente.PacienteRequest;
import br.com.migracao.frontend.dto.paciente.PacienteResponse;
import br.com.migracao.frontend.dto.paciente.PacienteResumoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PacienteClient {

    private final RestClient coreApiRestClient;

    public List<PacienteResumoResponse> listar() {

        List<PacienteResumoResponse> pacientes =
                coreApiRestClient
                        .get()
                        .uri("/api/v1/pacientes")
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return pacientes == null
                ? List.of()
                : pacientes;
    }

    public List<PacienteResumoResponse> pesquisar(
            String pesquisa
    ) {

        if (pesquisa == null || pesquisa.isBlank()) {
            return listar();
        }

        List<PacienteResumoResponse> pacientes =
                coreApiRestClient
                        .get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/api/v1/pacientes")
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

        return pacientes == null
                ? List.of()
                : pacientes;
    }

    public PacienteResponse buscarPorCodigo(
            Integer codigo
    ) {

        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/pacientes/{codigo}",
                        codigo
                )
                .retrieve()
                .body(PacienteResponse.class);
    }

    public PacienteResponse cadastrar(
            PacienteRequest request
    ) {

        return coreApiRestClient
                .post()
                .uri("/api/v1/pacientes")
                .body(request)
                .retrieve()
                .body(PacienteResponse.class);
    }

    public PacienteResponse atualizar(
            Integer codigo,
            PacienteRequest request
    ) {

        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/pacientes/{codigo}",
                        codigo
                )
                .body(request)
                .retrieve()
                .body(PacienteResponse.class);
    }

    public void excluir(
            Integer codigo
    ) {

        coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/pacientes/{codigo}",
                        codigo
                )
                .retrieve()
                .toBodilessEntity();
    }
}