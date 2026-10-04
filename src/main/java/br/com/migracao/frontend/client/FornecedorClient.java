package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.fornecedor.FornecedorRequest;
import br.com.migracao.frontend.dto.fornecedor.FornecedorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FornecedorClient {

    private final RestClient coreApiRestClient;

    public List<FornecedorResponse> listar(
            String pesquisa
    ) {
        List<FornecedorResponse> fornecedores;

        if (pesquisa == null || pesquisa.isBlank()) {
            fornecedores = coreApiRestClient
                    .get()
                    .uri("/api/v1/fornecedores")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } else {
            fornecedores = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/fornecedores")
                            .queryParam("pesquisa", pesquisa)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        return fornecedores == null
                ? List.of()
                : fornecedores;
    }

    public FornecedorResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri("/api/v1/fornecedores/{codigo}", codigo)
                .retrieve()
                .body(FornecedorResponse.class);
    }

    public FornecedorResponse cadastrar(
            FornecedorRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/fornecedores")
                .body(request)
                .retrieve()
                .body(FornecedorResponse.class);
    }

    public FornecedorResponse atualizar(
            Integer codigo,
            FornecedorRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri("/api/v1/fornecedores/{codigo}", codigo)
                .body(request)
                .retrieve()
                .body(FornecedorResponse.class);
    }

    public void excluir(
            Integer codigo
    ) {
        coreApiRestClient
                .delete()
                .uri("/api/v1/fornecedores/{codigo}", codigo)
                .retrieve()
                .toBodilessEntity();
    }
}