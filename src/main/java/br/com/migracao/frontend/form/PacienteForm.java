package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PacienteForm {

    private Integer codigo;

    private String nome;

    private String telefone;

    private String celular;

    private String cpf;

    private LocalDate dataNascimento;

    private String sexo;

    private String endereco;

    private String numero;

    private String cep;

    private String cidade;

    private String estado;

    private String bairro;

    private AnamneseForm anamnese = new AnamneseForm();
}