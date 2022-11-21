package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bd.ConexaoBD;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ListaFuncionarios {

	protected JFrame frmListarFuncionarios;
	private JTable tableFuncionarios;
	private int posicaoFuncionario;
	private JButton btnEditar;

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

	/**
	 * Create the application.
	 */
	public ListaFuncionarios(int permissao) {
		this.permissao = permissao;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmListarFuncionarios = new JFrame();
		frmListarFuncionarios.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmListarFuncionarios.setIconImage(Toolkit.getDefaultToolkit().getImage(ListaFuncionarios.class.getResource("/img/iconeLaboratorio.png")));
		frmListarFuncionarios.setTitle("Lista de Funcionários");
		frmListarFuncionarios.setBounds(100, 100, 922, 600);
		frmListarFuncionarios.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmListarFuncionarios.setLocationRelativeTo(null);
		frmListarFuncionarios.setResizable(false);
		Locale.setDefault(new Locale("US"));

		JScrollPane scrollPane = new JScrollPane(tableFuncionarios);
		scrollPane.setBounds(10, 52, 886, 440);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
				new String[] { "Nome Completo", "CPF", "Data de Nascimento", "Telefone", "Usuário" });
		tableFuncionarios = new JTable();
		tableFuncionarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tableFuncionarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				posicaoFuncionario = tableFuncionarios.rowAtPoint(e.getPoint());
				btnEditar.setEnabled(true);
			}
		});

		tableFuncionarios.setModel(dtm);
		tableFuncionarios.setFocusable(false);
		tableFuncionarios.setDefaultEditor(Object.class, null);
		tableFuncionarios.setEnabled(true);
		frmListarFuncionarios.getContentPane().setLayout(null);
		frmListarFuncionarios.getContentPane().add(scrollPane);
		scrollPane.setViewportView(tableFuncionarios);

		JLabel lblListaDeFuncionarios = new JLabel("Lista de Funcionários");
		lblListaDeFuncionarios.setFont(new Font("Dialog", Font.BOLD, 18));
		lblListaDeFuncionarios.setBounds(22, 26, 234, 15);
		frmListarFuncionarios.getContentPane().add(lblListaDeFuncionarios);

		btnEditar = new JButton("Editar");
		btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEditar.setEnabled(false);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String funcionarioSelecionado = (String) tableFuncionarios.getValueAt(posicaoFuncionario, 4);
				frmListarFuncionarios.dispose();
				TelaEditarFuncionario editarFuncionario = new TelaEditarFuncionario(permissao, funcionarioSelecionado);
				editarFuncionario.frmEditarFuncionario.setVisible(true);
			}
		});

		btnEditar.setBounds(390, 497, 117, 25);
		frmListarFuncionarios.getContentPane().add(btnEditar);

		try {
//			ConexaoBD conexaoBD = new ConexaoBD();
			Statement statement = ConexaoBD.connect.createStatement();
			String comando = "SELECT * FROM Funcionario";
			ResultSet rs = statement.executeQuery(comando);

			while (rs.next()) {
				if (!rs.getString("nome_completo").equals("")) {
					dtm.addRow(new Object[] { rs.getString("nome_completo"), rs.getString("cpf"),
							rs.getString("data_nascimento"), rs.getString("telefone"), rs.getString("usuario") });
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		menu();
	}

	private void menu() {
		// Inicio do MENU
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 191, 255));
		frmListarFuncionarios.setJMenuBar(menuBar);

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
				frmListarFuncionarios.dispose();
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
				frmListarFuncionarios.dispose();
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
				frmListarFuncionarios.dispose();
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
					frmListarFuncionarios.dispose();
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
					frmListarFuncionarios.dispose();
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
