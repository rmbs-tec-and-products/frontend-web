package br.com.migracao.frontend.dto.procedimento;

import java.math.BigDecimal;
import java.util.List;

public record ProcedimentoRequest(
        String nome,
        BigDecimal valor,
        List<ProcedimentoProdutoRequest> produtos
) {
}