package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.odontograma.OdontogramaProcedimentoRequest;
import br.com.migracao.frontend.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.frontend.dto.odontograma.OdontogramaRequest;
import br.com.migracao.frontend.dto.odontograma.OdontogramaResponse;
import br.com.migracao.frontend.dto.odontograma.OdontogramaResumoResponse;
import br.com.migracao.frontend.dto.odontograma.OdontogramaStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OdontogramaClient {

    private final RestClient coreApiRestClient;

    public OdontogramaResponse cadastrar(
            OdontogramaRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri("/api/v1/odontogramas")
                .body(request)
                .retrieve()
                .body(OdontogramaResponse.class);
    }

    public OdontogramaResponse buscarPorCodigo(
            Integer codigo
    ) {
        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/odontogramas/{codigo}",
                        codigo
                )
                .retrieve()
                .body(OdontogramaResponse.class);
    }

    public List<OdontogramaResumoResponse> listarPorPaciente(
            Integer pacienteCodigo
    ) {
        List<OdontogramaResumoResponse> odontogramas =
                coreApiRestClient
                        .get()
                        .uri(
                                "/api/v1/pacientes/{pacienteCodigo}/odontogramas",
                                pacienteCodigo
                        )
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return odontogramas == null
                ? List.of()
                : odontogramas;
    }

    public OdontogramaResponse alterarStatus(
            Integer codigo,
            OdontogramaStatusRequest request
    ) {
        return coreApiRestClient
                .patch()
                .uri(
                        "/api/v1/odontogramas/{codigo}/status",
                        codigo
                )
                .body(request)
                .retrieve()
                .body(OdontogramaResponse.class);
    }

    public OdontogramaResponse excluirDente(
            Integer codigo,
            Integer dente
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/odontogramas/{codigo}/dentes/{dente}/excluir",
                        codigo,
                        dente
                )
                .retrieve()
                .body(OdontogramaResponse.class);
    }

    public OdontogramaResponse restaurarDente(
            Integer codigo,
            Integer dente
    ) {
        return coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/odontogramas/{codigo}/dentes/{dente}/exclusao",
                        codigo,
                        dente
                )
                .retrieve()
                .body(OdontogramaResponse.class);
    }

    public OdontogramaProcedimentoResponse adicionarProcedimento(
            Integer codigo,
            OdontogramaProcedimentoRequest request
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/odontogramas/{codigo}/procedimentos",
                        codigo
                )
                .body(request)
                .retrieve()
                .body(OdontogramaProcedimentoResponse.class);
    }

    public OdontogramaProcedimentoResponse atualizarProcedimento(
            Integer codigo,
            Integer itemCodigo,
            OdontogramaProcedimentoRequest request
    ) {
        return coreApiRestClient
                .put()
                .uri(
                        "/api/v1/odontogramas/{codigo}/procedimentos/{itemCodigo}",
                        codigo,
                        itemCodigo
                )
                .body(request)
                .retrieve()
                .body(OdontogramaProcedimentoResponse.class);
    }

    public void excluirProcedimento(
            Integer codigo,
            Integer itemCodigo
    ) {
        coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/odontogramas/{codigo}/procedimentos/{itemCodigo}",
                        codigo,
                        itemCodigo
                )
                .retrieve()
                .toBodilessEntity();
    }

    public OdontogramaProcedimentoResponse concluirProcedimento(
            Integer codigo,
            Integer itemCodigo
    ) {
        return coreApiRestClient
                .post()
                .uri(
                        "/api/v1/odontogramas/{codigo}/procedimentos/{itemCodigo}/concluir",
                        codigo,
                        itemCodigo
                )
                .retrieve()
                .body(OdontogramaProcedimentoResponse.class);
    }
}