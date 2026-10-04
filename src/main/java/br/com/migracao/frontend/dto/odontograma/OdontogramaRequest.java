package br.com.migracao.frontend.dto.odontograma;

public record OdontogramaRequest(

        Integer pacienteCodigo,
        Boolean odontopediatria

) {
}