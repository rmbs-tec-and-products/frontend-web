package br.com.migracao.frontend.dto.caixa;

import java.math.BigDecimal;

public record CaixaMovimentacaoRequest(
        String descricao,
        String tipo,
        BigDecimal valor,
        String observacao
) {
}