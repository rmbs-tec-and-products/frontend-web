package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcedimentoProdutoForm {

    private Integer produtoCodigo;
    private String produtoNome;
    private Integer quantidade = 1;
}