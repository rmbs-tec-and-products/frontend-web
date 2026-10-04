package br.com.migracao.frontend.dto.odontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OdontogramaProcedimentoResponse(
        Integer codigo,
        Integer procedimentoCodigo,
        String procedimentoNome,
        Integer dente,
        String status,
        String statusDescricao,
        BigDecimal valor,
        String face,
        List<String> faces,
        String observacao,
        LocalDateTime data
) {
}