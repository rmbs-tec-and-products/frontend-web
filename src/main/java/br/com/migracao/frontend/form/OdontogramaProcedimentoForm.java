package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OdontogramaProcedimentoForm {

    private Integer codigo;
    private Integer procedimentoCodigo;
    private Integer dente;
    private String status = "A_REALIZAR";
    private BigDecimal valor;
    private List<String> faces = new ArrayList<>();
    private String observacao;
}