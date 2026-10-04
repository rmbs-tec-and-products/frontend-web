package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.produto.ProdutoRequest;
import br.com.migracao.frontend.dto.produto.ProdutoResponse;
import br.com.migracao.frontend.dto.produto.ProdutoResumoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProdutoClient {

    private final RestClient coreApiRestClient;

    public List<ProdutoResumoResponse> listar() {
        List<ProdutoResponse> produtos = coreApiRestClient
                .get()
                .uri("/api/v1/produtos")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (produtos == null) {
            return List.of();
        }

        return produtos.stream()
                .map(produto ->
                        new ProdutoResumoResponse(
                                produto.codigo(),
                                produto.nome()
                        )
                )
                .toList();
    }

    public List<ProdutoResponse> listar(
            String pesquisa
    ) {
        List<ProdutoResponse> produtos;

        if (pesquisa == null || pesquisa.isBlank()) {
            produtos = coreApiRestClient
                    .get()
                    .uri("/api/v1/produtos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } else {
            produtos = coreApiRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/produtos")
                            .queryParam("pesquisa", pesquisa)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        return produtos == null
                ? List.of()
                : produtos;
    }

    public ProdutoResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri("/api/v1/produtos/{codigo}", codigo)
                .retrieve()
                .body(ProdutoResponse.class);
    }

    public ProdutoResponse cadastrar(
            ProdutoRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/produtos")
                .body(request)
                .retrieve()
                .body(ProdutoResponse.class);
    }

    public ProdutoResponse atualizar(
            Integer codigo,
            ProdutoRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri("/api/v1/produtos/{codigo}", codigo)
                .body(request)
                .retrieve()
                .body(ProdutoResponse.class);
    }

    public void excluir(
            Integer codigo
    ) {
        coreApiRestClient
                .delete()
                .uri("/api/v1/produtos/{codigo}", codigo)
                .retrieve()
                .toBodilessEntity();
    }
}