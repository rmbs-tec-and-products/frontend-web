package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.dentista.DentistaRequest;
import br.com.migracao.frontend.dto.dentista.DentistaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistaClient {

    private final RestClient coreApiRestClient;

    public List<DentistaResponse> listar(
            String pesquisa
    ) {
        List<DentistaResponse> dentistas;

        if (pesquisa == null || pesquisa.isBlank()) {
            dentistas = coreApiRestClient
                    .get()
                    .uri("/api/v1/dentistas")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } else {
            dentistas = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/dentistas")
                            .queryParam("pesquisa", pesquisa)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        return dentistas == null
                ? List.of()
                : dentistas;
    }

    public DentistaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri("/api/v1/dentistas/{codigo}", codigo)
                .retrieve()
                .body(DentistaResponse.class);
    }

    public DentistaResponse cadastrar(
            DentistaRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/dentistas")
                .body(request)
                .retrieve()
                .body(DentistaResponse.class);
    }

    public DentistaResponse atualizar(
            Integer codigo,
            DentistaRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri("/api/v1/dentistas/{codigo}", codigo)
                .body(request)
                .retrieve()
                .body(DentistaResponse.class);
    }

    public void excluir(
            Integer codigo
    ) {
        coreApiRestClient
                .delete()
                .uri("/api/v1/dentistas/{codigo}", codigo)
                .retrieve()
                .toBodilessEntity();
    }
}