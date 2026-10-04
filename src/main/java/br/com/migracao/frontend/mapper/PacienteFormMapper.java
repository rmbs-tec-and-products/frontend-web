package br.com.migracao.frontend.mapper;

import br.com.migracao.frontend.dto.paciente.AnamneseRequest;
import br.com.migracao.frontend.dto.paciente.AnamneseResponse;
import br.com.migracao.frontend.dto.paciente.PacienteRequest;
import br.com.migracao.frontend.dto.paciente.PacienteResponse;
import br.com.migracao.frontend.form.AnamneseForm;
import br.com.migracao.frontend.form.PacienteForm;
import org.springframework.stereotype.Component;

@Component
public class PacienteFormMapper {

    public PacienteRequest toRequest(PacienteForm form) {

        return new PacienteRequest(
                form.getNome(),
                form.getTelefone(),
                form.getCelular(),
                form.getCpf(),
                form.getDataNascimento() == null
                        ? null
                        : form.getDataNascimento().atStartOfDay(),
                form.getSexo(),
                form.getEndereco(),
                form.getNumero(),
                form.getCep(),
                form.getCidade(),
                form.getEstado(),
                form.getBairro(),
                toAnamneseRequest(form.getAnamnese())
        );
    }

    public PacienteForm toForm(PacienteResponse response) {

        PacienteForm form = new PacienteForm();

        form.setCodigo(response.codigo());
        form.setNome(response.nome());
        form.setTelefone(response.telefone());
        form.setCelular(response.celular());
        form.setCpf(response.cpf());

        if (response.dataNascimento() != null) {
            form.setDataNascimento(
                    response.dataNascimento().toLocalDate()
            );
        }

        form.setSexo(response.sexo());
        form.setEndereco(response.endereco());
        form.setNumero(response.numero());
        form.setCep(response.cep());
        form.setCidade(response.cidade());
        form.setEstado(response.estado());
        form.setBairro(response.bairro());

        form.setAnamnese(
                toAnamneseForm(response.anamnese())
        );

        return form;
    }

    private AnamneseRequest toAnamneseRequest(
            AnamneseForm form
    ) {

        if (form == null) {
            form = new AnamneseForm();
        }

        return new AnamneseRequest(
                form.isAlgumTratamento(),
                form.isHospitalizado(),
                value(form.getMotivo()),
                form.isAlergiaMedicamentoAnestesico(),
                value(form.getQualCirurgia()),
                form.isTransfusaoSangue(),
                form.isBebidaAlcoolica(),
                form.isProblemaCardiaco(),
                form.isFebreReumatica(),
                value(form.getEspecialidade()),
                value(form.getQuandoHospitalizado()),
                form.isTomaMedicamento(),
                value(form.getAlergia()),
                form.isSangraMuito(),
                form.isFumante(),
                form.isDesmaioTontura(),
                form.isHiv(),
                form.isTuberculose(),
                value(form.getNomeMedico()),
                value(form.getTempo()),
                value(form.getMedicamento()),
                form.isCirurgia(),
                form.isCirurgiaBucal(),
                form.getCigarroDia() == null
                        ? 0
                        : form.getCigarroDia(),
                form.isRespiratorio(),
                form.isHepatite(),
                form.isDemoraCicatrizar(),
                form.isDepressao(),
                form.isDiabete(),
                form.isHipertensao(),
                form.isReumatismo(),
                form.isRenal(),
                form.isProblemaNervoso(),
                form.isAnemia(),
                form.isEpilepsia()
        );
    }

    private AnamneseForm toAnamneseForm(
            AnamneseResponse response
    ) {

        AnamneseForm form = new AnamneseForm();

        if (response == null) {
            return form;
        }

        form.setAlgumTratamento(response.algumTratamento());
        form.setHospitalizado(response.hospitalizado());
        form.setMotivo(value(response.motivo()));
        form.setAlergiaMedicamentoAnestesico(
                response.alergiaMedicamentoAnestesico()
        );
        form.setQualCirurgia(value(response.qualCirurgia()));
        form.setTransfusaoSangue(response.transfusaoSangue());
        form.setBebidaAlcoolica(response.bebidaAlcoolica());
        form.setProblemaCardiaco(response.problemaCardiaco());
        form.setFebreReumatica(response.febreReumatica());
        form.setEspecialidade(value(response.especialidade()));
        form.setQuandoHospitalizado(
                value(response.quandoHospitalizado())
        );
        form.setTomaMedicamento(response.tomaMedicamento());
        form.setAlergia(value(response.alergia()));
        form.setSangraMuito(response.sangraMuito());
        form.setFumante(response.fumante());
        form.setDesmaioTontura(
                Boolean.TRUE.equals(response.desmaioTontura())
        );
        form.setHiv(response.hiv());
        form.setTuberculose(response.tuberculose());
        form.setNomeMedico(value(response.nomeMedico()));
        form.setTempo(value(response.tempo()));
        form.setMedicamento(value(response.medicamento()));
        form.setCirurgia(response.cirurgia());
        form.setCirurgiaBucal(response.cirurgiaBucal());
        form.setCigarroDia(
                response.cigarroDia() == null
                        ? 0
                        : response.cigarroDia()
        );
        form.setRespiratorio(response.respiratorio());
        form.setHepatite(response.hepatite());
        form.setDemoraCicatrizar(response.demoraCicatrizar());
        form.setDepressao(response.depressao());
        form.setDiabete(response.diabete());
        form.setHipertensao(response.hipertensao());
        form.setReumatismo(response.reumatismo());
        form.setRenal(response.renal());
        form.setProblemaNervoso(response.problemaNervoso());
        form.setAnemia(response.anemia());
        form.setEpilepsia(
                Boolean.TRUE.equals(response.epilepsia())
        );

        return form;
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}