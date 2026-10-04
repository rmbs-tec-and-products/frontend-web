package br.com.migracao.frontend.dto.agenda;

public record AgendaPacienteResponse(
        Integer codigo,
        String nome,
        String telefone,
        String celular
) {
}