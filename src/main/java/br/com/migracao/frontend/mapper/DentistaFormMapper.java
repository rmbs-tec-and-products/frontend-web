package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.dentista.DentistaRequest;
import br.com.migracao.frontend.dto.dentista.DentistaResponse;
import br.com.migracao.frontend.form.DentistaForm;
import org.springframework.stereotype.Component;

@Component
public class DentistaFormMapper {

    public DentistaRequest toRequest(
            DentistaForm form
    ) {
        return new DentistaRequest(
                form.getNome()
        );
    }

    public DentistaForm toForm(
            DentistaResponse response
    ) {
        DentistaForm form = new DentistaForm();

        form.setCodigo(response.codigo());
        form.setNome(response.nome());

        return form;
    }
}