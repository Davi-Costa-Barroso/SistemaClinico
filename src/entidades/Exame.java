package entidades;

public class Exame {
	private int idExame;
	private String nome;
	private double preco;

	public Exame(String nome, double preco) {
		this.nome = nome;
		this.preco = preco;
	}

	public int getIdExame() {
		return idExame;
	}

	public void setIdExame(int idExame) {
		this.idExame = idExame;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

}
