package entidades;

public abstract class Pessoa {
	public int idPessoa;
	private String nomeCompleto;
	private String dataNascimento;
	private int anos;
	private int meses;
	private int dias;

	private String telefone;
	private String cpf;

	public Pessoa() {
		nomeCompleto = "";
		dataNascimento = "";
		telefone = "";
		cpf = "";
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public int getDias() {
		return dias;
	}

	public int getMeses() {
		return meses;
	}

	public int getAnos() {
		return anos;
	}

	public void setAnos(int anos) {
		this.anos = anos;
	}

	public void setMeses(int meses) {
		this.meses = meses;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

}
