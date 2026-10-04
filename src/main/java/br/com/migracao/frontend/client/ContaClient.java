package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.conta.ContaResponse;
import br.com.migracao.frontend.dto.conta.ContaResumoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContaClient {

    private final RestClient coreApiRestClient;

    public List<ContaResponse> listar(
            LocalDate inicio,
            LocalDate fim,
            String tipo,
            String status,
            String pesquisa
    ) {
        List<ContaResponse> contas =
                coreApiRestClient
                        .get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/api/v1/contas")
                                        .queryParam("inicio", inicio)
                                        .queryParam("fim", fim)
                                        .queryParamIfPresent(
                                                "tipo",
                                                Optional.ofNullable(
                                                        normalizarFiltro(tipo)
                                                )
                                        )
                                        .queryParamIfPresent(
                                                "status",
                                                Optional.ofNullable(
                                                        normalizarFiltro(status)
                                                )
                                        )
                                        .queryParamIfPresent(
                                                "pesquisa",
                                                Optional.ofNullable(
                                                        normalizarFiltro(pesquisa)
                                                )
                                        )
                                        .build()
                        )
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return contas == null
                ? List.of()
                : contas;
    }

    public ContaResumoResponse resumo(
            LocalDate inicio,
            LocalDate fim
    ) {
        return coreApiRestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/contas/resumo")
                                .queryParam(
                                        "inicio",
                                        inicio
                                )
                                .queryParam(
                                        "fim",
                                        fim
                                )
                                .build()
                )
                .retrieve()
                .body(
                        ContaResumoResponse.class
                );
    }

    public ContaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/contas/{codigo}",
                        codigo
                )
                .retrieve()
                .body(
                        ContaResponse.class
                );
    }

    public ContaResponse cadastrar(
            String tipo,
            String descricao,
            String formaPagamento,
            BigDecimal valor,
            LocalDate dataVencimento,
            String observacao,
            Boolean recorrente
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/contas")
                .body(
                        new ContaRequest(
                                tipo,
                                descricao,
                                formaPagamento,
                                valor,
                                dataVencimento,
                                observacao,
                                recorrente
                        )
                )
                .retrieve()
                .body(
                        ContaResponse.class
                );
    }

    public ContaResponse atualizar(
            Integer codigo,
            String tipo,
            String descricao,
            String formaPagamento,
            BigDecimal valor,
            LocalDate dataVencimento,
            String observacao,
            Boolean recorrente
    ) {
        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/contas/{codigo}",
                        codigo
                )
                .body(
                        new ContaRequest(
                                tipo,
                                descricao,
                                formaPagamento,
                                valor,
                                dataVencimento,
                                observacao,
                                recorrente
                        )
                )
                .retrieve()
                .body(
                        ContaResponse.class
                );
    }

    public ContaResponse baixar(
            Integer codigo
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/contas/{codigo}/baixar",
                        codigo
                )
                .retrieve()
                .body(
                        ContaResponse.class
                );
    }

    public ContaResponse cancelar(
            Integer codigo
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/contas/{codigo}/cancelar",
                        codigo
                )
                .retrieve()
                .body(
                        ContaResponse.class
                );
    }

    private String normalizarFiltro(
            String valor
    ) {
        if (valor == null
                || valor.isBlank()) {

            return null;
        }

        return valor.trim();
    }

    private record ContaRequest(

            String tipo,
            String descricao,
            String formaPagamento,
            BigDecimal valor,
            LocalDate dataVencimento,
            String observacao,
            Boolean recorrente

    ) {
    }
}