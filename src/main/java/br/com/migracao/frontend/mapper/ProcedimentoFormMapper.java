package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.procedimento.ProcedimentoProdutoRequest;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoProdutoResponse;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoResponse;
import br.com.migracao.frontend.form.ProcedimentoForm;
import br.com.migracao.frontend.form.ProcedimentoProdutoForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcedimentoFormMapper {

    public ProcedimentoRequest toRequest(ProcedimentoForm form) {
        List<ProcedimentoProdutoRequest> produtos = form.getProdutos() == null
                ? List.of()
                : form.getProdutos()
                .stream()
                .filter(produto -> produto.getProdutoCodigo() != null)
                .map(produto -> new ProcedimentoProdutoRequest(
                        produto.getProdutoCodigo(),
                        produto.getQuantidade()
                ))
                .toList();

        return new ProcedimentoRequest(
                form.getNome(),
                form.getValor(),
                produtos
        );
    }

    public ProcedimentoForm toForm(ProcedimentoResponse response) {
        ProcedimentoForm form = new ProcedimentoForm();

        form.setCodigo(response.codigo());
        form.setNome(response.nome());
        form.setValor(response.valor());

        if (response.produtos() != null) {
            form.setProdutos(
                    response.produtos()
                            .stream()
                            .map(this::toProdutoForm)
                            .toList()
            );
        }

        return form;
    }

    private ProcedimentoProdutoForm toProdutoForm(
            ProcedimentoProdutoResponse response
    ) {
        ProcedimentoProdutoForm form = new ProcedimentoProdutoForm();

        form.setProdutoCodigo(response.produtoCodigo());
        form.setProdutoNome(response.produtoNome());
        form.setQuantidade(response.quantidade());

        return form;
    }
}