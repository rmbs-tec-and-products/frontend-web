package br.com.migracao.frontend.dto.produto;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String descricao,
        Integer quantidade,
        String unidade,
        Integer embalagem,
        Integer qtdEmbalagem,
        BigDecimal valor,
        BigDecimal ultimoValor,
        Integer fornecedorCodigo,
        Integer quantidadeMinima
) {
}