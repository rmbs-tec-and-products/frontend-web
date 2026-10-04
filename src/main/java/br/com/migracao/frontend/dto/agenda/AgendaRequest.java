package br.com.migracao.frontend.dto.agenda;

import java.time.LocalDateTime;

public record AgendaRequest(
        LocalDateTime data,
        Integer pacienteCodigo,
        Integer dentistaCodigo
) {
}