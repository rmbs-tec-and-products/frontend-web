package br.com.migracao.frontend.dto.caixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaixaMovimentacaoResponse(
        Integer codigo,
        String tipo,
        String descricao,
        BigDecimal valor,
        String observacao,
        LocalDateTime data
) {
}