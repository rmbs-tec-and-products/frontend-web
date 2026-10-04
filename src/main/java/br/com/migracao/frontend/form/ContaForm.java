package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContaForm {

    private Integer codigo;

    private String tipo = "PAGAR";

    private String descricao;

    private String formaPagamento;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private String observacao;

    private Boolean recorrente = false;
}