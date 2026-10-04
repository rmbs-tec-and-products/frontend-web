package br.com.migracao.frontend.dto.conta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContaResponse(

        Integer codigo,
        String tipo,
        String descricao,
        String formaPagamento,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDateTime dataBaixa,
        String observacao,
        String status,
        boolean recorrente,
        boolean vencida

) {
}