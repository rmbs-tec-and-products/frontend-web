package br.com.migracao.frontend.client;

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
        List<ProdutoResumoResponse> produtos = coreApiRestClient
                .get()
                .uri("/api/v1/produtos")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return produtos == null
                ? List.of()
                : produtos;
    }
}