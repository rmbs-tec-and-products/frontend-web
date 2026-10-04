package br.com.migracao.frontend.dto.odontograma;

import java.math.BigDecimal;
import java.util.List;

public record OdontogramaProcedimentoRequest(
        Integer procedimentoCodigo,
        Integer dente,
        String status,
        BigDecimal valor,
        List<String> faces,
        String observacao
) {
}