package arquivos;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import bd.ConexaoBD;
import entidades.Atendimento;
import entidades.Exame;
import entidades.Medico;
import entidades.Paciente;
import exames.Colesterol;
import exames.Glicemia;
import exames.Hemograma;
import exames.Triglicerideos;
import exames.Urina;

public class PDF {
	private String exames = "";
	private String resultados = "";

	public String gerarComprovantePDF(Atendimento atendimento, Paciente paciente, Medico medico)
			throws IOException, COSVisitorException, ClassNotFoundException {
		Locale.setDefault(new Locale("US"));
		PDDocument pDDocument = PDDocument.load(new File("template_comprovante.pdf"));
		PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();

		PDField field = pDAcroForm.getField("DATA");
		field.setValue(atendimento.getDataAtendimento());

		field = pDAcroForm.getField("PACIENTE");
		field.setValue(paciente.getNomeCompleto());

		field = pDAcroForm.getField("NASCIMENTO");
		field.setValue(paciente.getDataNascimento());

		field = pDAcroForm.getField("IDADE");
		field.setValue(paciente.getAnos() + "A " + paciente.getMeses() + "M " + paciente.getDias() + "D");
		field = pDAcroForm.getField("EXAME");

		field.setValue(atendimento.getExames());
		String valorExame = "";
		String[] examesSolicitados = atendimento.getExames().split("\n");

		HashMap<String, Double> exames = new HashMap<String, Double>();
		double preco = 0;
		for (Exame exame : new ConexaoBD().listarExames()) {
			for (int i = 0; i < examesSolicitados.length; i++) {
				if (exame.getNome().equals(examesSolicitados[i])) {
					preco = exame.getPreco();
					exames.put(examesSolicitados[i], preco);

				}
			}
		}

		for (int i = 0; i < examesSolicitados.length; i++) {
			valorExame += "R$ " + String.valueOf(exames.get(examesSolicitados[i])) + "\n";
		}

		field = pDAcroForm.getField("VALOR");
		field.setValue(valorExame);

		field = pDAcroForm.getField("MEDICO");
		field.setValue(medico.getNome());

		field = pDAcroForm.getField("TOTAL");
		field.setValue("R$ " + String.valueOf(atendimento.getValorTotal()));

		field = pDAcroForm.getField("PAGAMENTO");
		field.setValue(atendimento.getFormaDePagamento());

		field = pDAcroForm.getField("REQ");
		field.setValue(String.valueOf(atendimento.getIdAtendimento()));

		String nomeArquivo = "comprovante_" + atendimento.getIdAtendimento() + ".pdf";
		pDDocument.save(nomeArquivo);
		pDDocument.close();

		return nomeArquivo;
	}

	public String gerarResultadoPDF(Atendimento atendimento, Paciente paciente, Medico medico)
			throws IOException, COSVisitorException, ClassNotFoundException {
		Locale.setDefault(new Locale("US"));
		PDDocument pDDocument = PDDocument.load(new File("Resultado_exame_template.pdf"));
		PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();

		PDField field = pDAcroForm.getField("DATA");
		field.setValue(atendimento.getDataAtendimento());

		field = pDAcroForm.getField("PACIENTE");
		field.setValue(paciente.getNomeCompleto());

		field = pDAcroForm.getField("NASCIMENTO");
		field.setValue(paciente.getDataNascimento());

		field = pDAcroForm.getField("IDADE");
		field.setValue(paciente.getAnos() + "A " + paciente.getMeses() + "M " + paciente.getDias() + "D");

		field = pDAcroForm.getField("EXAME");
		PDField field2 = pDAcroForm.getField("RESULTADO");
		if (atendimento.getExames().contains("Hemograma")) {
			Hemograma hemograma = new ConexaoBD().buscarResultadoHemograma(atendimento.getIdAtendimento());
			if (hemograma != null) {
				String exames = "Hemograma" + "\n\n" + "Hemácias" + "\n" + "Hemoglobina" + "\n" + "Hematocrito" + "\n"
						+ "Plaquetas" + "\n" + "CHCM" + "\n" + "Linfócitos" + "\n" + "Leucócitos" + "\n" + "Basofilos"
						+ "\n" + "Bastões" + "\n";

				this.exames = exames;
				String resultados = "\n\n" + hemograma.getHemacias() + " milhões/mm3\n" + hemograma.getHemoglobina()
						+ " g%\n" + hemograma.getHematocrito() + " %\n" + hemograma.getPlaquetas() + " mil/mm3\n"
						+ hemograma.getCHCM() + " %\n" + hemograma.getLinfocitos() + "%\n" + hemograma.getLeucocitos()
						+ " mm3\n" + hemograma.getBasofilos() + " %\n" + hemograma.getBastoes() + " %\n";

				this.resultados = resultados;
			}
		}
		if (atendimento.getExames().contains("Urina")) {
			Urina urina = new ConexaoBD().buscarResultadoUrina(atendimento.getIdAtendimento());
			if (urina != null) {
				String exame = "\nUrina" + "\n\n" + "Cor" + "\n" + "Aspecto" + "\n " + "Cilindros" + "\n" + "Cristais"
						+ "\n" + "Densidade" + "\n " + "Glicose" + "\n " + "PH" + "\n " + "Proteina" + "\n " + "Sangue"
						+ "\n";

				this.exames += exame;

				String resultados = "\n\n\n" + urina.getCor() + "\n" + urina.getAspecto() + "\n" + urina.getCilindros()
						+ "\n" + urina.getCristais() + "\n" + urina.getDensidade() + "\n" + urina.getGlicose() + "\n"
						+ urina.getPH() + "\n" + urina.getProteina() + "\n" + urina.getSangue() + " \n";
				this.resultados += resultados;
			}
		}
		if (atendimento.getExames().contains("Colesterol")) {
			Colesterol colesterol = new ConexaoBD().buscarResultadoColesterol(atendimento.getIdAtendimento());
			if (colesterol != null) {
				String exame = "\nColesterol" + "\n\n" + "Colesterol Total" + "\n" + "Colesterol LDL" + "\n"
						+ "Colesterol HDL" + "\n" + "Colesterol VLDL" + "\n";
				this.exames += exame;

				String resultados = "\n\n\n" + colesterol.getTotal() + " mg/dL\n" + colesterol.getLDL() + " mg/dL\n"
						+ colesterol.getHDL() + " mg/dL\n" + colesterol.getVLDL() + " mg/dL\n";
				this.resultados += resultados;

			}
		}
		if (atendimento.getExames().contains("Glicemia")) {
			Glicemia glicemia = new ConexaoBD().buscarResultadoGlicemia(atendimento.getIdAtendimento());
			if (glicemia != null) {
				String exame = "\nGlicemia" + "\n\n" + "Glicose" + "\n";
				this.exames += exame;

				String resultados = "\n\n\n" + glicemia.getGlicose() + " mg/dL\n";
				this.resultados += resultados;

			}
		}
		if (atendimento.getExames().contains("Triglicerideos")) {
			Triglicerideos triglicerideos = new ConexaoBD()
					.buscarResultadoTriglicerideos(atendimento.getIdAtendimento());
			if (triglicerideos != null) {
				String exame = "\nTriglicerideos" + "\n" + "Total" + "\n";
				this.exames += exame;
				String resultados = "\n\n\n" + triglicerideos.getTotal() + " mg/dL\n";
				this.resultados += resultados;
			}
		}

		field.setValue(exames);
		field2.setValue(resultados);

		field = pDAcroForm.getField("MEDICO");
		field.setValue(medico.getNome());

		field = pDAcroForm.getField("REQUISICAO");
		field.setValue(String.valueOf(atendimento.getIdAtendimento()));

		String nomeArquivo = "resultado_" + atendimento.getIdAtendimento() + ".pdf";
		pDDocument.save(nomeArquivo);
		pDDocument.close();
		return nomeArquivo;
	}

}