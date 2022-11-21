package entidades;

import java.util.HashMap;

public class Paciente extends Pessoa  {
	private int idPaciente;
	private String responsavel;
	private HashMap<Integer, Atendimento> atendimentos;

	public Paciente() {
		atendimentos = new HashMap<>();
		idPaciente = 0;
	}

	public int getIdPaciente() {
		return idPaciente;
	}


	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}


	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public void setAtendimento(int requisicao, Atendimento atendimento) {
		atendimentos.put(requisicao, atendimento);
	}

	public HashMap<Integer, Atendimento> getAtendimentos() {

		return atendimentos;
	}
}
