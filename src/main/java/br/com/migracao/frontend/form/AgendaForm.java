package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AgendaForm {

    private Integer codigo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

    private Integer pacienteCodigo;
    private Integer dentistaCodigo;
}