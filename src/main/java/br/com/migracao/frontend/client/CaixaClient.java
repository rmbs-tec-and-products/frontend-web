package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoRequest;
import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.frontend.dto.caixa.CaixaResponse;
import br.com.migracao.frontend.dto.caixa.CaixaResumoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CaixaClient {

    private final RestClient coreApiRestClient;

    public CaixaResponse abrir() {
        return coreApiRestClient
                .post()
                .uri("/api/v1/caixas/abrir")
                .retrieve()
                .body(CaixaResponse.class);
    }

    public CaixaResponse fechar(
            Integer codigo
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/caixas/{codigo}/fechar",
                        codigo
                )
                .retrieve()
                .body(CaixaResponse.class);
    }

    public CaixaResponse buscarAberto() {
        return coreApiRestClient
                .get()
                .uri("/api/v1/caixas/aberto")
                .retrieve()
                .body(CaixaResponse.class);
    }

    public CaixaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/caixas/{codigo}",
                        codigo
                )
                .retrieve()
                .body(CaixaResponse.class);
    }

    public List<CaixaResumoResponse> listar(
            LocalDate inicio,
            LocalDate fim
    ) {
        List<CaixaResumoResponse> caixas =
                coreApiRestClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v1/caixas")
                                .queryParam("inicio", inicio)
                                .queryParam("fim", fim)
                                .build())
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return caixas == null
                ? List.of()
                : caixas;
    }

    public CaixaMovimentacaoResponse adicionarMovimentacao(
            Integer caixaCodigo,
            CaixaMovimentacaoRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/caixas/{codigo}/movimentacoes",
                        caixaCodigo
                )
                .body(request)
                .retrieve()
                .body(CaixaMovimentacaoResponse.class);
    }

    public CaixaMovimentacaoResponse atualizarMovimentacao(
            Integer caixaCodigo,
            Integer movimentacaoCodigo,
            CaixaMovimentacaoRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/caixas/{codigo}/movimentacoes/{movimentacaoCodigo}",
                        caixaCodigo,
                        movimentacaoCodigo
                )
                .body(request)
                .retrieve()
                .body(CaixaMovimentacaoResponse.class);
    }

    public void excluirMovimentacao(
            Integer caixaCodigo,
            Integer movimentacaoCodigo
    ) {
        coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/caixas/{codigo}/movimentacoes/{movimentacaoCodigo}",
                        caixaCodigo,
                        movimentacaoCodigo
                )
                .retrieve()
                .toBodilessEntity();
    }
}