package br.com.migracao.frontend.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnamneseForm {

    private boolean algumTratamento;

    private boolean hospitalizado;

    private String motivo = "";

    private boolean alergiaMedicamentoAnestesico;

    private String qualCirurgia = "";

    private boolean transfusaoSangue;

    private boolean bebidaAlcoolica;

    private boolean problemaCardiaco;

    private boolean febreReumatica;

    private String especialidade = "";

    private String quandoHospitalizado = "";

    private boolean tomaMedicamento;

    private String alergia = "";

    private boolean sangraMuito;

    private boolean fumante;

    private boolean desmaioTontura;

    private boolean hiv;

    private boolean tuberculose;

    private String nomeMedico = "";

    private String tempo = "";

    private String medicamento = "";

    private boolean cirurgia;

    private boolean cirurgiaBucal;

    private Integer cigarroDia = 0;

    private boolean respiratorio;

    private boolean hepatite;

    private boolean demoraCicatrizar;

    private boolean depressao;

    private boolean diabete;

    private boolean hipertensao;

    private boolean reumatismo;

    private boolean renal;

    private boolean problemaNervoso;

    private boolean anemia;

    private boolean epilepsia;
}