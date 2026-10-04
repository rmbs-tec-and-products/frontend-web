package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CaixaMovimentacaoForm {

    private Integer codigo;
    private String descricao;
    private String tipo = "ENTRADA";
    private BigDecimal valor;
    private String observacao;
}