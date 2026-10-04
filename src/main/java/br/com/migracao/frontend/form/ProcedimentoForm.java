package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProcedimentoForm {

    private Integer codigo;
    private String nome;
    private BigDecimal valor;
    private List<ProcedimentoProdutoForm> produtos = new ArrayList<>();
}