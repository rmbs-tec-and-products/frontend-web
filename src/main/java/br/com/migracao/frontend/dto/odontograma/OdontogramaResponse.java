package br.com.migracao.frontend.dto.odontograma;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OdontogramaResponse(
        Integer codigo,
        String tipo,
        String tipoDescricao,
        Integer pacienteCodigo,
        String pacienteNome,
        String pacienteTelefone,
        BigDecimal valor,
        LocalDateTime data,
        String status,
        String statusDescricao,
        Boolean odontopediatria,
        List<Integer> dentesExcluidos,
        List<OdontogramaProcedimentoResponse> procedimentos
) {
}