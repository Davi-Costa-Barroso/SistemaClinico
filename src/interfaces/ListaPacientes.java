package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
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
import entidades.Atendimento;
import entidades.Medico;
import entidades.Paciente;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ListaPacientes {

	protected JFrame frmListarPacientes;
	private JTable tablePacientes;
	private int posicaoPaciente;
	private JButton btnHistorico;
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
	public ListaPacientes(int permissao) {
		this.permissao = permissao;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmListarPacientes = new JFrame();
		frmListarPacientes.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmListarPacientes.setIconImage(Toolkit.getDefaultToolkit().getImage(ListaPacientes.class.getResource("/img/iconeLaboratorio.png")));
		frmListarPacientes.setTitle("Lista de Pacientes");
		frmListarPacientes.setBounds(100, 100, 922, 600);
		frmListarPacientes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmListarPacientes.setLocationRelativeTo(null);
		frmListarPacientes.setResizable(false);
		Locale.setDefault(new Locale("US"));

		JScrollPane scrollPane = new JScrollPane(tablePacientes);
		scrollPane.setBounds(10, 52, 886, 440);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
				new String[] { "Nome Completo", "CPF", "Data de Nascimento", "Telefone", "Responsável" });
		tablePacientes = new JTable();
		tablePacientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tablePacientes.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				posicaoPaciente = tablePacientes.rowAtPoint(e.getPoint());
				btnHistorico.setEnabled(true);
				btnEditar.setEnabled(true);

			}
		});
		tablePacientes.setModel(dtm);
		tablePacientes.setFocusable(false);
		tablePacientes.setDefaultEditor(Object.class, null);
		tablePacientes.setEnabled(true);
		frmListarPacientes.getContentPane().setLayout(null);
		frmListarPacientes.getContentPane().add(scrollPane);
		scrollPane.setViewportView(tablePacientes);

		JLabel lblListaDePacienes = new JLabel("Lista de Pacientes");
		lblListaDePacienes.setFont(new Font("Dialog", Font.BOLD, 18));
		lblListaDePacienes.setBounds(22, 26, 234, 15);
		frmListarPacientes.getContentPane().add(lblListaDePacienes);

		btnHistorico = new JButton("Histórico");
		btnHistorico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHistorico.setEnabled(false);
		btnHistorico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmListarPacientes.dispose();
				TelaAtendimento telaAtendimento = new TelaAtendimento(permissao);
				telaAtendimento.frmHistoricoDeAtendimento.setVisible(true);
				String cpfPacienteSelecionado = (String) tablePacientes.getValueAt(posicaoPaciente, 1);

				ConexaoBD conexaoBD = new ConexaoBD();
				Paciente paciente = conexaoBD.buscarPacientePorCpf(cpfPacienteSelecionado);
				List<Atendimento> atendimentos = conexaoBD.getAtendimentosPorPaciente(paciente.getIdPaciente());
				Medico medico;
				for (Atendimento atendimento : atendimentos) {
					medico = conexaoBD.buscarMedicoPorId(atendimento.getIdMedicosSolicitante());
					telaAtendimento.dtm.addRow(new Object[] { atendimento.getIdAtendimento(),
							atendimento.getDataAtendimento(), medico.getNome() });
				}
			}
		});
		btnHistorico.setBounds(330, 504, 104, 25);
		frmListarPacientes.getContentPane().add(btnHistorico);

		btnEditar = new JButton("Editar");
		btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEditar.setEnabled(false);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmListarPacientes.dispose();

				try {
					String cpfSelecionado = (String) tablePacientes.getValueAt(posicaoPaciente, 1);
					TelaEditarPaciente editarPaciente = new TelaEditarPaciente(permissao, cpfSelecionado);

					Paciente paciente = new ConexaoBD().buscarPacientePorCpf(cpfSelecionado);
					editarPaciente.frmEditarPaciente.setVisible(true);
					editarPaciente.textField_CPF.setText(paciente.getCpf());
					editarPaciente.textField_NomeCompleto.setText(paciente.getNomeCompleto());
					editarPaciente.textField_DataDeNascimento.setText(paciente.getDataNascimento());
					editarPaciente.textField_Anos.setText(String.valueOf(editarPaciente.calcularAnos()));
					editarPaciente.textField_Meses.setText(String.valueOf(editarPaciente.calcularMeses()));
					editarPaciente.textField_Dias.setText(String.valueOf(editarPaciente.calcularDias()));
					editarPaciente.textField_Telefone.setText(paciente.getTelefone());
					editarPaciente.textField_Responsavel.setText(paciente.getResponsavel());

					if(!paciente.getResponsavel().isBlank()) {
						editarPaciente.textField_Responsavel.setEnabled(true);
					}

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			}
		});
		btnEditar.setBounds(446, 504, 117, 25);
		frmListarPacientes.getContentPane().add(btnEditar);

		menu();
		try {
			List<Paciente> pacientes = new ConexaoBD().listarPacientes();

			for (Paciente paciente : pacientes) {
				dtm.addRow(new Object[] { paciente.getNomeCompleto(), paciente.getCpf(), paciente.getDataNascimento(),
						paciente.getTelefone(), paciente.getResponsavel() });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void menu() {
		// Inicio do MENU
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 191, 255));
		frmListarPacientes.setJMenuBar(menuBar);

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
				frmListarPacientes.dispose();
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
				frmListarPacientes.dispose();
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
				frmListarPacientes.dispose();
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
					frmListarPacientes.dispose();
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
					frmListarPacientes.dispose();
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
