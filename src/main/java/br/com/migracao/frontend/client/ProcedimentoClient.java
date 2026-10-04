package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcedimentoClient {

    private final RestClient coreApiRestClient;

    public List<ProcedimentoResponse> listar(String pesquisa) {
        List<ProcedimentoResponse> procedimentos;

        if (pesquisa == null || pesquisa.isBlank()) {
            procedimentos = coreApiRestClient
                    .get()
                    .uri("/api/v1/procedimentos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } else {
            procedimentos = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/procedimentos")
                            .queryParam("pesquisa", pesquisa)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        return procedimentos == null
                ? List.of()
                : procedimentos;
    }

    public ProcedimentoResponse buscarPorCodigo(Integer codigo) {
        return coreApiRestClient
                .get()
                .uri("/api/v1/procedimentos/{codigo}", codigo)
                .retrieve()
                .body(ProcedimentoResponse.class);
    }

    public ProcedimentoResponse cadastrar(ProcedimentoRequest request) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/procedimentos")
                .body(request)
                .retrieve()
                .body(ProcedimentoResponse.class);
    }

    public ProcedimentoResponse atualizar(
            Integer codigo,
            ProcedimentoRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri("/api/v1/procedimentos/{codigo}", codigo)
                .body(request)
                .retrieve()
                .body(ProcedimentoResponse.class);
    }

    public void excluir(Integer codigo) {
        coreApiRestClient
                .delete()
                .uri("/api/v1/procedimentos/{codigo}", codigo)
                .retrieve()
                .toBodilessEntity();
    }
}