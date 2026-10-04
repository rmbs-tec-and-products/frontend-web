package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.UsuarioClient;
import br.com.migracao.frontend.dto.usuario.UsuarioResponse;
import br.com.migracao.frontend.form.UsuarioForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final UsuarioClient usuarioClient;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false)
            String pesquisa,
            Model model
    ) {
        try {
            model.addAttribute(
                    "usuarios",
                    usuarioClient.listar(
                            pesquisa
                    )
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "usuarios",
                    List.of()
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );
        }

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "admin/usuarios";
    }

    @GetMapping("/novo")
    public String novo(
            Model model
    ) {
        UsuarioForm form =
                new UsuarioForm();

        form.setPerfil(
                "USER"
        );

        form.setAtivo(
                true
        );

        model.addAttribute(
                "usuarioForm",
                form
        );

        model.addAttribute(
                "modo",
                "novo"
        );

        return "admin/usuarios";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable
            Integer codigo,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UsuarioResponse usuario =
                    usuarioClient
                            .buscarPorCodigo(
                                    codigo
                            );

            UsuarioForm form =
                    new UsuarioForm();

            form.setCodigo(
                    usuario.codigo()
            );

            form.setNome(
                    usuario.nome()
            );

            form.setLogin(
                    usuario.login()
            );

            form.setEmail(
                    usuario.email()
            );

            form.setPerfil(
                    usuario.perfil()
            );

            form.setAtivo(
                    usuario.ativo()
            );

            model.addAttribute(
                    "usuarioForm",
                    form
            );

            model.addAttribute(
                    "modo",
                    "novo"
            );

            return "admin/usuarios";

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );

            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute
            UsuarioForm usuarioForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (usuarioForm.getCodigo() == null) {

                usuarioClient.cadastrar(
                        usuarioForm.getNome(),
                        usuarioForm.getLogin(),
                        usuarioForm.getEmail(),
                        usuarioForm.getSenha(),
                        usuarioForm.getPerfil()
                );

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Usuário cadastrado com sucesso."
                        );

            } else {

                usuarioClient.atualizar(
                        usuarioForm.getCodigo(),
                        usuarioForm.getNome(),
                        usuarioForm.getLogin(),
                        usuarioForm.getEmail(),
                        usuarioForm.getPerfil()
                );

                if (usuarioForm.getSenha() != null
                        && !usuarioForm
                        .getSenha()
                        .isBlank()) {

                    usuarioClient.redefinirSenha(
                            usuarioForm.getCodigo(),
                            usuarioForm.getSenha()
                    );
                }

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Usuário atualizado com sucesso."
                        );
            }

            return "redirect:/admin/usuarios";

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "usuarioForm",
                    usuarioForm
            );

            model.addAttribute(
                    "modo",
                    "novo"
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );

            return "admin/usuarios";
        }
    }

    @PostMapping("/{codigo}/status")
    public String alterarStatus(
            @PathVariable
            Integer codigo,
            @RequestParam
            boolean ativo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            usuarioClient.alterarStatus(
                    codigo,
                    ativo
            );

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            ativo
                                    ? "Usuário ativado com sucesso."
                                    : "Usuário desativado com sucesso."
                    );

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/admin/usuarios";
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        try {
            JsonNode json =
                    objectMapper.readTree(
                            exception
                                    .getResponseBodyAsString()
                    );

            JsonNode fields =
                    json.get(
                            "fields"
                    );

            if (fields != null) {

                String mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "nome"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }

                mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "login"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }

                mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "email"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }

                mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "senha"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }

                mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "perfil"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }

                mensagemCampo =
                        obterMensagemCampo(
                                fields,
                                "ativo"
                        );

                if (mensagemCampo != null) {
                    return mensagemCampo;
                }
            }

            JsonNode mensagem =
                    json.get(
                            "message"
                    );

            if (mensagem != null
                    && !mensagem
                    .asText()
                    .isBlank()) {

                return mensagem.asText();
            }

        } catch (Exception ignored) {
        }

        int status =
                exception
                        .getStatusCode()
                        .value();

        if (status == 400) {

            return "Verifique os dados informados.";
        }

        if (status == 404) {

            return "Usuário não encontrado.";
        }

        if (status == 409) {

            return "Não foi possível concluir a operação.";
        }

        if (status == 401
                || status == 403) {

            return "Você não possui permissão para realizar esta operação.";
        }

        return "Não foi possível concluir a operação.";
    }

    private String obterMensagemCampo(
            JsonNode fields,
            String campo
    ) {
        JsonNode valor =
                fields.get(
                        campo
                );

        if (valor == null) {
            return null;
        }

        String mensagem =
                valor.asText();

        if (mensagem == null
                || mensagem.isBlank()) {

            return null;
        }

        return mensagem;
    }
}