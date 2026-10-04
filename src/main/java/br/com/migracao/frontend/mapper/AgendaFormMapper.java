package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.agenda.AgendaRequest;
import br.com.migracao.frontend.dto.agenda.AgendaResponse;
import br.com.migracao.frontend.form.AgendaForm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AgendaFormMapper {

    public AgendaRequest toRequest(
            AgendaForm form
    ) {
        LocalDateTime dataHora = null;

        if (form.getData() != null
                && form.getHora() != null) {

            dataHora = LocalDateTime.of(
                    form.getData(),
                    form.getHora()
            );
        }

        return new AgendaRequest(
                dataHora,
                form.getPacienteCodigo(),
                form.getDentistaCodigo()
        );
    }

    public AgendaForm toForm(
            AgendaResponse response
    ) {
        AgendaForm form = new AgendaForm();

        form.setCodigo(response.codigo());

        if (response.data() != null) {
            form.setData(
                    response.data().toLocalDate()
            );

            form.setHora(
                    response.data().toLocalTime()
            );
        }

        form.setPacienteCodigo(
                response.pacienteCodigo()
        );

        form.setDentistaCodigo(
                response.dentistaCodigo()
        );

        return form;
    }
}