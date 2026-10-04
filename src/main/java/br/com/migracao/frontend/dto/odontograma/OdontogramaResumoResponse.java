package br.com.migracao.frontend.dto.odontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OdontogramaResumoResponse(

        Integer codigo,
        String tipo,
        String tipoDescricao,
        BigDecimal valor,
        LocalDateTime data,
        String status,
        String statusDescricao,
        Boolean odontopediatria

) {
}