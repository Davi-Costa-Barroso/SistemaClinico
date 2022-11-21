package entidades;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;

import arquivos.PDF;

public class Atendimento {
	private int idAtendimento;
	private String dataAtendimento;
	private String formaDePagamento;
	private int idMedicoSolicitante;
	private int idPaciente;
	private double valorTotal;
	private String[] exames;

	public Atendimento() {
		idAtendimento = 0;
		exames = null;

	}

	public int getIdAtendimento() {
		return idAtendimento;
	}

	public void setIdAtendimento(int id) {
		idAtendimento = id;
	}


	public String getDataAtendimento() {
		return dataAtendimento;
	}
	public void setDataAtendimento(String dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}
	public String getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(String formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public int getIdMedicosSolicitante() {
		return idMedicoSolicitante;

	}

	public void setIdMedicoSolicitante(int idMedicoSolicitante) {
		this.idMedicoSolicitante = idMedicoSolicitante;
	}

	public void setExame(String[] examesSolicitados) {
		exames = examesSolicitados;
	}
	

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getExames() {
		String examesSolicitados = "";
		for (String exame : exames) {
			examesSolicitados += exame + "\n";
		}
		
		return examesSolicitados;
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String gerarComprovante(Paciente paciente, Medico medico) throws COSVisitorException, IOException, ClassNotFoundException {
		PDF pdf = new PDF();
		String nomeArquivo = pdf.gerarComprovantePDF(this, paciente, medico);
		return nomeArquivo;
	}
}
