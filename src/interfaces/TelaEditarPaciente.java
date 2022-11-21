package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import bd.ConexaoBD;
import entidades.Paciente;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class TelaEditarPaciente {

	protected JFrame frmEditarPaciente;
	protected JTextField textField_NomeCompleto;
	protected JFormattedTextField textField_CPF;
	protected JFormattedTextField textField_DataDeNascimento;
	protected JTextField textField_Dias;
	protected JTextField textField_Meses;
	protected JTextField textField_Anos;
	protected JTextField textField_Responsavel;
	protected JFormattedTextField textField_Telefone;
	private JButton btnSalvar;

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
	private String pacienteSelecionado;

	/**
	 * Create the application.
	 */

	public TelaEditarPaciente(int permissao, String pacienteSelecionado) {
		this.permissao = permissao;
		this.pacienteSelecionado = pacienteSelecionado;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() {
		try {
			// Para formatar esses campos, criando máscaras
			MaskFormatter formaterDataNascimento = new MaskFormatter();
			MaskFormatter formaterTelefone = new MaskFormatter();
			MaskFormatter formaterCpf = new MaskFormatter();
			MaskFormatter formaterCrm = new MaskFormatter();

			formaterCpf.setMask("###.###.###-##"); // Máscara de CPF
			formaterDataNascimento.setMask("##/##/####"); // Máscara de data de nascimento
			formaterTelefone.setMask("(##)#####-####"); // Máscara de telefone
			formaterCrm.setMask("######");

			// Abrindo o frame
			frmEditarPaciente = new JFrame();
			frmEditarPaciente.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						ConexaoBD.connect.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			frmEditarPaciente.setIconImage(Toolkit.getDefaultToolkit()
					.getImage(TelaEditarPaciente.class.getResource("/img/iconeLaboratorio.png")));
			frmEditarPaciente.setTitle("Editar Paciente");
			frmEditarPaciente.setBounds(100, 100, 922, 600);
			frmEditarPaciente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmEditarPaciente.setLocationRelativeTo(null);
			frmEditarPaciente.setResizable(false);
			frmEditarPaciente.getContentPane().setLayout(null);

			JPanel panelPaciente = new JPanel();
			panelPaciente.setBounds(12, 0, 882, 207);
			frmEditarPaciente.getContentPane().add(panelPaciente);
			panelPaciente
					.setBorder(new TitledBorder(null, "Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelPaciente.setLayout(null);

			JLabel lblCpf = new JLabel("CPF");
			lblCpf.setBounds(12, 29, 70, 15);

			JLabel lblCPFInvalido = new JLabel("Campo obrigatório");
			lblCPFInvalido.setBounds(12, 62, 136, 15);
			lblCPFInvalido.setForeground(Color.RED);
			lblCPFInvalido.setVisible(false);

			textField_CPF = new JFormattedTextField();
			textField_CPF.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_CPF.getText().contains(" ")) {
						lblCPFInvalido.setVisible(true);
					}
				}
			});
			textField_CPF.setBounds(12, 43, 155, 19);
			formaterCpf.install(textField_CPF);
			textField_CPF.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_CPF.getText().contains(" ")) {
						lblCPFInvalido.setVisible(true);
					} else {
						lblCPFInvalido.setVisible(false);

					}
				}
			});

			JLabel lblAvisoNomeNulo = new JLabel("Campo obrigatório");
			lblAvisoNomeNulo.setBounds(193, 62, 291, 15);
			lblAvisoNomeNulo.setForeground(Color.RED);
			lblAvisoNomeNulo.setVisible(false);

			JLabel lblNomeCompleto = new JLabel("Nome Completo");
			lblNomeCompleto.setBounds(193, 29, 111, 15);

			textField_NomeCompleto = new JTextField();
			textField_NomeCompleto.setBounds(193, 43, 291, 19);
			textField_NomeCompleto.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_NomeCompleto.getText().isBlank()) {
						lblAvisoNomeNulo.setVisible(true);

					} else {
						lblAvisoNomeNulo.setVisible(false);
					}
				}
			});
			textField_NomeCompleto.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_NomeCompleto.getText().isBlank()) {
						lblAvisoNomeNulo.setVisible(true);

					} else {
						lblAvisoNomeNulo.setVisible(false);
					}
				}

			});

			JLabel lblAnos = new JLabel("Anos");
			lblAnos.setBounds(666, 29, 70, 15);

			textField_Anos = new JTextField("0");
			textField_Anos.setBounds(660, 43, 49, 19);
			textField_Anos.setEditable(false);
			JLabel lblMeses = new JLabel("Meses");
			lblMeses.setBounds(728, 29, 70, 15);

			textField_Meses = new JTextField("0");
			textField_Meses.setBounds(725, 43, 49, 19);
			textField_Meses.setEditable(false);

			JLabel lblDias = new JLabel("Dias");
			lblDias.setBounds(792, 29, 49, 15);

			textField_Dias = new JTextField("0");
			textField_Dias.setBounds(786, 43, 49, 19);
			textField_Dias.setEditable(false);

			JLabel lblDataDeNascimento = new JLabel("Data de Nascimento");
			lblDataDeNascimento.setBounds(496, 29, 155, 15);

			JLabel lblDataDeNascimentoNaoNula = new JLabel("Campo obrigatório");
			lblDataDeNascimentoNaoNula.setBounds(496, 62, 302, 15);
			lblDataDeNascimentoNaoNula.setForeground(Color.RED);
			lblDataDeNascimentoNaoNula.setVisible(false);

			textField_DataDeNascimento = new JFormattedTextField();
			formaterDataNascimento.install(textField_DataDeNascimento);
			textField_DataDeNascimento.setBounds(496, 43, 152, 19);
			textField_DataDeNascimento.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_DataDeNascimento.getText().contains(" ")) {
						textField_Dias.setText("0");
						textField_Meses.setText("0");
						textField_Anos.setText("0");
						lblDataDeNascimentoNaoNula.setVisible(true);

					} else {
						if (isDateValid(textField_DataDeNascimento.getText())) {
							lblDataDeNascimentoNaoNula.setVisible(false);

							textField_Dias.setText(String.valueOf(calcularDias()));
							textField_Meses.setText(String.valueOf(calcularMeses()));
							textField_Anos.setText(String.valueOf(calcularAnos()));

							if (calcularAnos() >= 18) {
								textField_Responsavel.setEnabled(false); // torna o campo não editável
							} else {
								textField_Responsavel.setEnabled(true);
							}

						} else {
							lblDataDeNascimentoNaoNula.setVisible(true);
							lblDataDeNascimentoNaoNula.setText("Data de Nascimento Inválida");
						}

					}

				}
			});
			textField_DataDeNascimento.addFocusListener(new FocusAdapter() { // quando o campo perde o 'foco', salva o
																				// dado
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else {
						lblDataDeNascimentoNaoNula.setVisible(false);
					}

				}

			});

			JLabel lblResponsvel = new JLabel("Responsável");
			lblResponsvel.setBounds(15, 86, 97, 15);

			JLabel lblResponsvelNaoPodeSerNulo = new JLabel("Campo obrigatório");
			lblResponsvelNaoPodeSerNulo.setBounds(12, 122, 249, 15);
			lblResponsvelNaoPodeSerNulo.setForeground(Color.RED);
			lblResponsvelNaoPodeSerNulo.setVisible(false);

			textField_Responsavel = new JTextField();
			textField_Responsavel.setEnabled(false);
			textField_Responsavel.setBounds(12, 100, 249, 19);
			textField_Responsavel.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_Responsavel.getText().isBlank()) {
						lblResponsvelNaoPodeSerNulo.setVisible(true);
					} else {
						lblResponsvelNaoPodeSerNulo.setVisible(false);
					}
				}
			});
			textField_Responsavel.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) { // quando o campo perde o 'foco', salva o dado
					if (textField_Responsavel.isEnabled()) {
						if (textField_Responsavel.getText().isBlank()) {
							lblResponsvelNaoPodeSerNulo.setVisible(true);
						} else {
							lblResponsvelNaoPodeSerNulo.setVisible(false);
						}
					}

				}
			});

			JLabel lblTelefone = new JLabel("Telefone");
			lblTelefone.setBounds(279, 86, 70, 15);

			JLabel lblTelefoneNaoPodeSerNula = new JLabel("Campo obrigatório");
			lblTelefoneNaoPodeSerNula.setBounds(273, 122, 211, 15);
			lblTelefoneNaoPodeSerNula.setForeground(Color.RED);
			lblTelefoneNaoPodeSerNula.setVisible(false);

			textField_Telefone = new JFormattedTextField();
			formaterTelefone.install(textField_Telefone);
			textField_Telefone.setBounds(273, 100, 155, 19);
			textField_Telefone.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_Telefone.getText().contains(" ")) {
						lblTelefoneNaoPodeSerNula.setVisible(true);

					} else {
						lblTelefoneNaoPodeSerNula.setVisible(false);
					}
				}
			});
			textField_Telefone.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_Telefone.getText().contains(" ")) {
						lblTelefoneNaoPodeSerNula.setVisible(true);

					} else {
						lblTelefoneNaoPodeSerNula.setVisible(false);
					}
				}
			});
			panelPaciente.add(textField_CPF);
			panelPaciente.add(lblCpf);
			panelPaciente.add(lblNomeCompleto);
			panelPaciente.add(textField_NomeCompleto);
			panelPaciente.add(lblDataDeNascimento);
			panelPaciente.add(textField_DataDeNascimento);
			panelPaciente.add(textField_Meses);
			panelPaciente.add(lblAnos);
			panelPaciente.add(lblDias);
			panelPaciente.add(lblMeses);
			panelPaciente.add(textField_Anos);
			panelPaciente.add(textField_Dias);
			panelPaciente.add(lblCPFInvalido);
			panelPaciente.add(lblAvisoNomeNulo);
			panelPaciente.add(lblDataDeNascimentoNaoNula);
			panelPaciente.add(textField_Responsavel);
			panelPaciente.add(lblResponsvel);
			panelPaciente.add(textField_Telefone);
			panelPaciente.add(lblTelefone);
			panelPaciente.add(lblResponsvelNaoPodeSerNulo);
			panelPaciente.add(lblTelefoneNaoPodeSerNula);

			JLabel lblSalvoComSucesso = new JLabel("Salvo com sucesso.");
			lblSalvoComSucesso.setForeground(new Color(0, 128, 0));
			lblSalvoComSucesso.setBounds(502, 168, 149, 27);
			lblSalvoComSucesso.setVisible(false);
			panelPaciente.add(lblSalvoComSucesso);

			btnSalvar = new JButton("Salvar");
			btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnSalvar.setBounds(406, 168, 78, 25);
			panelPaciente.add(btnSalvar);
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (textField_CPF.getText().contains(" ")) {
						lblCPFInvalido.setVisible(true);
					} else if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else if (textField_NomeCompleto.getText().isBlank()) {
						lblAvisoNomeNulo.setVisible(true);
					}

					else if (textField_Telefone.getText().contains(" ")) {
						lblTelefoneNaoPodeSerNula.setVisible(true);
					} else if (textField_Responsavel.getText().isBlank() && textField_Responsavel.isEnabled()) {
						lblResponsvelNaoPodeSerNulo.setVisible(true);
					}

					else {
						try {
							ConexaoBD conexaoBD = new ConexaoBD();
							Paciente paciente = conexaoBD.buscarPacientePorCpf(pacienteSelecionado);
							paciente.setCpf(textField_CPF.getText());
							paciente.setDataNascimento(textField_DataDeNascimento.getText());
							paciente.setNomeCompleto(textField_NomeCompleto.getText());
							paciente.setAnos(calcularAnos());
							paciente.setMeses(calcularMeses());
							paciente.setDias(calcularDias());
							paciente.setTelefone(textField_Telefone.getText());
							paciente.setResponsavel(textField_Responsavel.getText());
							conexaoBD.atualizarPaciente(paciente);

							lblSalvoComSucesso.setVisible(true);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

		} catch (Exception e) {
			System.out.println(e);
		}

		menu();
	}

	public int calcularDias() {
		String[] vetorDataNascimento = textField_DataDeNascimento.getText().split("/");
		String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		String[] vetorDataAtual = dataAtual.split("/");
		int dias = Integer.parseInt(vetorDataAtual[0]) - Integer.parseInt(vetorDataNascimento[0]);
		return Math.abs(dias);
	}

	public int calcularMeses() {
		String[] vetorDataNascimento = textField_DataDeNascimento.getText().split("/");
		String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		String[] vetorDataAtual = dataAtual.split("/");

		int meses = Integer.parseInt(vetorDataAtual[1]) - Integer.parseInt(vetorDataNascimento[1]);
		return Math.abs(meses);
	}

	public int calcularAnos() {
		String[] vetorDataNascimento = textField_DataDeNascimento.getText().split("/");
		String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		String[] vetorDataAtual = dataAtual.split("/");

		int anos = Integer.parseInt(vetorDataAtual[2]) - Integer.parseInt(vetorDataNascimento[2]);

		return Math.abs(anos);
	}

	public static boolean isDateValid(String strDate) {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataVerificada = LocalDate.parse(strDate, dtf);
			LocalDate hoje = LocalDate.now();
			return dataVerificada.compareTo(hoje) <= 0;
		} catch (Exception e) {
			return false;
		}
	}

	private void menu() {
		// Inicio do MENU
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 191, 255));
		frmEditarPaciente.setJMenuBar(menuBar);

		menuAtendimento = new JMenu("Atendimento");

		menuAtendimento.setForeground(Color.BLACK);
		menuAtendimento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuAtendimento.setHorizontalTextPosition(SwingConstants.CENTER);
		menuAtendimento.setHorizontalAlignment(SwingConstants.CENTER);
		menuAtendimento.setFont(new Font("Segoe UI", Font.BOLD, 16));
		menuBar.add(menuAtendimento);

		menuItemNovoAtendimento = new JMenuItem("Novo Atendimento");
		menuItemNovoAtendimento
				.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/adicionarAtendimento.png")));
		menuItemNovoAtendimento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuItemNovoAtendimento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmEditarPaciente.dispose();
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
				frmEditarPaciente.dispose();
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
				frmEditarPaciente.dispose();
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
			menuItemCadastrarFuncionario
					.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/cadastrar.png")));
			menuItemCadastrarFuncionario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			menuItemCadastrarFuncionario.setHorizontalAlignment(SwingConstants.LEFT);
			menuItemCadastrarFuncionario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			menuItemCadastrarFuncionario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmEditarPaciente.dispose();
					TelaCadastrarFuncionario cadastrarFuncionario = new TelaCadastrarFuncionario(1);
					cadastrarFuncionario.frmCadastrarFuncionario.setVisible(true);
				}
			});
			menuFuncionario.add(menuItemCadastrarFuncionario);

			menuItemListarFuncionarios = new JMenuItem("Listar Funcionários");
			menuItemListarFuncionarios
					.setIcon(new ImageIcon(TelaAdicionarResultados.class.getResource("/img/listar.png")));
			menuItemListarFuncionarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			menuItemListarFuncionarios.setHorizontalAlignment(SwingConstants.LEFT);
			menuItemListarFuncionarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			menuItemListarFuncionarios.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmEditarPaciente.dispose();
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
