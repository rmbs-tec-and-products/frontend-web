package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.AgendaClient;
import br.com.migracao.frontend.dto.agenda.AgendaRequest;
import br.com.migracao.frontend.dto.agenda.AgendaResponse;
import br.com.migracao.frontend.form.AgendaForm;
import br.com.migracao.frontend.mapper.AgendaFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Controller
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaClient agendaClient;
    private final AgendaFormMapper agendaFormMapper;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,

            @RequestParam(required = false)
            Integer dentistaCodigo,

            Model model
    ) {
        LocalDate dataSelecionada =
                data != null
                        ? data
                        : LocalDate.now();

        model.addAttribute(
                "agendamentos",
                agendaClient.listar(
                        dataSelecionada,
                        dentistaCodigo
                )
        );

        model.addAttribute(
                "dentistas",
                agendaClient.listarDentistas()
        );

        model.addAttribute(
                "dataSelecionada",
                dataSelecionada
        );

        model.addAttribute(
                "dentistaCodigo",
                dentistaCodigo
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "agenda/index";
    }

    @GetMapping("/novo")
    public String novo(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,

            @RequestParam(required = false)
            Integer dentistaCodigo,

            Model model
    ) {
        AgendaForm form =
                new AgendaForm();

        form.setData(
                data != null
                        ? data
                        : LocalDate.now()
        );

        form.setDentistaCodigo(
                dentistaCodigo
        );

        prepararFormulario(
                model,
                form
        );

        return "agenda/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {
        AgendaResponse response =
                agendaClient.buscarPorCodigo(codigo);

        AgendaForm form =
                agendaFormMapper.toForm(response);

        prepararFormulario(
                model,
                form
        );

        return "agenda/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute AgendaForm agendaForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            AgendaRequest request =
                    agendaFormMapper.toRequest(
                            agendaForm
                    );

            if (agendaForm.getCodigo() == null) {

                agendaClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Agendamento cadastrado com sucesso."
                );

            } else {

                agendaClient.atualizar(
                        agendaForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Agendamento atualizado com sucesso."
                );
            }

            String redirect =
                    "redirect:/agenda?data="
                            + agendaForm.getData();

            if (agendaForm.getDentistaCodigo() != null) {
                redirect +=
                        "&dentistaCodigo="
                                + agendaForm.getDentistaCodigo();
            }

            return redirect;

        } catch (RestClientResponseException exception) {

            prepararFormulario(
                    model,
                    agendaForm
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "agenda/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,

            @RequestParam(required = false)
            Integer dentistaCodigo,

            RedirectAttributes redirectAttributes
    ) {
        try {
            agendaClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Agendamento excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        String redirect =
                "redirect:/agenda?data=" + data;

        if (dentistaCodigo != null) {
            redirect +=
                    "&dentistaCodigo="
                            + dentistaCodigo;
        }

        return redirect;
    }

    private void prepararFormulario(
            Model model,
            AgendaForm form
    ) {
        model.addAttribute(
                "agendaForm",
                form
        );

        model.addAttribute(
                "pacientes",
                agendaClient.listarPacientes()
        );

        model.addAttribute(
                "dentistas",
                agendaClient.listarDentistas()
        );

        model.addAttribute(
                "modo",
                "novo"
        );
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        try {
            JsonNode json =
                    objectMapper.readTree(
                            exception.getResponseBodyAsString()
                    );

            JsonNode mensagem =
                    json.get("message");

            if (mensagem != null
                    && !mensagem.asText().isBlank()) {

                return mensagem.asText();
            }

        } catch (Exception ignored) {
        }

        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados do agendamento.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "Paciente, dentista ou agendamento não encontrado.";
        }

        return "Não foi possível concluir a operação.";
    }
}