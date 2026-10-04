package br.com.migracao.frontend.dto.procedimento;

public record ProcedimentoProdutoRequest(
        Integer produtoCodigo,
        Integer quantidade
) {
}