package interfaces;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import bd.ConexaoBD;
import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaEditarFuncionario {

	protected JFrame frmEditarFuncionario;
	private JTextField textField_Usuario;
	private JPasswordField textField_Senha;
	private JTextField textField_NomeCompleto;
	private JFormattedTextField textField_CPF;
	private JFormattedTextField textField_DataDeNascimento;
	private JFormattedTextField textField_Telefone;
	private JButton btnSalvar;
	private JLabel lblSenha;
	private JLabel lblUsuario;
	private JLabel lblMsgAviso;

	// Variaveis Menu

	private JMenu menuAtendimento;
	private JMenuBar menuBar;
	private JMenuItem menuItemNovoAtendimento;
	private JMenuItem menuItemListarAtendimento;
	private JMenuItem mntmNewMenuItem;
	private JMenu menuFuncionario;
	private JMenuItem menuItemCadastrarFuncionario;
	private JMenuItem menuItemListarFuncionarios;
	private int permissao;
	private String funcionarioSelecionado;
	private int idFuncionarioSelecionado;

	/**
	 * Create the application.
	 */
	public TelaEditarFuncionario(int permissao, String funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
		this.permissao = permissao;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditarFuncionario = new JFrame();
		frmEditarFuncionario.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmEditarFuncionario.setIconImage(Toolkit.getDefaultToolkit().getImage(TelaEditarFuncionario.class.getResource("/img/iconeLaboratorio.png")));
		frmEditarFuncionario.setTitle("Sistema Clínico");
		frmEditarFuncionario.setBounds(100, 100, 922, 600);
		frmEditarFuncionario.setResizable(false);
		frmEditarFuncionario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEditarFuncionario.getContentPane().setLayout(new BorderLayout(0, 0));
		frmEditarFuncionario.setLocationRelativeTo(null);

		try {
			// Para formatar esses campos, criando máscaras
			MaskFormatter formaterDataNascimento = new MaskFormatter();
			MaskFormatter formaterTelefone = new MaskFormatter();
			MaskFormatter formaterCpf = new MaskFormatter();

			formaterCpf.setMask("###.###.###-##"); // Máscara de CPF
			formaterDataNascimento.setMask("##/##/####"); // Máscara de data de nascimento
			formaterTelefone.setMask("(##)#####-####"); // Máscara de telefone

			JPanel panelFuncionario = new JPanel();
			panelFuncionario.setBounds(12, 27, 882, 178);
			frmEditarFuncionario.getContentPane().add(panelFuncionario);
			panelFuncionario.setBorder(
					new TitledBorder(null, "Editar informações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFuncionario.setLayout(null);

			JLabel lblCpf = new JLabel("CPF");
			lblCpf.setBounds(12, 29, 70, 15);

			JLabel lblCPFInvalido = new JLabel("CPF inválido");
			lblCPFInvalido.setBounds(12, 62, 136, 15);
			lblCPFInvalido.setForeground(Color.RED);
			lblCPFInvalido.setVisible(false);

			textField_CPF = new JFormattedTextField();
			textField_CPF.setBounds(12, 43, 155, 19);
			formaterCpf.install(textField_CPF);
			JLabel lblAvisoNomeNulo = new JLabel("Nome Completo não pode ser nulo");
			lblAvisoNomeNulo.setBounds(224, 62, 291, 15);
			lblAvisoNomeNulo.setForeground(Color.RED);
			lblAvisoNomeNulo.setVisible(false);
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

			JLabel lblNomeCompleto = new JLabel("Nome Completo");
			lblNomeCompleto.setBounds(224, 29, 111, 15);

			textField_NomeCompleto = new JTextField();
			textField_NomeCompleto.setBounds(224, 43, 200, 19);
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
			JLabel lblDataDeNascimento = new JLabel("Data de Nascimento");
			lblDataDeNascimento.setBounds(482, 29, 155, 15);

			JLabel lblDataDeNascimentoNaoNula = new JLabel("Data de Nascimento inválida");
			lblDataDeNascimentoNaoNula.setBounds(482, 62, 302, 15);
			lblDataDeNascimentoNaoNula.setForeground(Color.RED);
			lblDataDeNascimentoNaoNula.setVisible(false);

			textField_DataDeNascimento = new JFormattedTextField();
			formaterDataNascimento.install(textField_DataDeNascimento);
			textField_DataDeNascimento.setBounds(482, 43, 152, 19);

			textField_DataDeNascimento.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else {
						if (isDateValid(textField_DataDeNascimento.getText())) {
							lblDataDeNascimentoNaoNula.setVisible(false);
						} else {
							lblDataDeNascimentoNaoNula.setVisible(true);
						}
					}
				}
			});
			// quando o campo perde o 'foco', salva o dado
			textField_DataDeNascimento.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else {
						lblDataDeNascimentoNaoNula.setVisible(false);
					}

				}

			});

			JLabel lblTelefone = new JLabel("Telefone");
			lblTelefone.setBounds(15, 86, 97, 15);

			JLabel lblTelefoneNaoPodeSerNula = new JLabel("Telefone não pode ser nulo");
			lblTelefoneNaoPodeSerNula.setBounds(12, 122, 249, 15);
			lblTelefoneNaoPodeSerNula.setForeground(Color.RED);
			lblTelefoneNaoPodeSerNula.setVisible(false);

			textField_Telefone = new JFormattedTextField();
			formaterTelefone.install(textField_Telefone);
			textField_Telefone.setBounds(12, 100, 155, 19);
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
			lblUsuario = new JLabel("Usuário");
			lblUsuario.setBounds(224, 86, 70, 15);

			textField_Usuario = new JTextField();
			textField_Usuario.setBounds(224, 100, 155, 19);

			lblSenha = new JLabel("Senha");
			lblSenha.setBounds(482, 85, 70, 15);

			textField_Senha = new JPasswordField();
			textField_Senha.setBounds(482, 99, 155, 19);

			lblMsgAviso = new JLabel();
			lblMsgAviso.setBounds(661, 75, 211, 15);

			btnSalvar = new JButton("Salvar");
			btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnSalvar.setBounds(662, 94, 100, 25);
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
					} else {
						checkLogin(textField_Usuario.getText(), new String(textField_Senha.getPassword()),
								textField_CPF.getText(), textField_NomeCompleto.getText(),
								textField_DataDeNascimento.getText(), textField_Telefone.getText());
					}
				}
			});

			try {
//				ConexaoBD conexaoBD = new ConexaoBD();
				Statement statement = ConexaoBD.connect.createStatement();
				String comando = "SELECT * FROM Funcionario";
				ResultSet rs = statement.executeQuery(comando);

				while (rs.next()) {
					if (rs.getString("usuario").equals(funcionarioSelecionado)) {
						idFuncionarioSelecionado = rs.getInt("id_funcionario");
						textField_CPF.setText(rs.getString("cpf"));
						textField_NomeCompleto.setText(rs.getString("nome_completo"));
						textField_Usuario.setText(rs.getString("usuario"));
						textField_DataDeNascimento.setText(rs.getString("data_nascimento"));
						textField_Telefone.setText(rs.getString("telefone"));
						textField_Senha.setText(rs.getString("senha"));
						rs.close();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			panelFuncionario.add(textField_CPF);
			panelFuncionario.add(lblCpf);
			panelFuncionario.add(lblNomeCompleto);
			panelFuncionario.add(textField_NomeCompleto);
			panelFuncionario.add(lblDataDeNascimento);
			panelFuncionario.add(textField_DataDeNascimento);
			panelFuncionario.add(lblCPFInvalido);
			panelFuncionario.add(lblAvisoNomeNulo);
			panelFuncionario.add(lblDataDeNascimentoNaoNula);
			panelFuncionario.add(textField_Telefone);
			panelFuncionario.add(lblTelefone);
			panelFuncionario.add(lblTelefoneNaoPodeSerNula);
			panelFuncionario.add(lblUsuario);
			panelFuncionario.add(textField_Usuario);
			panelFuncionario.add(lblSenha);
			panelFuncionario.add(textField_Senha);
			panelFuncionario.add(lblMsgAviso);
			panelFuncionario.add(btnSalvar);

			menu();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void checkLogin(String usuario, String senha, String cpf, String nomeCompleto, String dataNascimento,
			String telefone) {

		try {
			boolean usuarioRepetido = false;
			ConexaoBD conexaoBD = new ConexaoBD();
			Statement statement = ConexaoBD.connect.createStatement();
			String sql = "SELECT * FROM Funcionario";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (rs.getInt("id_funcionario") != idFuncionarioSelecionado) {
					if (rs.getString("usuario").equals(usuario)) {
						usuarioRepetido = true;
						lblMsgAviso.setText("Usuário já utilizado");
						lblMsgAviso.setForeground(Color.RED);
						break;
					}
					if (rs.getString("cpf").equals(cpf)) {
						usuarioRepetido = true;
						lblMsgAviso.setText("CPF já utilizado");
						lblMsgAviso.setForeground(Color.RED);
						break;
					}
				}
			}
			if (usuarioRepetido == false) {
				if ((usuario.length() > 3)) {
					if (senha.length() > 3) {

						conexaoBD.editarFuncionario(nomeCompleto, dataNascimento, telefone, cpf, usuario, senha,
								idFuncionarioSelecionado);
						lblMsgAviso.setForeground(new Color(0, 128, 0));
						lblMsgAviso.setText("Informações editadas");
						textField_DataDeNascimento.setText("");
						textField_Usuario.setText("");
						textField_Senha.setText("");
						textField_Telefone.setText("");
						textField_CPF.setText("");
						textField_NomeCompleto.setText("");
					} else {
						lblMsgAviso.setText("Senha curta");
						lblMsgAviso.setForeground(Color.RED);
					}
				} else {
					lblMsgAviso.setText("Nome de usuário curto");
					lblMsgAviso.setForeground(Color.RED);
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		frmEditarFuncionario.setJMenuBar(menuBar);

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
				frmEditarFuncionario.dispose();
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
				frmEditarFuncionario.dispose();
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
				frmEditarFuncionario.dispose();
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
					frmEditarFuncionario.dispose();
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
					frmEditarFuncionario.dispose();
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