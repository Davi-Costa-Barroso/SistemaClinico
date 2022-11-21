package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

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

import org.apache.pdfbox.exceptions.COSVisitorException;

import bd.ConexaoBD;
import entidades.Atendimento;
import entidades.Medico;
import entidades.Paciente;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaAtendimento {

	protected JFrame frmHistoricoDeAtendimento;
	private JTable tableAtendimentos;
	protected DefaultTableModel dtm;
	private int posicaoAtendimento;
	private JButton btnImprimirComprovante;
	protected int requisicaoAtendimento;

	// Variaveis Menu

	private JMenu menuAtendimento;
	private JMenuBar menuBar;
	private JMenuItem menuItemNovoAtendimento;
	private JMenuItem menuItemListarAtendimento;
	private JMenuItem mntmNewMenuItem;
	private JMenu menuFuncionario;
	private JMenuItem menuItemCadastrarFuncionario;
	private JMenuItem menuItemListarFuncionarios;
	private JButton btnResultado;
	private int permissao = 0;

	/**
	 * Create the application.
	 */
	public TelaAtendimento(int permissao) {
		this.permissao = permissao;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHistoricoDeAtendimento = new JFrame();
		frmHistoricoDeAtendimento.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmHistoricoDeAtendimento.setIconImage(Toolkit.getDefaultToolkit().getImage(TelaAtendimento.class.getResource("/img/iconeLaboratorio.png")));
		frmHistoricoDeAtendimento.setTitle("Histórico de Atendimento");
		frmHistoricoDeAtendimento.setBounds(100, 100, 922, 600);
		frmHistoricoDeAtendimento.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHistoricoDeAtendimento.getContentPane().setLayout(null);
		frmHistoricoDeAtendimento.setResizable(false);
		frmHistoricoDeAtendimento.setLocationRelativeTo(null);

		JLabel lblHistorico = new JLabel("Histórico");
		lblHistorico.setFont(new Font("Dialog", Font.BOLD, 18));
		lblHistorico.setBounds(12, 23, 201, 15);
		frmHistoricoDeAtendimento.getContentPane().add(lblHistorico);

		JScrollPane scrollPane = new JScrollPane(tableAtendimentos);
		scrollPane.setBounds(22, 61, 886, 422);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		dtm = new DefaultTableModel(new Object[][] {}, new String[] { "Requisição", "Data de atendimento", "Médico" });
		tableAtendimentos = new JTable();
		tableAtendimentos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tableAtendimentos.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				posicaoAtendimento = tableAtendimentos.rowAtPoint(e.getPoint());
				requisicaoAtendimento = (int) tableAtendimentos.getValueAt(posicaoAtendimento, 0);
				btnImprimirComprovante.setEnabled(true);
				btnResultado.setEnabled(true);

			}
		});
		tableAtendimentos.setModel(dtm);
		tableAtendimentos.setFocusable(false);
		tableAtendimentos.setDefaultEditor(Object.class, null);
		tableAtendimentos.setEnabled(true);
		frmHistoricoDeAtendimento.getContentPane().setLayout(null);
		frmHistoricoDeAtendimento.getContentPane().add(scrollPane);
		scrollPane.setViewportView(tableAtendimentos);

		btnImprimirComprovante = new JButton("Comprovante");
		btnImprimirComprovante.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnImprimirComprovante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Atendimento atendimento = new ConexaoBD().getAtendimentosPorIdAtendimento(requisicaoAtendimento);
				Paciente paciente = new ConexaoBD().buscarPacientePorId(atendimento.getIdPaciente());
				Medico medico = new ConexaoBD().buscarMedicoPorId(atendimento.getIdMedicosSolicitante());
				if (atendimento != null) {
					TelaAviso aviso = new TelaAviso();
					try {
						aviso.setNomeArquivo(atendimento.gerarComprovante(paciente, medico));
					} catch (COSVisitorException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					aviso.getFrmAviso().setVisible(true);
					aviso.getFrmAviso().setLocationRelativeTo(frmHistoricoDeAtendimento);
				}

			}

		});
		btnImprimirComprovante.setBounds(297, 495, 138, 25);
		frmHistoricoDeAtendimento.getContentPane().add(btnImprimirComprovante);
		btnImprimirComprovante.setEnabled(false);

		btnResultado = new JButton("Resultado");
		btnResultado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnResultado.setEnabled(false);
		btnResultado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHistoricoDeAtendimento.dispose();
				TelaAdicionarResultados telaAdicionarResultados = new TelaAdicionarResultados(requisicaoAtendimento, permissao);
				telaAdicionarResultados.frameAdicionarResultados.setVisible(true);
				
			}
		});
		btnResultado.setBounds(459, 495, 138, 25);
		frmHistoricoDeAtendimento.getContentPane().add(btnResultado);

		menu();

	}

	private void menu() {
		// Inicio do MENU
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 191, 255));
		frmHistoricoDeAtendimento.setJMenuBar(menuBar);

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
				frmHistoricoDeAtendimento.dispose();
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
				frmHistoricoDeAtendimento.dispose();
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
				frmHistoricoDeAtendimento.dispose();
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
					frmHistoricoDeAtendimento.dispose();
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
					frmHistoricoDeAtendimento.dispose();
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
