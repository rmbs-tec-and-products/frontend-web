package br.com.migracao.frontend.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioResponse(

        Integer codigo,
        String nome,
        String login,
        String email,
        String perfil,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) {
}