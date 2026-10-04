package br.com.migracao.frontend.dto.prontuario;

import java.time.LocalDateTime;
import java.util.Locale;

public record ProntuarioArquivoResponse(

        Integer codigo,
        Integer pacienteCodigo,
        String titulo,
        String descricao,
        String nomeArquivoOriginal,
        String contentType,
        Long tamanho,
        LocalDateTime criadoEm,
        boolean imagem

) {

    public boolean pdf() {
        return contentType != null
                && contentType.equalsIgnoreCase(
                "application/pdf"
        );
    }

    public boolean imagemPreview() {
        if (contentType == null) {
            return false;
        }

        String tipo =
                contentType.toLowerCase(
                        Locale.ROOT
                );

        return tipo.equals("image/jpeg")
                || tipo.equals("image/jpg")
                || tipo.equals("image/png")
                || tipo.equals("image/webp");
    }

    public String tipoExibicao() {

        if (pdf()) {
            return "PDF";
        }

        if (imagem) {
            return "Imagem";
        }

        return "Arquivo";
    }

    public String tamanhoFormatado() {

        if (tamanho == null
                || tamanho <= 0) {

            return "0 B";
        }

        if (tamanho < 1024) {

            return tamanho + " B";
        }

        if (tamanho < 1024L * 1024L) {

            return String.format(
                    Locale.forLanguageTag("pt-BR"),
                    "%.1f KB",
                    tamanho / 1024.0
            );
        }

        return String.format(
                Locale.forLanguageTag("pt-BR"),
                "%.1f MB",
                tamanho / (1024.0 * 1024.0)
        );
    }
}