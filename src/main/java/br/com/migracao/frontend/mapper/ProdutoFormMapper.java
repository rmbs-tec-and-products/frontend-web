package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.produto.ProdutoRequest;
import br.com.migracao.frontend.dto.produto.ProdutoResponse;
import br.com.migracao.frontend.form.ProdutoForm;
import org.springframework.stereotype.Component;

@Component
public class ProdutoFormMapper {

    public ProdutoRequest toRequest(
            ProdutoForm form
    ) {
        return new ProdutoRequest(
                form.getNome(),
                form.getDescricao(),
                form.getQuantidade(),
                form.getUnidade(),
                form.getEmbalagem(),
                form.getQtdEmbalagem(),
                form.getValor(),
                form.getUltimoValor(),
                form.getFornecedorCodigo(),
                form.getQuantidadeMinima()
        );
    }

    public ProdutoForm toForm(
            ProdutoResponse response
    ) {
        ProdutoForm form = new ProdutoForm();

        form.setCodigo(response.codigo());
        form.setNome(response.nome());
        form.setDescricao(response.descricao());
        form.setQuantidade(response.quantidade());
        form.setUnidade(response.unidade());
        form.setEmbalagem(response.embalagem());
        form.setQtdEmbalagem(response.qtdEmbalagem());
        form.setValor(response.valor());
        form.setUltimoValor(response.ultimoValor());
        form.setFornecedorCodigo(response.fornecedorCodigo());
        form.setQuantidadeMinima(response.quantidadeMinima());

        return form;
    }
}