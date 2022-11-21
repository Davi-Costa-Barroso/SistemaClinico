package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import entidades.Atendimento;
import entidades.Exame;
import entidades.Medico;
import entidades.Paciente;
import exames.Colesterol;
import exames.Glicemia;
import exames.Hemograma;
import exames.Triglicerideos;
import exames.Urina;

public class ConexaoBD {
	public static Connection connect;

	public void conectarBancoDeDados() {
		try {
			Class.forName("org.sqlite.JDBC"); // Carrega o driver do banco de dados
			connect = DriverManager.getConnection("jdbc:sqlite:dados.db"); // Cria a conexão com o bd
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro de conexão");
			System.exit(0);
		}

	}

	public void criarTabelaPaciente() {
		try {
			Statement statement = connect.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Paciente" + "(id_paciente INTEGER PRIMARY KEY AUTOINCREMENT ,"
					+ "nome_completo VARCHAR NOT NULL," + "data_nascimento VARCHAR NOT NULL," + "anos INTEGER NOT NULL,"
					+ "meses INTEGER NOT NULL," + "dias INTEGER NOT NULL," + "telefone VARCHAR NOT NULL,"
					+ "cpf VARCHAR NOT NULL," + "responsavel VARCHAR)";
			statement.execute(sql);
			statement.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA PACIENTE");
		}
	}

	public void adicionarPaciente(Paciente paciente) {
		try {
			PreparedStatement preparedStatement = connect.prepareStatement(
					"INSERT into Paciente (nome_completo, data_nascimento, anos, meses, dias, telefone, cpf, responsavel) values (?,?,?,?,?,?,?,?)");

			preparedStatement.setString(1, paciente.getNomeCompleto());
			preparedStatement.setString(2, paciente.getDataNascimento());
			preparedStatement.setInt(3, paciente.getAnos());
			preparedStatement.setInt(4, paciente.getMeses());
			preparedStatement.setInt(5, paciente.getDias());
			preparedStatement.setString(6, paciente.getTelefone());
			preparedStatement.setString(7, paciente.getCpf());
			preparedStatement.setString(8, paciente.getResponsavel());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR PACIENTE");
		}
	}

	public Paciente buscarPacientePorCpf(String cpf) {
		try {
			Statement statement = connect.createStatement();
			String sql = "SELECT * from Paciente WHERE cpf='" + cpf + "';";
			ResultSet rs_paciente = statement.executeQuery(sql);

			while (rs_paciente.next()) {
				if (rs_paciente.getString("cpf").equals(cpf)) {
					Paciente paciente = new Paciente();
					paciente.setIdPaciente(rs_paciente.getInt("id_paciente"));
					paciente.setCpf(cpf);
					paciente.setDataNascimento(rs_paciente.getString("data_nascimento"));
					paciente.setNomeCompleto(rs_paciente.getString("nome_completo"));
					paciente.setAnos(rs_paciente.getInt("anos"));
					paciente.setMeses(rs_paciente.getInt("meses"));
					paciente.setDias(rs_paciente.getInt("dias"));
					paciente.setTelefone(rs_paciente.getString("telefone"));
					paciente.setResponsavel(rs_paciente.getString("responsavel"));
					return paciente;
				}
			}
			rs_paciente.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - BUSCAR PACIENTE");
		}
		return null;
	}

	public Paciente buscarPacientePorId(int idPaciente) {
		try {
			Statement statement = connect.createStatement();
			String sql = "SELECT * from Paciente";
			ResultSet rs_paciente = statement.executeQuery(sql);

			while (rs_paciente.next()) {
				if (rs_paciente.getInt("id_paciente") == idPaciente) {
					Paciente paciente = new Paciente();
					paciente.setIdPaciente(rs_paciente.getInt("id_paciente"));
					paciente.setCpf(rs_paciente.getString("cpf"));
					paciente.setDataNascimento(rs_paciente.getString("data_nascimento"));
					paciente.setNomeCompleto(rs_paciente.getString("nome_completo"));
					paciente.setAnos(rs_paciente.getInt("anos"));
					paciente.setMeses(rs_paciente.getInt("meses"));
					paciente.setDias(rs_paciente.getInt("dias"));
					paciente.setTelefone(rs_paciente.getString("telefone"));
					paciente.setResponsavel(rs_paciente.getString("responsavel"));
					return paciente;
				}
			}
			rs_paciente.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - BUSCAR PACIENTE");
		}
		return null;
	}

	public List<Paciente> listarPacientes() {
		List<Paciente> pacientes = new LinkedList<>();

		try {
			Statement statement = connect.createStatement();
			String sql = "SELECT * from Paciente";
			ResultSet rs_paciente = statement.executeQuery(sql);

			while (rs_paciente.next()) {
				Paciente paciente = new Paciente();
				paciente.setIdPaciente(rs_paciente.getInt("id_paciente"));
				paciente.setCpf(rs_paciente.getString("cpf"));
				paciente.setNomeCompleto(rs_paciente.getString("nome_completo"));
				paciente.setAnos(rs_paciente.getInt("anos"));
				paciente.setDataNascimento(rs_paciente.getString("data_nascimento"));
				paciente.setMeses(rs_paciente.getInt("meses"));
				paciente.setDias(rs_paciente.getInt("dias"));
				paciente.setTelefone(rs_paciente.getString("telefone"));
				paciente.setResponsavel(rs_paciente.getString("responsavel"));
				pacientes.add(paciente);
			}

			rs_paciente.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA EXAME");
		}

		return pacientes;
	}

	public void atualizarPaciente(Paciente paciente) {
		try {
			String sql = "UPDATE Paciente SET nome_completo='" + paciente.getNomeCompleto() + "', data_nascimento='"
					+ paciente.getDataNascimento() + "', anos=" + paciente.getAnos() + ", meses=" + paciente.getMeses()
					+ ", dias=" + paciente.getDias() + ", telefone='" + paciente.getTelefone() + "', cpf='"
					+ paciente.getCpf() + "', responsavel='" + paciente.getResponsavel() + "' WHERE id_paciente=" + paciente.getIdPaciente() + ";";
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void criarTabelaMedico() {
		try {
			Statement statement = connect.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Medico" + "(id_medico INTEGER PRIMARY KEY AUTOINCREMENT ,"
					+ "nome VARCHAR NOT NULL," + "CRM VARCHAR NOT NULL)";
			statement.execute(sql);

			String comando = "SELECT * FROM Medico";
			ResultSet rs = statement.executeQuery(comando);

			if (!rs.next()) {
				Medico medico = new Medico();
				medico.setCRM("000000");
				medico.setNome("Sem solicitação");
				adicionarMedico(medico);
			}
			statement.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA MEDICO");
		}
	}

	public void adicionarMedico(Medico medico) {
		try {
			PreparedStatement preparedStatement = connect
					.prepareStatement("insert into Medico (nome, CRM) values (?,?)");

			preparedStatement.setString(1, medico.getNome());
			preparedStatement.setString(2, medico.getCRM());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR MEDICO");
		}
	}

	public Medico buscarMedicoPorCRM(String CRM) {
		ResultSet rs_medico = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql_medico = "SELECT * from Medico";
			rs_medico = statement.executeQuery(sql_medico);

			while (rs_medico.next()) {
				if (rs_medico.getString("CRM").equals(CRM)) {
					Medico medico = new Medico();
					medico.setIdMedico(rs_medico.getInt("id_medico"));
					medico.setCRM(CRM);
					medico.setNome(rs_medico.getString("nome"));
					return medico;
				}
			}

			rs_medico.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Medico buscarMedicoPorId(int idMedico) {
		Medico medico = new Medico();

		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql_medico = "SELECT * from Medico WHERE id_medico=" + idMedico + ";";
			ResultSet rs_medico = statement.executeQuery(sql_medico);

			medico.setIdMedico(rs_medico.getInt("id_medico"));
			medico.setCRM(rs_medico.getString("CRM"));
			medico.setNome(rs_medico.getString("nome"));

			rs_medico.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return medico;
	}

	public void tabelaExames() {
		try {
			Statement statement = connect.createStatement();
			String sql_exames = "CREATE TABLE IF NOT EXISTS Exames" + "(id_exame INTEGER PRIMARY KEY AUTOINCREMENT ,"
					+ "nome_exame VARCHAR NOT NULL ," + "preco DOUBLE NOT NULL)";
			statement.execute(sql_exames);

			String comando = "SELECT * FROM Exames";
			ResultSet rs = statement.executeQuery(comando);

			if (!rs.next()) {
				HashMap<String, Double> listaExames = new HashMap<>();
				listaExames.put("Urina", 13.75);
				listaExames.put("Hemograma", 6.65);
				listaExames.put("Glicemia", 8.12);
				listaExames.put("Triglicerideos", 6.04);
				listaExames.put("Colesterol", 25.99);
				PreparedStatement preparedStatement = connect
						.prepareStatement("insert into Exames (nome_exame, preco) values (?,?)");

				for (String key : listaExames.keySet()) {
					preparedStatement.setString(1, key);
					preparedStatement.setDouble(2, listaExames.get(key));
					preparedStatement.executeUpdate();
				}
			}
			statement = connect.createStatement();
			String sql_hemograma = "CREATE TABLE IF NOT EXISTS Hemograma" + "(hemacias DOUBLE NOT NULL ,"
					+ "hemoglobina DOUBLE NOT NULL ," + "plaquetas DOUBLE NOT NULL ," + "chcm DOUBLE NOT NULL ,"
					+ "hematocrito DOUBLE NOT NULL ," + "linfocitos DOUBLE NOT NULL ," + "leucocitos DOUBLE NOT NULL, "
					+ "basofilos DOUBLE NOT NULL ," + "bastoes DOUBLE NOT NULL ," + "id_atendimento INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_atendimento) REFERENCES ATENDIMENTO (id_atendimento))";
			statement.execute(sql_hemograma);

			String sql_urina = "CREATE TABLE IF NOT EXISTS Urina" + "(aspecto VARCHAR NOT NULL ,"
					+ "cilindros DOUBLE NOT NULL ," + "cor VARCHAR NOT NULL ," + "cristais VARCHAR NOT NULL ,"
					+ "densidade DOUBLE NOT NULL ," + "glicose VARCHAR NOT NULL ," + "ph INTEGER NOT NULL ,"
					+ "proteina VARCHAR NOT NULL ," + "sangue VARCHAR NOT NULL ," + "id_atendimento INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_atendimento) REFERENCES ATENDIMENTO (id_atendimento))";
			statement.execute(sql_urina);

			String sql_glicemia = "CREATE TABLE IF NOT EXISTS Glicemia" + "(glicose DOUBLE NOT NULL ,"
					+ "id_atendimento INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_atendimento) REFERENCES ATENDIMENTO (id_atendimento))";
			statement.execute(sql_glicemia);

			String sql_colesterol = "CREATE TABLE IF NOT EXISTS Colesterol" + "(LDL DOUBLE NOT NULL ,"
					+ "total DOUBLE NOT NULL ," + "HDL DOUBLE NOT NULL ," + "VLDL DOUBLE NOT NULL ,"
					+ "id_atendimento INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_atendimento) REFERENCES ATENDIMENTO (id_atendimento))";
			statement.execute(sql_colesterol);

			String sql_triglicerideos = "CREATE TABLE IF NOT EXISTS Triglicerideos" + "(total DOUBLE NOT NULL ,"
					+ "id_atendimento INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_atendimento) REFERENCES ATENDIMENTO (id_atendimento))";
			statement.execute(sql_triglicerideos);

			statement.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA EXAME");
		}
	}

	public void adicionarResultadosHemograma(Hemograma hemograma, int idAtendimento) {
		try {

			Hemograma hemogramaBuscado = buscarResultadoHemograma(idAtendimento);
			if (hemogramaBuscado != null) {

				String sql = "UPDATE Hemograma set hemacias = ?, hemoglobina = ?, "
						+ "basofilos = ?, bastoes = ?, chcm = ?, leucocitos = ?, plaquetas = ?, linfocitos = ?, hematocrito = ? where id_atendimento= ?";
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setDouble(1, hemograma.getHemacias());
				preparedStatement.setDouble(2, hemograma.getHemoglobina());
				preparedStatement.setDouble(3, hemograma.getBasofilos());
				preparedStatement.setDouble(4, hemograma.getBastoes());
				preparedStatement.setDouble(5, hemograma.getCHCM());
				preparedStatement.setDouble(6, hemograma.getLeucocitos());
				preparedStatement.setDouble(7, hemograma.getPlaquetas());
				preparedStatement.setDouble(8, hemograma.getLinfocitos());
				preparedStatement.setDouble(9, hemograma.getHematocrito());
				preparedStatement.setInt(10, idAtendimento);

				preparedStatement.executeUpdate();
				preparedStatement.close();

			} else {
				PreparedStatement preparedStatement = connect.prepareStatement(
						"INSERT into Hemograma (hemacias, hemoglobina, basofilos, bastoes, chcm, leucocitos, plaquetas, linfocitos, hematocrito, id_atendimento) values (?,?,?,?,?,?,?,?,?,?)");

				preparedStatement.setDouble(1, hemograma.getHemacias());
				preparedStatement.setDouble(2, hemograma.getHemoglobina());
				preparedStatement.setDouble(3, hemograma.getBasofilos());
				preparedStatement.setDouble(4, hemograma.getBastoes());
				preparedStatement.setDouble(5, hemograma.getCHCM());
				preparedStatement.setDouble(6, hemograma.getLeucocitos());
				preparedStatement.setDouble(7, hemograma.getPlaquetas());
				preparedStatement.setDouble(8, hemograma.getLinfocitos());
				preparedStatement.setDouble(9, hemograma.getHematocrito());
				preparedStatement.setInt(10, idAtendimento);

				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR RESULTADO HEMOGRAMA");
		}
	}

	public void adicionarResultadosUrina(Urina urina, int idAtendimento) {
		Urina urinaBuscado = buscarResultadoUrina(idAtendimento);
		try {
			if (urinaBuscado != null) {
				String sql = "UPDATE Urina set cor = ?, densidade = ?, "
						+ "ph = ?, aspecto = ?, proteina = ?, glicose = ?, sangue = ?, cristais = ?, cilindros = ? where id_atendimento= ?";
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setString(1, urina.getCor());
				preparedStatement.setDouble(2, urina.getDensidade());
				preparedStatement.setInt(3, urina.getPH());
				preparedStatement.setString(4, urina.getAspecto());
				preparedStatement.setString(5, urina.getProteina());
				preparedStatement.setString(6, urina.getGlicose());
				preparedStatement.setString(7, urina.getSangue());
				preparedStatement.setString(8, urina.getCristais());
				preparedStatement.setString(9, urina.getCilindros());
				preparedStatement.setInt(10, idAtendimento);

				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				PreparedStatement preparedStatement = connect.prepareStatement(
						"INSERT into Urina (cor, densidade, ph, aspecto, proteina, glicose, sangue, cristais, cilindros, id_atendimento) values (?,?,?,?,?,?,?,?,?,?)");

				preparedStatement.setString(1, urina.getCor());
				preparedStatement.setDouble(2, urina.getDensidade());
				preparedStatement.setDouble(3, urina.getPH());
				preparedStatement.setString(4, urina.getAspecto());
				preparedStatement.setString(5, urina.getProteina());
				preparedStatement.setString(6, urina.getGlicose());
				preparedStatement.setString(7, urina.getSangue());
				preparedStatement.setString(8, urina.getCristais());
				preparedStatement.setString(9, urina.getCilindros());
				preparedStatement.setInt(10, idAtendimento);

				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR RESULTADO URINA");
		}
	}

	public void adicionarResultadosColesterol(Colesterol colesterol, int idAtendimento) {
		Colesterol colesterolBuscado = buscarResultadoColesterol(idAtendimento);
		try {
			if (colesterolBuscado != null) {
				System.out.println("a");
				String sql = "UPDATE Colesterol set total = ?, LDL = ?, " + "HDL = ?, VLDL = ? where id_atendimento= ?";
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setDouble(1, colesterol.getTotal());
				preparedStatement.setDouble(2, colesterol.getLDL());
				preparedStatement.setDouble(3, colesterol.getHDL());
				preparedStatement.setDouble(4, colesterol.getVLDL());
				preparedStatement.setInt(5, idAtendimento);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				PreparedStatement preparedStatement = connect.prepareStatement(
						"INSERT into Colesterol (total, LDL, HDL, VLDL, id_atendimento) values (?,?,?,?,?)");

				preparedStatement.setDouble(1, colesterol.getTotal());
				preparedStatement.setDouble(2, colesterol.getLDL());
				preparedStatement.setDouble(3, colesterol.getHDL());
				preparedStatement.setDouble(4, colesterol.getVLDL());
				preparedStatement.setInt(5, idAtendimento);

				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR RESULTADO COLESTEROL");
		}
	}

	public void adicionarResultadosGlicemia(Glicemia glicemia, int idAtendimento) {
		Glicemia glicemiaBuscada = buscarResultadoGlicemia(idAtendimento);
		try {
			if (glicemiaBuscada != null) {
				String sql = "UPDATE Glicemia set glicose = ? where id_atendimento= ?";
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setDouble(1, glicemia.getGlicose());
				preparedStatement.setInt(2, idAtendimento);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} else {
				PreparedStatement preparedStatement = connect
						.prepareStatement("INSERT into Glicemia (glicose, id_atendimento) values (?,?)");

				preparedStatement.setDouble(1, glicemia.getGlicose());
				preparedStatement.setInt(2, idAtendimento);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR RESULTADO GLICOSE");
		}
	}

	public void adicionarResultadosTriglicerideos(Triglicerideos triglicerideos, int idAtendimento) {
		Triglicerideos triglicerideosBuscado = buscarResultadoTriglicerideos(idAtendimento);
		try {

			if (triglicerideosBuscado != null) {
				String sql = "UPDATE Triglicerideos set total = ? where id_atendimento= ?";
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setDouble(1, triglicerideos.getTotal());
				preparedStatement.setInt(2, idAtendimento);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}

			else {
				PreparedStatement preparedStatement = connect
						.prepareStatement("INSERT into Triglicerideos (total, id_atendimento) values (?,?)");

				preparedStatement.setDouble(1, triglicerideos.getTotal());
				preparedStatement.setInt(2, idAtendimento);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR RESULTADO TRIGLICERIDEOS");
		}
	}

	public Hemograma buscarResultadoHemograma(int idAtendimento) {
		Hemograma hemograma = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Hemograma";
			ResultSet rs_hemograma = statement.executeQuery(sql);

			while (rs_hemograma.next()) {
				if (rs_hemograma.getInt("id_atendimento") == idAtendimento) {
					hemograma = new Hemograma();
					hemograma.setHemacias(rs_hemograma.getDouble("hemacias"));
					hemograma.setHemoglobina(rs_hemograma.getDouble("hemoglobina"));
					hemograma.setPlaquetas(rs_hemograma.getDouble("plaquetas"));
					hemograma.setCHCM(rs_hemograma.getDouble("chcm"));
					hemograma.setHematocrito(rs_hemograma.getDouble("hematocrito"));
					hemograma.setLinfocitos(rs_hemograma.getDouble("linfocitos"));
					hemograma.setLeucocitos(rs_hemograma.getDouble("leucocitos"));
					hemograma.setBasofilos(rs_hemograma.getDouble("basofilos"));
					hemograma.setBastoes(rs_hemograma.getDouble("bastoes"));
					hemograma.setIdAtendimento(idAtendimento);
				}
			}

			rs_hemograma.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA ATENDIMENTO POR PACIENTE");
		}

		return hemograma;

	}

	public Urina buscarResultadoUrina(int idAtendimento) {
		Urina urina = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Urina";
			ResultSet rs_urina = statement.executeQuery(sql);

			while (rs_urina.next()) {
				if (rs_urina.getInt("id_atendimento") == idAtendimento) {
					urina = new Urina();
					urina.setAspecto(rs_urina.getString("aspecto"));
					urina.setCilindros(rs_urina.getString("cilindros"));
					urina.setCor(rs_urina.getString("cor"));
					urina.setCristais(rs_urina.getString("cristais"));
					urina.setDensidade(rs_urina.getDouble("densidade"));
					urina.setGlicose(rs_urina.getString("glicose"));
					urina.setPH(rs_urina.getInt("ph"));
					urina.setProteina(rs_urina.getString("proteina"));
					urina.setSangue(rs_urina.getString("sangue"));
					urina.setIdAtendimento(idAtendimento);
				}
			}

			rs_urina.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA BUSCAR URINA");
		}

		return urina;

	}

	public Colesterol buscarResultadoColesterol(int idAtendimento) {
		Colesterol colesterol = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Colesterol";
			ResultSet rs_colesterol = statement.executeQuery(sql);

			while (rs_colesterol.next()) {
				if (rs_colesterol.getInt(("id_atendimento")) == idAtendimento) {
					colesterol = new Colesterol();
					colesterol.setTotal(rs_colesterol.getDouble("total"));
					colesterol.setHDL(rs_colesterol.getDouble("HDL"));
					colesterol.setLDL(rs_colesterol.getDouble("LDL"));
					colesterol.setVLDL(rs_colesterol.getDouble("VLDL"));
					colesterol.setIdAtendimento(idAtendimento);
					
				}

			}

			rs_colesterol.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA BUSCAR COLESTEROL");
		}

		return colesterol;

	}

	public Glicemia buscarResultadoGlicemia(int idAtendimento) {
		Glicemia glicemia = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Glicemia";
			ResultSet rs_glicemia = statement.executeQuery(sql);

			while (rs_glicemia.next()) {
				if (rs_glicemia.getInt("id_atendimento") == idAtendimento) {
					glicemia = new Glicemia();
					glicemia.setGlicose(rs_glicemia.getDouble("glicose"));
					glicemia.setIdAtendimento(idAtendimento);					
				}
			}
			rs_glicemia.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA BUSCAR GLICOSE");
		}

		return glicemia;

	}

	public Triglicerideos buscarResultadoTriglicerideos(int idAtendimento) {
		Triglicerideos triglicerideos = null;
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Triglicerideos";
			ResultSet rs_triglicerideos = statement.executeQuery(sql);

			while (rs_triglicerideos.next()) {
				if (rs_triglicerideos.getInt("id_atendimento") == idAtendimento) {
					triglicerideos = new Triglicerideos();
					triglicerideos.setTotal(rs_triglicerideos.getDouble("total"));
					triglicerideos.setIdAtendimento(idAtendimento);
				}
			}
			rs_triglicerideos.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA BUSCAR TRIGLICERIDEOS");
		}

		return triglicerideos;

	}

	public List<Exame> listarExames() {
		List<Exame> exames = new LinkedList<>();

		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Exames";
			ResultSet rs_exame = statement.executeQuery(sql);

			while (rs_exame.next()) {
				exames.add(new Exame(rs_exame.getString("nome_exame"), rs_exame.getDouble("preco")));
			}

			rs_exame.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA EXAME");
		}

		return exames;
	}

	public void criarTabelaFuncionario() {
		try {
			Statement statement = connect.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Funcionario"
					+ "(id_funcionario INTEGER PRIMARY KEY AUTOINCREMENT ," + "nome_completo VARCHAR NOT NULL,"
					+ "data_nascimento VARCHAR NOT NULL," + "telefone VARCHAR NOT NULL," + "cpf VARCHAR NOT NULL,"
					+ "usuario VARCHAR NOT NULL," + "senha VARCHAR NOT NULL)";
			statement.execute(sql);

			String comando = "SELECT * FROM Funcionario";
			ResultSet rs = statement.executeQuery(comando);

			if (!rs.next()) {
				adicionarFuncionario("", "", "", "", "admin", "admin");
			}

			statement.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA FUNCIONARIO");
		}
	}

	public void adicionarFuncionario(String nome_completo, String data_nascimento, String telefone, String cpf,
			String usuario, String senha) {
		try {
			PreparedStatement preparedStatement = connect.prepareStatement(
					"INSERT into Funcionario (nome_completo, data_nascimento, telefone, cpf, usuario, senha) values (?,?,?,?,?,?)");

			preparedStatement.setString(1, nome_completo);
			preparedStatement.setString(2, data_nascimento);
			preparedStatement.setString(3, telefone);
			preparedStatement.setString(4, cpf);
			preparedStatement.setString(5, usuario);
			preparedStatement.setString(6, senha);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - ADICIONAR FUNCIONARIO");
		}
	}

	public void editarFuncionario(String nomeCompleto, String dataNascimento, String telefone, String cpf,
			String usuario, String senha, int idFuncionario) {
		try {
			String sql = "UPDATE Funcionario set nome_completo = ?, data_nascimento = ?, "
					+ "telefone = ?, cpf = ?, usuario = ?," + " senha = ? where id_funcionario = ?";
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, nomeCompleto);
			preparedStatement.setString(2, dataNascimento);
			preparedStatement.setString(3, telefone);
			preparedStatement.setString(4, cpf);
			preparedStatement.setString(5, usuario);
			preparedStatement.setString(6, senha);
			preparedStatement.setInt(7, idFuncionario);
			preparedStatement.execute();
			preparedStatement.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - EDITAR FUNCIONARIO");
		}
	}

	public void criarTabelaAtendimento() {
		try {
			Statement statement = connect.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Atendimento"
					+ "(id_atendimento INTEGER PRIMARY KEY AUTOINCREMENT ," + "data VARCHAR NOT NULL,"
					+ "forma_pagamento VARCHAR NOT NULL," + "valor_total DOUBLE NOT NULL," + "exames VARCHAR NOT NULL,"
					+ "id_paciente INTEGER NOT NULL," + "id_medico INTEGER NOT NULL,"
					+ "FOREIGN KEY(id_paciente) REFERENCES PACIENTE(id_paciente)"
					+ "FOREIGN KEY(id_medico) REFERENCES MEDICO(id_medico))";
			statement.execute(sql);
			statement.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA ATENDIMENTO");
		}
	}

	public void adicionarAtendimento(Atendimento atendimento) {
		try {
			String sql = "INSERT into Atendimento (data, forma_pagamento, valor_total, exames, id_medico, id_paciente) values (?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, atendimento.getDataAtendimento());
			preparedStatement.setString(2, atendimento.getFormaDePagamento());
			preparedStatement.setDouble(3, atendimento.getValorTotal());
			preparedStatement.setString(4, atendimento.getExames());
			preparedStatement.setInt(5, atendimento.getIdMedicosSolicitante());
			preparedStatement.setInt(6, atendimento.getIdPaciente());
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - Adicionar atendimento");
		}
	}

	public Atendimento getUltimoAtendimento() {
		Atendimento atendimento = new Atendimento();
		try {
			Statement st_id = connect.createStatement();
			int id = st_id.executeQuery("select last_insert_rowid()").getInt(1);

			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Atendimento WHERE id_atendimento=" + id + ";";
			ResultSet rs_atendimento = statement.executeQuery(sql);

			atendimento.setExame(rs_atendimento.getString("exames").split("/"));
			atendimento.setFormaDePagamento(rs_atendimento.getString("forma_pagamento"));
			atendimento.setIdAtendimento(rs_atendimento.getInt("id_atendimento"));
			atendimento.setIdMedicoSolicitante(rs_atendimento.getInt("id_medico"));
			atendimento.setIdPaciente(rs_atendimento.getInt("id_paciente"));
			atendimento.setValorTotal(rs_atendimento.getDouble("valor_total"));
			atendimento.setDataAtendimento(rs_atendimento.getString("data"));
			rs_atendimento.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA ULTIMO ATENDIMENTO");
		}

		return atendimento;
	}

	public List<Atendimento> getAtendimentosPorPaciente(int idPaciente) {
		List<Atendimento> atendimentos = new LinkedList<>();
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Atendimento WHERE id_paciente=" + idPaciente + ";";
			ResultSet rs_atendimento = statement.executeQuery(sql);

			while (rs_atendimento.next()) {
				Atendimento atendimento = new Atendimento();
				atendimento.setDataAtendimento(rs_atendimento.getString("data"));
				atendimento.setExame(rs_atendimento.getString("exames").split("/"));
				atendimento.setFormaDePagamento(rs_atendimento.getString("forma_pagamento"));
				atendimento.setIdAtendimento(rs_atendimento.getInt("id_atendimento"));
				atendimento.setIdMedicoSolicitante(rs_atendimento.getInt("id_medico"));
				atendimento.setIdPaciente(rs_atendimento.getInt("id_paciente"));
				atendimento.setValorTotal(rs_atendimento.getDouble("valor_total"));
				atendimentos.add(atendimento);
			}
			rs_atendimento.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA ATENDIMENTO POR PACIENTE");
		}

		return atendimentos;
	}

	public Atendimento getAtendimentosPorIdAtendimento(int idAtendimento) {
		Atendimento atendimento = new Atendimento();
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * from Atendimento WHERE id_atendimento=" + idAtendimento + ";";
			ResultSet rs_atendimento = statement.executeQuery(sql);

			while (rs_atendimento.next()) {
				atendimento.setDataAtendimento(rs_atendimento.getString("data"));
				atendimento.setExame(rs_atendimento.getString("exames").split("\n"));
				atendimento.setFormaDePagamento(rs_atendimento.getString("forma_pagamento"));
				atendimento.setIdAtendimento(rs_atendimento.getInt("id_atendimento"));
				atendimento.setIdMedicoSolicitante(rs_atendimento.getInt("id_medico"));
				atendimento.setIdPaciente(rs_atendimento.getInt("id_paciente"));
				atendimento.setValorTotal(rs_atendimento.getDouble("valor_total"));
			}
			rs_atendimento.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Erro na conexão - TABELA ATENDIMENTO POR PACIENTE");
		}

		return atendimento;
	}

	public void iniciar() {
		conectarBancoDeDados();
		criarTabelaPaciente();
		criarTabelaMedico();
		criarTabelaAtendimento();
		criarTabelaFuncionario();
		tabelaExames();
	}
}