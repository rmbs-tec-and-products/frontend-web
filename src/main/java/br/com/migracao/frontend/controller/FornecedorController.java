package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.FornecedorClient;
import br.com.migracao.frontend.dto.fornecedor.FornecedorRequest;
import br.com.migracao.frontend.dto.fornecedor.FornecedorResponse;
import br.com.migracao.frontend.form.FornecedorForm;
import br.com.migracao.frontend.mapper.FornecedorFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorClient fornecedorClient;
    private final FornecedorFormMapper fornecedorFormMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {
        model.addAttribute(
                "fornecedores",
                fornecedorClient.listar(pesquisa)
        );

        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("modo", "consultar");

        return "fornecedor/index";
    }

    @GetMapping("/novo")
    public String novo(
            Model model
    ) {
        model.addAttribute(
                "fornecedorForm",
                new FornecedorForm()
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "fornecedor/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {
        FornecedorResponse response =
                fornecedorClient.buscarPorCodigo(codigo);

        FornecedorForm form =
                fornecedorFormMapper.toForm(response);

        model.addAttribute(
                "fornecedorForm",
                form
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "fornecedor/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute FornecedorForm fornecedorForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            FornecedorRequest request =
                    fornecedorFormMapper.toRequest(fornecedorForm);

            if (fornecedorForm.getCodigo() == null) {

                fornecedorClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Fornecedor cadastrado com sucesso."
                );

            } else {

                fornecedorClient.atualizar(
                        fornecedorForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Fornecedor atualizado com sucesso."
                );
            }

            return "redirect:/fornecedores";

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "fornecedorForm",
                    fornecedorForm
            );

            model.addAttribute(
                    "modo",
                    "novo"
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "fornecedor/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {

            fornecedorClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Fornecedor excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    "Não foi possível excluir o fornecedor."
            );
        }

        return "redirect:/fornecedores";
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados informados e tente novamente.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "Fornecedor não encontrado.";
        }

        if (exception.getStatusCode().value() == 409) {
            return "O fornecedor não pode ser excluído porque está sendo utilizado.";
        }

        return "Não foi possível concluir a operação.";
    }
}