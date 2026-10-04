package br.com.migracao.frontend.dto.paciente;

public record PacienteResumoResponse(

        Integer codigo,
        String nome,
        String telefone,
        String celular

) {
}