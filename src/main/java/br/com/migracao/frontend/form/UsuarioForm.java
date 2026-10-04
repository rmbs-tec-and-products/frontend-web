package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioForm {

    private Integer codigo;
    private String nome;
    private String login;
    private String email;
    private String senha;
    private String perfil = "USER";
    private Boolean ativo = true;
}