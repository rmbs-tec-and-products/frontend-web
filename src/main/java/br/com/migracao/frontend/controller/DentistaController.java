package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.DentistaClient;
import br.com.migracao.frontend.dto.dentista.DentistaRequest;
import br.com.migracao.frontend.dto.dentista.DentistaResponse;
import br.com.migracao.frontend.form.DentistaForm;
import br.com.migracao.frontend.mapper.DentistaFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dentistas")
@RequiredArgsConstructor
public class DentistaController {

    private final DentistaClient dentistaClient;
    private final DentistaFormMapper dentistaFormMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {
        model.addAttribute(
                "dentistas",
                dentistaClient.listar(pesquisa)
        );

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "dentista/index";
    }

    @GetMapping("/novo")
    public String novo(
            Model model
    ) {
        model.addAttribute(
                "dentistaForm",
                new DentistaForm()
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "dentista/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {
        DentistaResponse response =
                dentistaClient.buscarPorCodigo(codigo);

        DentistaForm form =
                dentistaFormMapper.toForm(response);

        model.addAttribute(
                "dentistaForm",
                form
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "dentista/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute DentistaForm dentistaForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            DentistaRequest request =
                    dentistaFormMapper.toRequest(dentistaForm);

            if (dentistaForm.getCodigo() == null) {

                dentistaClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Dentista cadastrado com sucesso."
                );

            } else {

                dentistaClient.atualizar(
                        dentistaForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Dentista atualizado com sucesso."
                );
            }

            return "redirect:/dentistas";

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "dentistaForm",
                    dentistaForm
            );

            model.addAttribute(
                    "modo",
                    "novo"
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "dentista/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {

            dentistaClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Dentista excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {

            if (exception.getStatusCode().value() == 409) {

                redirectAttributes.addFlashAttribute(
                        "mensagemErro",
                        "O dentista não pode ser excluído porque possui agendamentos vinculados."
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "mensagemErro",
                        "Não foi possível excluir o dentista."
                );
            }
        }

        return "redirect:/dentistas";
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados informados e tente novamente.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "Dentista não encontrado.";
        }

        if (exception.getStatusCode().value() == 409) {
            return "A operação não pôde ser concluída porque o dentista está sendo utilizado.";
        }

        return "Não foi possível concluir a operação.";
    }
}