package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.PacienteClient;
import br.com.migracao.frontend.dto.paciente.PacienteRequest;
import br.com.migracao.frontend.dto.paciente.PacienteResponse;
import br.com.migracao.frontend.dto.paciente.PacienteResumoResponse;
import br.com.migracao.frontend.form.PacienteForm;
import br.com.migracao.frontend.mapper.PacienteFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteClient pacienteClient;
    private final PacienteFormMapper pacienteFormMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {

        List<PacienteResumoResponse> pacientes =
                pacienteClient.pesquisar(pesquisa);

        model.addAttribute(
                "pacientes",
                pacientes
        );

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "paciente/index";
    }

    @GetMapping("/novo")
    public String novo(
            Model model
    ) {

        model.addAttribute(
                "pacienteForm",
                new PacienteForm()
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "paciente/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {

        PacienteResponse paciente =
                pacienteClient.buscarPorCodigo(codigo);

        PacienteForm form =
                pacienteFormMapper.toForm(paciente);

        model.addAttribute(
                "pacienteForm",
                form
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "paciente/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute PacienteForm pacienteForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PacienteRequest request =
                    pacienteFormMapper.toRequest(
                            pacienteForm
                    );

            if (pacienteForm.getCodigo() == null) {

                pacienteClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Paciente cadastrado com sucesso."
                );

            } else {

                pacienteClient.atualizar(
                        pacienteForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Paciente atualizado com sucesso."
                );
            }

            return "redirect:/pacientes";

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "pacienteForm",
                    pacienteForm
            );

            model.addAttribute(
                    "modo",
                    "novo"
            );

            model.addAttribute(
                    "mensagemErro",
                    "Não foi possível salvar o paciente. "
                            + "Status retornado pela API: "
                            + exception.getStatusCode()
            );

            return "paciente/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {

        try {

            pacienteClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Paciente excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    "Não foi possível excluir o paciente."
            );
        }

        return "redirect:/pacientes";
    }
}