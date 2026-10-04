package br.com.migracao.frontend.dto.procedimento;

public record ProcedimentoProdutoResponse(
        Integer produtoCodigo,
        String produtoNome,
        Integer quantidade
) {
}