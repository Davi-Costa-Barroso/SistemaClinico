package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.pdfbox.exceptions.COSVisitorException;

import arquivos.PDF;
import bd.ConexaoBD;
import entidades.Atendimento;
import entidades.Medico;
import entidades.Paciente;
import exames.Colesterol;
import exames.Glicemia;
import exames.Hemograma;
import exames.Triglicerideos;
import exames.Urina;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaAdicionarResultados {

	protected JFrame frameAdicionarResultados;
	protected int requisicao;
	protected JButton btnSalvar;

	// Variaveis Menu
	private JMenu menuAtendimento;
	private JMenuBar menuBar;
	private JMenuItem menuItemNovoAtendimento;
	private JMenuItem menuItemListarAtendimento;
	private JMenuItem mntmNewMenuItem;
	private JMenu menuFuncionario;
	private JMenuItem menuItemCadastrarFuncionario;
	private JMenuItem menuItemListarFuncionarios;
	private int permissao = 0;

	// Hemograma
	private JTextField textFieldHemacias;
	private JTextField textFieldHemoglobina;
	private JTextField textFieldHematocrito;
	private JTextField textFieldCHCM;
	private JTextField textFieldPlaquetas;
	private JTextField textFieldLeucocitos;
	private JTextField textFieldBasofilos;
	private JTextField textFieldBastoes;
	private JTextField textFieldLinfocitos;
	// Urina
	private JTextField textFieldAspecto;
	private JTextField textFieldCor;
	private JTextField textFieldDensidade;
	private JTextField textFieldPH;
	private JTextField textFieldProteina;
	private JTextField textFieldGlicose;
	private JTextField textFieldSangue;
	private JTextField textFieldCristais;
	private JTextField textFieldCilindros;
	//
	private JTextField textFieldGlicemiaGlicose;

	// Colesterol
	private JTextField textFieldColesterolTotal;
	private JTextField textFieldColesterolLDL;
	private JTextField textFieldColesterolHDL;
	private JTextField textFieldColesterolVLDL;
	// Triglicerideos
	private JTextField textFieldTriglicerideosTotal;

	/**
	 * Create the application.
	 */
	public TelaAdicionarResultados(int requisicao, int permissao) {
		this.requisicao = requisicao;
		this.permissao = permissao;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameAdicionarResultados = new JFrame();
		frameAdicionarResultados.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frameAdicionarResultados.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(TelaAdicionarResultados.class.getResource("/img/iconeLaboratorio.png")));
		frameAdicionarResultados.setTitle("Adicionar resultado");
		frameAdicionarResultados.setBounds(100, 100, 922, 600);
		frameAdicionarResultados.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameAdicionarResultados.getContentPane().setLayout(null);

		JPanel panelDadosExameSelecionado = new JPanel();
		panelDadosExameSelecionado.setLayout(null);
		panelDadosExameSelecionado.setBounds(12, 146, 896, 206);
		panelDadosExameSelecionado.setVisible(false);
		frameAdicionarResultados.getContentPane().add(panelDadosExameSelecionado);

		JPanel panelExame = new JPanel();
		panelExame.setBorder(new TitledBorder(null, "Exame", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelExame.setBounds(12, 37, 896, 98);
		frameAdicionarResultados.getContentPane().add(panelExame);
		panelExame.setLayout(null);

		JLabel lblSelecioneOExame = new JLabel("Selecione o exame");
		lblSelecioneOExame.setBounds(12, 56, 173, 15);
		panelExame.add(lblSelecioneOExame);

		JLabel lblSalvoComSucesso = new JLabel("Salvo com sucesso");
		lblSalvoComSucesso.setForeground(new Color(0, 128, 0));
		lblSalvoComSucesso.setVisible(false);
		lblSalvoComSucesso.setBounds(404, 466, 145, 15);
		frameAdicionarResultados.getContentPane().add(lblSalvoComSucesso);

		JComboBox<String> comboBoxExamesSolicitados = new JComboBox<>();
		comboBoxExamesSolicitados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboBoxExamesSolicitados.addItem(" ");
		ConexaoBD conexao = new ConexaoBD();
		Atendimento atendimento = conexao.getAtendimentosPorIdAtendimento(requisicao);
		Paciente paciente = conexao.buscarPacientePorId(atendimento.getIdPaciente());
		Medico medico = conexao.buscarMedicoPorId(atendimento.getIdMedicosSolicitante());

		String[] exames = atendimento.getExames().split("\n");

		for (int i = 0; i < exames.length; i++) {
			comboBoxExamesSolicitados.addItem(exames[i]);
		}

		comboBoxExamesSolicitados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblSalvoComSucesso.setVisible(false);
				if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Hemograma")) {
					Hemograma hemogramaBuscado = conexao.buscarResultadoHemograma(requisicao);
					mostrarDadosUrina(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, null, false);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, null, false);
					mostrarDadosHemograma(panelDadosExameSelecionado, hemogramaBuscado, true);
					btnSalvar.setEnabled(true);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Urina")) {
					Urina urinaBuscada = conexao.buscarResultadoUrina(requisicao);
					mostrarDadosHemograma(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, null, false);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, null, false);
					mostrarDadosUrina(panelDadosExameSelecionado, urinaBuscada, true);
					btnSalvar.setEnabled(true);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Glicemia")) {
					Glicemia glicemiaBuscada = conexao.buscarResultadoGlicemia(requisicao);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, null, false);
					mostrarDadosHemograma(panelDadosExameSelecionado, null, false);
					mostrarDadosUrina(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, glicemiaBuscada, true);
					btnSalvar.setEnabled(true);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Colesterol")) {
					Colesterol colesterolBuscado = conexao.buscarResultadoColesterol(requisicao);
					mostrarDadosHemograma(panelDadosExameSelecionado, null, false);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, null, false);
					mostrarDadosUrina(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, colesterolBuscado, true);
					btnSalvar.setEnabled(true);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Triglicerideos")) {
					Triglicerideos triglicerideosBuscado = conexao.buscarResultadoTriglicerideos(requisicao);
					mostrarDadosHemograma(panelDadosExameSelecionado, null, false);
					mostrarDadosUrina(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, null, false);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, triglicerideosBuscado, true);
					btnSalvar.setEnabled(true);

				} else {
					mostrarDadosHemograma(panelDadosExameSelecionado, null, false);
					mostrarDadosUrina(panelDadosExameSelecionado, null, false);
					mostrarDadosColesterol(panelDadosExameSelecionado, null, false);
					mostrarDadosGlicemia(panelDadosExameSelecionado, null, false);
					mostrarDadosTriglicerideos(panelDadosExameSelecionado, null, false);
					btnSalvar.setEnabled(false);
					panelDadosExameSelecionado.setVisible(false);

				}
			}
		});

		comboBoxExamesSolicitados.setBounds(150, 51, 173, 24);
		panelExame.add(comboBoxExamesSolicitados);

		JLabel lblNomeExame = new JLabel("Nome: " + paciente.getNomeCompleto());
		lblNomeExame.setBounds(12, 29, 311, 15);
		panelExame.add(lblNomeExame);

		JLabel lblRequisicaoNumero = new JLabel("Requisição nº " + requisicao);
		lblRequisicaoNumero.setBounds(391, 29, 112, 15);
		panelExame.add(lblRequisicaoNumero);

		JLabel lblDataDeAtendimento = new JLabel("Data de atendimento: " + atendimento.getDataAtendimento());
		lblDataDeAtendimento.setBounds(587, 29, 277, 15);
		panelExame.add(lblDataDeAtendimento);

		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PDF gerarResultado = new PDF();
				try {
					Desktop.getDesktop()
							.open(new File(gerarResultado.gerarResultadoPDF(atendimento, paciente, medico)));

				} catch (COSVisitorException | ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnImprimir.setEnabled(true);
		btnImprimir.setBounds(528, 430, 117, 25);
		frameAdicionarResultados.getContentPane().add(btnImprimir);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Hemograma")) {
					Hemograma hemograma = new Hemograma();
					hemograma.setHemacias(Double.parseDouble(textFieldHemacias.getText()));
					hemograma.setHemoglobina(Double.parseDouble(textFieldHemoglobina.getText()));
					hemograma.setPlaquetas(Double.parseDouble(textFieldPlaquetas.getText()));
					hemograma.setCHCM(Double.parseDouble(textFieldCHCM.getText()));
					hemograma.setHematocrito(Double.parseDouble(textFieldHematocrito.getText()));
					hemograma.setLinfocitos(Double.parseDouble(textFieldLinfocitos.getText()));
					hemograma.setBasofilos(Double.parseDouble(textFieldBasofilos.getText()));
					hemograma.setBastoes(Double.parseDouble(textFieldBastoes.getText()));
					hemograma.setLeucocitos(Double.parseDouble(textFieldLeucocitos.getText()));
					conexao.adicionarResultadosHemograma(hemograma, requisicao);
					btnSalvar.setEnabled(false);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Urina")) {
					Urina urina = new Urina();
					urina.setAspecto(textFieldAspecto.getText());
					urina.setCilindros(textFieldCilindros.getText());
					urina.setCor(textFieldCor.getText());
					urina.setCristais(textFieldCristais.getText());
					urina.setDensidade(Double.parseDouble(textFieldDensidade.getText()));
					urina.setGlicose(textFieldGlicose.getText());
					urina.setPH(Integer.parseInt(textFieldPH.getText()));
					urina.setProteina(textFieldProteina.getText());
					urina.setSangue(textFieldSangue.getText());
					conexao.adicionarResultadosUrina(urina, requisicao);
					btnSalvar.setEnabled(false);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Glicemia")) {
					Glicemia glicemia = new Glicemia();
					glicemia.setGlicose(Double.parseDouble(textFieldGlicemiaGlicose.getText()));
					conexao.adicionarResultadosGlicemia(glicemia, requisicao);
					btnSalvar.setEnabled(false);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Colesterol")) {
					Colesterol colesterol = new Colesterol();
					colesterol.setHDL(Double.parseDouble(textFieldColesterolHDL.getText()));
					colesterol.setLDL(Double.parseDouble(textFieldColesterolLDL.getText()));
					colesterol.setTotal(Double.parseDouble(textFieldColesterolTotal.getText()));
					colesterol.setVLDL(Double.parseDouble(textFieldColesterolVLDL.getText()));
					conexao.adicionarResultadosColesterol(colesterol, requisicao);
					btnSalvar.setEnabled(false);

				} else if (comboBoxExamesSolicitados.getSelectedItem().toString().equals("Triglicerideos")) {
					Triglicerideos triglicerideos = new Triglicerideos();
					triglicerideos.setTotal(Double.parseDouble(textFieldTriglicerideosTotal.getText()));
					conexao.adicionarResultadosTriglicerideos(triglicerideos, requisicao);
					btnSalvar.setEnabled(false);

				}
				lblSalvoComSucesso.setVisible(true);

			}
		});
		btnSalvar.setBounds(394, 430, 117, 25);
		btnSalvar.setEnabled(false);
		frameAdicionarResultados.getContentPane().add(btnSalvar);

		// Opcao de voltar para tela atendimento
		JButton btnVoltar = new JButton("voltar");
		
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameAdicionarResultados.dispose();
				TelaAtendimento telaAtendimento = new TelaAtendimento(permissao);
				telaAtendimento.frmHistoricoDeAtendimento.setVisible(true);

				ConexaoBD conexaoBD = new ConexaoBD();
				List<Atendimento> atendimentos = conexaoBD.getAtendimentosPorPaciente(paciente.getIdPaciente());
				Medico medico;
				for (Atendimento atendimento : atendimentos) {
					medico = conexaoBD.buscarMedicoPorId(atendimento.getIdMedicosSolicitante());
					telaAtendimento.dtm.addRow(new Object[] { atendimento.getIdAtendimento(),
							atendimento.getDataAtendimento(), medico.getNome() });
				}
			}
		});
		btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVoltar.setBounds(267, 430, 117, 25);
		frameAdicionarResultados.getContentPane().add(btnVoltar);

		frameAdicionarResultados.setResizable(false);
		frameAdicionarResultados.setLocationRelativeTo(null);

		menu();
	}

	public void mostrarDadosHemograma(JPanel panelDadosExameSelecionado, Hemograma hemograma, boolean habilitar) {
		panelDadosExameSelecionado.setVisible(true);
		panelDadosExameSelecionado.setBorder(new TitledBorder("Hemograma"));

		JLabel lblHemacias = new JLabel("Hemácias");
		lblHemacias.setBounds(12, 39, 77, 15);

		textFieldHemacias = new JTextField();
		textFieldHemacias.setBounds(121, 39, 77, 19);
		textFieldHemacias.setColumns(10);

		JLabel lblUnidadeHemacias = new JLabel("milhões/mm3");
		lblUnidadeHemacias.setBounds(209, 39, 120, 15);

		JLabel lblHemoglobina = new JLabel("Hemoglobina");
		lblHemoglobina.setBounds(12, 78, 98, 15);

		textFieldHemoglobina = new JTextField();
		textFieldHemoglobina.setBounds(121, 76, 77, 19);
		textFieldHemoglobina.setColumns(10);

		JLabel lblUnidadeHemoglobina = new JLabel("g %");
		lblUnidadeHemoglobina.setBounds(209, 78, 70, 15);

		JLabel lblHematocrito = new JLabel("Hematócrito");
		lblHematocrito.setBounds(12, 117, 98, 15);

		textFieldHematocrito = new JTextField();
		textFieldHematocrito.setColumns(10);
		textFieldHematocrito.setBounds(121, 115, 77, 19);

		JLabel lblUnidadeHematocrito = new JLabel("%");
		lblUnidadeHematocrito.setBounds(209, 117, 70, 15);

		JLabel lblCHCM = new JLabel("CHCM");
		lblCHCM.setBounds(341, 37, 98, 15);

		textFieldCHCM = new JTextField();
		textFieldCHCM.setColumns(10);
		textFieldCHCM.setBounds(450, 33, 77, 19);

		JLabel lblUnidadeCHCM = new JLabel("%");
		lblUnidadeCHCM.setBounds(538, 37, 120, 15);

		JLabel lblLinfocitos = new JLabel("Linfócitos");
		lblLinfocitos.setBounds(341, 76, 98, 15);

		textFieldLinfocitos = new JTextField();
		textFieldLinfocitos.setColumns(10);
		textFieldLinfocitos.setBounds(450, 72, 77, 19);

		JLabel lblUnidadeLinfocitos = new JLabel("%");
		lblUnidadeLinfocitos.setBounds(538, 74, 70, 15);

		JLabel lblPlaquetas = new JLabel("Plaquetas");
		lblPlaquetas.setBounds(341, 115, 98, 15);

		textFieldPlaquetas = new JTextField();
		textFieldPlaquetas.setColumns(10);
		textFieldPlaquetas.setBounds(450, 113, 77, 19);

		JLabel lblUnidadePlaquetas = new JLabel("mil/mm3");
		lblUnidadePlaquetas.setBounds(538, 115, 120, 15);

		JLabel lblLeucocitos = new JLabel("Leucócitos");
		lblLeucocitos.setBounds(642, 35, 98, 15);

		textFieldLeucocitos = new JTextField();
		textFieldLeucocitos.setColumns(10);
		textFieldLeucocitos.setBounds(731, 33, 77, 19);

		JLabel lblUnidadeLeucocitos = new JLabel("/mm3");
		lblUnidadeLeucocitos.setBounds(826, 39, 120, 15);

		JLabel lblBasofilos = new JLabel("Basófilos");
		lblBasofilos.setBounds(642, 78, 98, 15);

		textFieldBasofilos = new JTextField();
		textFieldBasofilos.setColumns(10);
		textFieldBasofilos.setBounds(731, 72, 77, 19);

		JLabel lblUnidadeBasofilos = new JLabel("%");
		lblUnidadeBasofilos.setBounds(826, 78, 43, 15);

		textFieldBastoes = new JTextField();
		textFieldBastoes.setColumns(10);
		textFieldBastoes.setBounds(731, 113, 77, 19);

		JLabel lblBastoes = new JLabel("Bastões");
		lblBastoes.setBounds(642, 117, 98, 15);

		JLabel lblUnidadesBastoes = new JLabel("%");
		lblUnidadesBastoes.setBounds(826, 117, 77, 15);

		if (habilitar) {
			if (hemograma != null) {
				textFieldHemacias.setText(String.valueOf(hemograma.getHemacias()));
				textFieldHemoglobina.setText(String.valueOf(hemograma.getHemoglobina()));
				textFieldPlaquetas.setText(String.valueOf(hemograma.getPlaquetas()));
				textFieldCHCM.setText(String.valueOf(hemograma.getCHCM()));
				textFieldHematocrito.setText(String.valueOf(hemograma.getHematocrito()));
				textFieldLinfocitos.setText(String.valueOf(hemograma.getLinfocitos()));
				textFieldBasofilos.setText(String.valueOf(hemograma.getBasofilos()));
				textFieldBastoes.setText(String.valueOf(hemograma.getBastoes()));
				textFieldLeucocitos.setText(String.valueOf(hemograma.getLeucocitos()));
			}
			panelDadosExameSelecionado.add(lblHemacias);
			panelDadosExameSelecionado.add(lblUnidadeBasofilos);
			panelDadosExameSelecionado.add(lblHemoglobina);
			panelDadosExameSelecionado.add(textFieldBastoes);
			panelDadosExameSelecionado.add(lblBastoes);
			panelDadosExameSelecionado.add(lblUnidadesBastoes);
			panelDadosExameSelecionado.add(textFieldBasofilos);
			panelDadosExameSelecionado.add(lblBasofilos);
			panelDadosExameSelecionado.add(lblUnidadeLeucocitos);
			panelDadosExameSelecionado.add(textFieldLeucocitos);
			panelDadosExameSelecionado.add(lblUnidadeHemacias);
			panelDadosExameSelecionado.add(lblUnidadePlaquetas);
			panelDadosExameSelecionado.add(textFieldPlaquetas);
			panelDadosExameSelecionado.add(lblLeucocitos);
			panelDadosExameSelecionado.add(lblPlaquetas);
			panelDadosExameSelecionado.add(lblLinfocitos);
			panelDadosExameSelecionado.add(lblUnidadeLinfocitos);
			panelDadosExameSelecionado.add(textFieldCHCM);
			panelDadosExameSelecionado.add(lblUnidadeHematocrito);
			panelDadosExameSelecionado.add(lblUnidadeCHCM);
			panelDadosExameSelecionado.add(textFieldHematocrito);
			panelDadosExameSelecionado.add(textFieldLinfocitos);
			panelDadosExameSelecionado.add(lblCHCM);
			panelDadosExameSelecionado.add(lblHematocrito);
			panelDadosExameSelecionado.add(lblUnidadeHemoglobina);
			panelDadosExameSelecionado.add(textFieldHemoglobina);
			panelDadosExameSelecionado.add(textFieldHemacias);

		} else {
			panelDadosExameSelecionado.removeAll();
		}

	}

	public void mostrarDadosUrina(JPanel panelDadosExameSelecionado, Urina urina, boolean habilitar) {
		panelDadosExameSelecionado.setVisible(true);
		panelDadosExameSelecionado.setBorder(new TitledBorder("Urina"));

		JLabel lblCor = new JLabel("Cor");
		lblCor.setBounds(12, 39, 77, 15);

		textFieldCor = new JTextField();
		textFieldCor.setBounds(121, 39, 77, 19);
		textFieldCor.setColumns(10);

		JLabel lblDensidade = new JLabel("Densidade");
		lblDensidade.setBounds(12, 78, 98, 15);

		textFieldDensidade = new JTextField();
		textFieldDensidade.setBounds(121, 76, 77, 19);
		textFieldDensidade.setColumns(10);

		JLabel lblPH = new JLabel("PH");
		lblPH.setBounds(12, 117, 98, 15);

		textFieldPH = new JTextField();
		textFieldPH.setColumns(10);
		textFieldPH.setBounds(121, 115, 77, 19);

		JLabel lblAspecto = new JLabel("Aspecto");
		lblAspecto.setBounds(341, 37, 98, 15);

		textFieldAspecto = new JTextField();
		textFieldAspecto.setColumns(10);
		textFieldAspecto.setBounds(450, 33, 77, 19);

		JLabel lblProteina = new JLabel("Proteína");
		lblProteina.setBounds(341, 76, 98, 15);

		textFieldProteina = new JTextField();
		textFieldProteina.setColumns(10);
		textFieldProteina.setBounds(450, 72, 77, 19);

		JLabel lblGlicose = new JLabel("Glicose");
		lblGlicose.setBounds(341, 115, 98, 15);

		textFieldGlicose = new JTextField();
		textFieldGlicose.setColumns(10);
		textFieldGlicose.setBounds(450, 113, 77, 19);

		JLabel lblSangue = new JLabel("Sangue");
		lblSangue.setBounds(642, 35, 98, 15);

		textFieldSangue = new JTextField();
		textFieldSangue.setColumns(10);
		textFieldSangue.setBounds(731, 33, 77, 19);

		JLabel lblCristais = new JLabel("Cristais");
		lblCristais.setBounds(642, 78, 98, 15);

		textFieldCristais = new JTextField();
		textFieldCristais.setColumns(10);
		textFieldCristais.setBounds(731, 72, 77, 19);

		textFieldCilindros = new JTextField();
		textFieldCilindros.setColumns(10);
		textFieldCilindros.setBounds(731, 113, 77, 19);

		JLabel lblCilindros = new JLabel("Cilindros");
		lblCilindros.setBounds(642, 117, 98, 15);

		if (habilitar) {
			if (urina != null) {
				textFieldAspecto.setText(urina.getAspecto());
				textFieldCilindros.setText(urina.getCilindros());
				textFieldCor.setText(urina.getCor());
				textFieldCristais.setText(urina.getCristais());
				textFieldDensidade.setText(String.valueOf(urina.getDensidade()));
				textFieldGlicose.setText(urina.getGlicose());
				textFieldPH.setText(String.valueOf(urina.getPH()));
				textFieldProteina.setText(urina.getProteina());
				textFieldSangue.setText(urina.getSangue());
			}
			panelDadosExameSelecionado.add(lblGlicose);
			panelDadosExameSelecionado.add(textFieldGlicose);
			panelDadosExameSelecionado.add(lblSangue);
			panelDadosExameSelecionado.add(textFieldSangue);
			panelDadosExameSelecionado.add(lblCristais);
			panelDadosExameSelecionado.add(textFieldCristais);
			panelDadosExameSelecionado.add(textFieldCilindros);
			panelDadosExameSelecionado.add(lblCilindros);
			panelDadosExameSelecionado.add(textFieldProteina);
			panelDadosExameSelecionado.add(lblProteina);
			panelDadosExameSelecionado.add(textFieldAspecto);
			panelDadosExameSelecionado.add(lblAspecto);
			panelDadosExameSelecionado.add(textFieldPH);
			panelDadosExameSelecionado.add(lblPH);
			panelDadosExameSelecionado.add(textFieldDensidade);
			panelDadosExameSelecionado.add(lblDensidade);
			panelDadosExameSelecionado.add(textFieldCor);
			panelDadosExameSelecionado.add(lblCor);
		} else {
			panelDadosExameSelecionado.removeAll();
		}

	}

	public void mostrarDadosGlicemia(JPanel panelDadosExameSelecionado, Glicemia glicemia, boolean habilitar) {
		panelDadosExameSelecionado.setVisible(true);
		panelDadosExameSelecionado.setBorder(new TitledBorder("Glicemia"));

		JLabel lblGlicose = new JLabel("Glicose");
		lblGlicose.setBounds(12, 39, 77, 15);

		textFieldGlicemiaGlicose = new JTextField();
		textFieldGlicemiaGlicose.setBounds(121, 39, 77, 19);
		textFieldGlicemiaGlicose.setColumns(10);

		JLabel lblUnidadeGlicose = new JLabel("mg/dL");
		lblUnidadeGlicose.setBounds(209, 39, 120, 15);

		if (habilitar) {
			if (glicemia != null) {
				textFieldGlicemiaGlicose.setText(String.valueOf(glicemia.getGlicose()));
			}

			panelDadosExameSelecionado.add(lblUnidadeGlicose);
			panelDadosExameSelecionado.add(textFieldGlicemiaGlicose);
			panelDadosExameSelecionado.add(lblGlicose);
		} else {
			panelDadosExameSelecionado.removeAll();
		}

	}

	public void mostrarDadosColesterol(JPanel panelDadosExameSelecionado, Colesterol colesterol, boolean habilitar) {
		panelDadosExameSelecionado.setVisible(true);
		panelDadosExameSelecionado.setBorder(new TitledBorder("Colesterol"));

		JLabel lblColesterolTotal = new JLabel("Total");
		lblColesterolTotal.setBounds(12, 39, 77, 15);

		textFieldColesterolTotal = new JTextField();
		textFieldColesterolTotal.setBounds(121, 39, 77, 19);
		textFieldColesterolTotal.setColumns(10);

		JLabel lblUnidadeColesterolTotal = new JLabel("mg/dL");
		lblUnidadeColesterolTotal.setBounds(209, 39, 120, 15);

		JLabel lblColesterolLDL = new JLabel("LDL");
		lblColesterolLDL.setBounds(12, 78, 98, 15);

		textFieldColesterolLDL = new JTextField();
		textFieldColesterolLDL.setBounds(121, 76, 77, 19);
		textFieldColesterolLDL.setColumns(10);

		JLabel lblUnidadeColesterolLDL = new JLabel("mg/dL");
		lblUnidadeColesterolLDL.setBounds(209, 78, 70, 15);

		JLabel lblColesterolHDL = new JLabel("HDL");
		lblColesterolHDL.setBounds(341, 37, 98, 15);

		textFieldColesterolHDL = new JTextField();
		textFieldColesterolHDL.setColumns(10);
		textFieldColesterolHDL.setBounds(450, 33, 77, 19);

		JLabel lblUnidadeColesterolHDL = new JLabel("mg/dL");
		lblUnidadeColesterolHDL.setBounds(538, 37, 120, 15);

		JLabel lblColesterolVLDL = new JLabel("VLDL");
		lblColesterolVLDL.setBounds(341, 76, 98, 15);

		textFieldColesterolVLDL = new JTextField();
		textFieldColesterolVLDL.setColumns(10);
		textFieldColesterolVLDL.setBounds(450, 72, 77, 19);

		JLabel lblUnidadeColesterolVLDL = new JLabel("mg/dL");
		lblUnidadeColesterolVLDL.setBounds(538, 74, 70, 15);

		if (habilitar) {
			if (colesterol != null) {
				textFieldColesterolLDL.setText(String.valueOf(colesterol.getLDL()));
				textFieldColesterolTotal.setText(String.valueOf(colesterol.getTotal()));
				textFieldColesterolHDL.setText(String.valueOf(colesterol.getHDL()));
				textFieldColesterolVLDL.setText(String.valueOf(colesterol.getVLDL()));
			}
			panelDadosExameSelecionado.add(lblUnidadeColesterolVLDL);
			panelDadosExameSelecionado.add(textFieldColesterolVLDL);
			panelDadosExameSelecionado.add(lblColesterolVLDL);
			panelDadosExameSelecionado.add(lblUnidadeColesterolHDL);
			panelDadosExameSelecionado.add(textFieldColesterolHDL);
			panelDadosExameSelecionado.add(lblColesterolHDL);
			panelDadosExameSelecionado.add(lblUnidadeColesterolLDL);
			panelDadosExameSelecionado.add(textFieldColesterolLDL);
			panelDadosExameSelecionado.add(lblColesterolLDL);
			panelDadosExameSelecionado.add(lblColesterolTotal);
			panelDadosExameSelecionado.add(textFieldColesterolTotal);
			panelDadosExameSelecionado.add(lblUnidadeColesterolTotal);
		} else {
			panelDadosExameSelecionado.removeAll();
		}
	}

	public void mostrarDadosTriglicerideos(JPanel panelDadosExameSelecionado, Triglicerideos triglicerideos,
			boolean habilitar) {
		panelDadosExameSelecionado.setVisible(true);
		panelDadosExameSelecionado.setBorder(new TitledBorder("Triglicerídeos"));

		JLabel lblTriglicerideos = new JLabel("Total");
		lblTriglicerideos.setBounds(12, 39, 77, 15);

		textFieldTriglicerideosTotal = new JTextField();
		textFieldTriglicerideosTotal.setBounds(121, 39, 77, 19);
		textFieldTriglicerideosTotal.setColumns(10);

		JLabel lblUnidadeTriglicerideos = new JLabel("mg/dL");
		lblUnidadeTriglicerideos.setBounds(209, 39, 120, 15);

		if (habilitar) {
			if (triglicerideos != null) {
				textFieldTriglicerideosTotal.setText(String.valueOf(triglicerideos.getTotal()));
			}
			panelDadosExameSelecionado.add(lblUnidadeTriglicerideos);
			panelDadosExameSelecionado.add(textFieldTriglicerideosTotal);
			panelDadosExameSelecionado.add(lblTriglicerideos);
		} else {
			panelDadosExameSelecionado.removeAll();
		}

	}

	private void menu() {
		// Inicio do MENU
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 191, 255));
		frameAdicionarResultados.setJMenuBar(menuBar);

		menuAtendimento = new JMenu("Atendimento");

		menuAtendimento.setForeground(Color.BLACK);
		menuAtendimento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuAtendimento.setHorizontalTextPosition(SwingConstants.CENTER);
		menuAtendimento.setHorizontalAlignment(SwingConstants.CENTER);
		menuAtendimento.setFont(new Font("Segoe UI", Font.BOLD, 16));
		menuBar.add(menuAtendimento);

		menuItemNovoAtendimento = new JMenuItem("Novo Atendimento");
		menuItemNovoAtendimento.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/adicionarAtendimento.png")));
		menuItemNovoAtendimento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuItemNovoAtendimento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameAdicionarResultados.dispose();
				TelaPaciente telaPaciente = new TelaPaciente(permissao);
				telaPaciente.frmCadastroDePaciente.setVisible(true);
			}
		});
		menuItemNovoAtendimento.setHorizontalAlignment(SwingConstants.LEFT);
		menuItemNovoAtendimento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuAtendimento.add(menuItemNovoAtendimento);

		menuItemListarAtendimento = new JMenuItem("Listar Atendimentos");
		menuItemListarAtendimento.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/listar.png")));
		menuItemListarAtendimento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameAdicionarResultados.dispose();
				ListaPacientes telaPaciente = new ListaPacientes(permissao);
				telaPaciente.frmListarPacientes.setVisible(true);
			}
		});
		menuItemListarAtendimento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuItemListarAtendimento.setHorizontalAlignment(SwingConstants.LEFT);
		menuItemListarAtendimento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuAtendimento.add(menuItemListarAtendimento);

		mntmNewMenuItem = new JMenuItem("Sair");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameAdicionarResultados.dispose();
				TelaLogin telaLogin = new TelaLogin();
				telaLogin.frmTelaLogin.setVisible(true);
			}
		});
		mntmNewMenuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mntmNewMenuItem.setForeground(new Color(0, 0, 0));
		mntmNewMenuItem.setBackground(new Color(0, 191, 255));
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.BOLD, 16));

		if (permissao == 1) {
			menuFuncionario = new JMenu("Funcionário");
			menuFuncionario.setForeground(Color.BLACK);
			menuFuncionario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			menuFuncionario.setHorizontalTextPosition(SwingConstants.CENTER);
			menuFuncionario.setHorizontalAlignment(SwingConstants.CENTER);
			menuFuncionario.setFont(new Font("Segoe UI", Font.BOLD, 16));

			menuBar.add(menuFuncionario);

			menuItemCadastrarFuncionario = new JMenuItem("Cadastrar Funcionário");
			menuItemCadastrarFuncionario.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/cadastrar.png")));
			menuItemCadastrarFuncionario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			menuItemCadastrarFuncionario.setHorizontalAlignment(SwingConstants.LEFT);
			menuItemCadastrarFuncionario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			menuItemCadastrarFuncionario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frameAdicionarResultados.dispose();
					TelaCadastrarFuncionario cadastrarFuncionario = new TelaCadastrarFuncionario(1);
					cadastrarFuncionario.frmCadastrarFuncionario.setVisible(true);
				}
			});
			menuFuncionario.add(menuItemCadastrarFuncionario);

			menuItemListarFuncionarios = new JMenuItem("Listar Funcionários");
			menuItemListarFuncionarios.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/listar.png")));
			menuItemListarFuncionarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			menuItemListarFuncionarios.setHorizontalAlignment(SwingConstants.LEFT);
			menuItemListarFuncionarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			menuItemListarFuncionarios.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frameAdicionarResultados.dispose();
					ListaFuncionarios listarFuncionarios = new ListaFuncionarios(permissao);
					listarFuncionarios.frmListarFuncionarios.setVisible(true);
				}
			});
			menuFuncionario.add(menuItemListarFuncionarios);
		}
		menuBar.add(mntmNewMenuItem);
		// fim do MENU
	}
}
