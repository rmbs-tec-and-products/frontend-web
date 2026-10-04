package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.fornecedor.FornecedorRequest;
import br.com.migracao.frontend.dto.fornecedor.FornecedorResponse;
import br.com.migracao.frontend.form.FornecedorForm;
import org.springframework.stereotype.Component;

@Component
public class FornecedorFormMapper {

    public FornecedorRequest toRequest(
            FornecedorForm form
    ) {
        return new FornecedorRequest(
                form.getNome(),
                form.getTelefone()
        );
    }

    public FornecedorForm toForm(
            FornecedorResponse response
    ) {
        FornecedorForm form = new FornecedorForm();

        form.setCodigo(response.codigo());
        form.setNome(response.nome());
        form.setTelefone(response.telefone());

        return form;
    }
}