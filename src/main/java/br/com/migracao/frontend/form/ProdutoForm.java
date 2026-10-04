package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoForm {

    private Integer codigo;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private String unidade;
    private Integer embalagem;
    private Integer qtdEmbalagem;
    private BigDecimal valor;
    private BigDecimal ultimoValor;
    private Integer fornecedorCodigo;
    private Integer quantidadeMinima;
}