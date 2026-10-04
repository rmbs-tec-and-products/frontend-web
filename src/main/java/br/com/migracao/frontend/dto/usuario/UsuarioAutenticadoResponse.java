package br.com.migracao.frontend.dto.usuario;

public record UsuarioAutenticadoResponse(

        Integer codigo,
        String nome,
        String login,
        String perfil

) {
}