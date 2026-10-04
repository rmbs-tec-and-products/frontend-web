package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.agenda.AgendaDentistaResponse;
import br.com.migracao.frontend.dto.agenda.AgendaPacienteResponse;
import br.com.migracao.frontend.dto.agenda.AgendaRequest;
import br.com.migracao.frontend.dto.agenda.AgendaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AgendaClient {

    private final RestClient coreApiRestClient;

    public List<AgendaResponse> listar(
            LocalDate data,
            Integer dentistaCodigo
    ) {
        List<AgendaResponse> agendamentos;

        if (dentistaCodigo == null) {
            agendamentos = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/agenda")
                            .queryParam(
                                    "data",
                                    data
                            )
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } else {
            agendamentos = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/agenda")
                            .queryParam(
                                    "data",
                                    data
                            )
                            .queryParam(
                                    "dentistaCodigo",
                                    dentistaCodigo
                            )
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        return agendamentos == null
                ? List.of()
                : agendamentos;
    }

    public AgendaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/agenda/{codigo}",
                        codigo
                )
                .retrieve()
                .body(AgendaResponse.class);
    }

    public AgendaResponse cadastrar(
            AgendaRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/agenda")
                .body(request)
                .retrieve()
                .body(AgendaResponse.class);
    }

    public AgendaResponse atualizar(
            Integer codigo,
            AgendaRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/agenda/{codigo}",
                        codigo
                )
                .body(request)
                .retrieve()
                .body(AgendaResponse.class);
    }

    public void excluir(
            Integer codigo
    ) {
        coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/agenda/{codigo}",
                        codigo
                )
                .retrieve()
                .toBodilessEntity();
    }

    public List<AgendaPacienteResponse> listarPacientes() {
        List<AgendaPacienteResponse> pacientes =
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

    public List<AgendaDentistaResponse> listarDentistas() {
        List<AgendaDentistaResponse> dentistas =
                coreApiRestClient
                        .get()
                        .uri("/api/v1/dentistas")
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return dentistas == null
                ? List.of()
                : dentistas;
    }
}