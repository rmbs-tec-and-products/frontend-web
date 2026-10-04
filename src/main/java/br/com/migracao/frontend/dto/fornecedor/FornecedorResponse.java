package br.com.migracao.frontend.dto.fornecedor;

public record FornecedorResponse(
        Integer codigo,
        String nome,
        String telefone
) {
}