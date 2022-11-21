package interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import bd.ConexaoBD;
import entidades.Atendimento;
import entidades.Exame;
import entidades.Medico;
import entidades.Paciente;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class TelaPaciente {

	protected JFrame frmCadastroDePaciente;
	protected JPanel panel_Exames;
	protected JPanel panel_Medico;
	protected JTextField textField_NomeCompleto;
	protected JFormattedTextField textField_CPF;
	protected JFormattedTextField textField_DataDeNascimento;
	protected JFormattedTextField textField_Crm;
	protected JTextField textField_Medico;
	private JTable table_Exames;
	protected JTextField textField_Dias;
	protected JTextField textField_Meses;
	protected JTextField textField_Anos;
	protected JTextField textField_Responsavel;
	protected JFormattedTextField textField_Telefone;
	private double valorTotalExames;
	private JButton btnSalvar;
	private JButton btnRemover;
	private int idExame;
	private boolean pacienteExiste;
	private boolean medicoExiste;

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

	public TelaPaciente(int permissao) {
		this.permissao = permissao;
		valorTotalExames = 0;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() {

		try {
			Medico medico = new Medico();

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
			frmCadastroDePaciente = new JFrame();
			frmCadastroDePaciente.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						ConexaoBD.connect.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			frmCadastroDePaciente.setIconImage(
					Toolkit.getDefaultToolkit().getImage(TelaPaciente.class.getResource("/img/iconeLaboratorio.png")));
			frmCadastroDePaciente.setTitle("Cadastro de Paciente");
			frmCadastroDePaciente.setBounds(100, 100, 922, 600);
			frmCadastroDePaciente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmCadastroDePaciente.setLocationRelativeTo(null);
			frmCadastroDePaciente.setResizable(false);
			frmCadastroDePaciente.getContentPane().setLayout(null);

			JPanel panelPaciente = new JPanel();
			panelPaciente.setBounds(12, 0, 882, 178);
			frmCadastroDePaciente.getContentPane().add(panelPaciente);
			panelPaciente
					.setBorder(new TitledBorder(null, "Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelPaciente.setLayout(null);

			JLabel lblCpf = new JLabel("CPF");
			lblCpf.setBounds(12, 29, 70, 15);

			JLabel lblCPFCampoObrigatorio = new JLabel("Campo obrigatório");
			lblCPFCampoObrigatorio.setBounds(12, 62, 136, 15);
			lblCPFCampoObrigatorio.setForeground(Color.RED);
			lblCPFCampoObrigatorio.setVisible(false);

			textField_CPF = new JFormattedTextField();
			textField_CPF.setBounds(12, 43, 155, 19);
			formaterCpf.install(textField_CPF);
			textField_CPF.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_CPF.getText().contains(" ")) {
						lblCPFCampoObrigatorio.setVisible(true);
					}
				}
			});

			textField_CPF.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_CPF.getText().contains(" ")) {
						lblCPFCampoObrigatorio.setVisible(true);
					} else {
						lblCPFCampoObrigatorio.setVisible(false);
						try {
							Paciente paciente = new ConexaoBD().buscarPacientePorCpf(textField_CPF.getText());
							if (paciente != null) {
								pacienteExiste = true;
								textField_CPF.setText(paciente.getCpf());
								textField_NomeCompleto.setText(paciente.getNomeCompleto());
								textField_DataDeNascimento.setText(paciente.getDataNascimento());
								textField_Anos.setText(String.valueOf(calcularAnos()));
								textField_Meses.setText(String.valueOf(calcularMeses()));
								textField_Dias.setText(String.valueOf(calcularDias()));
								textField_Telefone.setText(paciente.getTelefone());
								textField_Responsavel.setText(paciente.getResponsavel());
								
								if (calcularAnos() >= 18) {
									textField_Responsavel.setEnabled(false); // torna o campo não editável
								} else {
									textField_Responsavel.setEnabled(true);
								}
							} else {
								pacienteExiste = false;
							}
							lblCPFCampoObrigatorio.setVisible(false);

						} catch (Exception ex) {
							ex.printStackTrace();
						}
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

			JLabel lblResponsvelNaoPodeSerNulo = new JLabel("Campo obrigatório");
			lblResponsvelNaoPodeSerNulo.setBounds(12, 122, 249, 15);
			lblResponsvelNaoPodeSerNulo.setForeground(Color.RED);
			lblResponsvelNaoPodeSerNulo.setVisible(false);

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
						lblDataDeNascimentoNaoNula.setText("Campo obrigatório");
						lblDataDeNascimentoNaoNula.setVisible(true);

					} else {
						if (isDateValid(textField_DataDeNascimento.getText())) {
							lblDataDeNascimentoNaoNula.setVisible(false);

							textField_Dias.setText(String.valueOf(calcularDias()));
							textField_Meses.setText(String.valueOf(calcularMeses()));
							textField_Anos.setText(String.valueOf(calcularAnos()));

							if (calcularAnos() >= 18) {
								textField_Responsavel.setEnabled(false); // torna o campo não editável
								lblResponsvelNaoPodeSerNulo.setVisible(false);
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
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setText("Campo obrigatório");
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else {
						lblDataDeNascimentoNaoNula.setVisible(false);
					}

				}

			});

			JLabel lblResponsvel = new JLabel("Responsável");
			lblResponsvel.setBounds(15, 86, 97, 15);

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
			menu();
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
			panelPaciente.add(lblCPFCampoObrigatorio);
			panelPaciente.add(lblAvisoNomeNulo);
			panelPaciente.add(lblDataDeNascimentoNaoNula);
			panelPaciente.add(textField_Responsavel);
			panelPaciente.add(lblResponsvel);
			panelPaciente.add(textField_Telefone);
			panelPaciente.add(lblTelefone);
			panelPaciente.add(lblResponsvelNaoPodeSerNulo);
			panelPaciente.add(lblTelefoneNaoPodeSerNula);

			panel_Exames = new JPanel();
			panel_Exames.setBounds(12, 297, 882, 208);
			frmCadastroDePaciente.getContentPane().add(panel_Exames);
			panel_Exames.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Exames",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panel_Exames.setLayout(null);

			JComboBox<String> comboBoxExames = new JComboBox<>();
			DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
			comboBoxExames.setBounds(78, 37, 262, 24);
			comboBoxExames.setModel(dcbm);

			for (Exame exame : new ConexaoBD().listarExames()) {
				comboBoxExames.addItem(exame.getNome());
			}

			JLabel lblExame = new JLabel("Exame");
			lblExame.setBounds(12, 42, 70, 15);

			JScrollPane scrollPane = new JScrollPane(table_Exames);
			scrollPane.setBounds(20, 100, 334, 71);
			scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
			scrollPane.setViewportView(table_Exames);

			DefaultTableModel dtf = new DefaultTableModel(new String[][] {}, new Object[] { "Exame", "Preço(R$)" });

			table_Exames = new JTable();
			table_Exames.setFocusable(false);
			table_Exames.setDefaultEditor(Object.class, null);
			table_Exames.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					idExame = table_Exames.rowAtPoint(e.getPoint());
					btnRemover.setEnabled(true);
				}
			});
			table_Exames.setModel(dtf);
			table_Exames.setEnabled(true);
			scrollPane.setViewportView(table_Exames);

			JPanel panel_Pagamento = new JPanel();
			panel_Pagamento.setBounds(574, 35, 253, 136);
			panel_Pagamento.setBorder(new LineBorder(new Color(0, 0, 0)));

			JLabel lblTotal = new JLabel("TOTAL ");
			lblTotal.setBounds(13, 27, 70, 15);
			lblTotal.setFont(new Font("Dialog", Font.BOLD, 18));

			JLabel lblFormaDePagamento = new JLabel("FORMA DE PAGAMENTO");
			lblFormaDePagamento.setBounds(23, 71, 212, 15);
			lblFormaDePagamento.setFont(new Font("Dialog", Font.BOLD, 13));

			JComboBox<String> comboBox_FormaPagamento = new JComboBox<>();
			comboBox_FormaPagamento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			comboBox_FormaPagamento.setBounds(23, 99, 152, 24);
			comboBox_FormaPagamento.setModel(new DefaultComboBoxModel<String>(
					new String[] { "Dinheiro", "PIX", "Cartão de Crédito", "Cartão de Débito" }));

			JLabel lblSimboloReal = new JLabel("R$");
			lblSimboloReal.setBounds(101, 27, 39, 15);
			lblSimboloReal.setFont(new Font("Dialog", Font.BOLD, 18));

			JLabel lblValorTotal = new JLabel("0.00");
			lblValorTotal.setBounds(146, 27, 112, 15);
			lblValorTotal.setFont(new Font("Dialog", Font.BOLD, 18));

			JLabel lblAdicionePeloMenosUmExame = new JLabel("Adicione pelo menos um exame!");
			lblAdicionePeloMenosUmExame.setBounds(12, 183, 253, 15);
			lblAdicionePeloMenosUmExame.setForeground(Color.RED);
			lblAdicionePeloMenosUmExame.setVisible(false);

			JButton btnAdicionar = new JButton("Adicionar");
			btnAdicionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnAdicionar.setBounds(366, 37, 100, 25);
			btnAdicionar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					lblAdicionePeloMenosUmExame.setVisible(false);
					String exameSelecionado = comboBoxExames.getSelectedItem().toString(); // Pega o exame selecionado
					double preco = 0;
					for (Exame exame : new ConexaoBD().listarExames()) {
						if (exame.getNome().equals(exameSelecionado)) {
							preco = exame.getPreco();
						}
					}
					dtf.addRow(new Object[] { exameSelecionado, preco }); // adiciona na tabela
					valorTotalExames += preco; // soma dos valores do exame
					lblValorTotal.setText(String.format("%.2f", valorTotalExames));
					comboBoxExames.removeItem(exameSelecionado); // remove do combobox os que já foram add
					if(comboBoxExames.getSelectedItem() == null) {
						btnAdicionar.setEnabled(false);
					}
					
				}
			});

			btnRemover = new JButton("Remover");
			btnRemover.setBounds(366, 116, 100, 25);
			btnRemover.setEnabled(false);
			btnRemover.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					btnAdicionar.setEnabled(true);
					String exameSelecionado = (String) table_Exames.getValueAt(idExame, 0);
					double preco = 0;
					for (Exame exame : new ConexaoBD().listarExames()) {
						if (exame.getNome().equals(exameSelecionado)) {
							preco = exame.getPreco();
						}
					}
					dtf.removeRow(idExame);
					valorTotalExames -= preco; // soma dos valores do exame
					lblValorTotal.setText(String.format("%.2f", valorTotalExames));
					comboBoxExames.addItem(exameSelecionado);
					btnRemover.setEnabled(false);
				}
			});
			panel_Exames.add(lblExame);
			panel_Exames.add(comboBoxExames);
			panel_Exames.add(scrollPane);
			panel_Exames.add(btnAdicionar);
			panel_Exames.add(btnRemover);
			panel_Exames.add(panel_Pagamento);
			panel_Pagamento.setLayout(null);
			panel_Pagamento.add(lblTotal);
			panel_Pagamento.add(lblSimboloReal);
			panel_Pagamento.add(lblValorTotal);
			panel_Pagamento.add(comboBox_FormaPagamento);
			panel_Pagamento.add(lblFormaDePagamento);
			panel_Exames.add(lblAdicionePeloMenosUmExame);

			panel_Medico = new JPanel();
			panel_Medico.setBounds(12, 190, 882, 95);
			frmCadastroDePaciente.getContentPane().add(panel_Medico);
			panel_Medico.setPreferredSize(new Dimension(40, 10));
			panel_Medico.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "M\u00E9dico",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

			JLabel lblCrm = new JLabel("CRM");
			lblCrm.setBounds(12, 24, 70, 15);

			JLabel lblCrmInvlido = new JLabel("CRM inválido");
			lblCrmInvlido.setBounds(12, 52, 103, 27);
			lblCrmInvlido.setForeground(Color.RED);
			lblCrmInvlido.setVisible(false);

			textField_Crm = new JFormattedTextField();
			textField_Crm.setBounds(12, 39, 70, 19);
			textField_Crm.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_Crm.getText().contains(" ")) {
						lblCrmInvlido.setVisible(true);
						textField_Medico.setText("");

					} else {
						Medico medico = new ConexaoBD().buscarMedicoPorCRM(textField_Crm.getText());
						if (medico != null) {
							textField_Medico.setText(medico.getNome());
							medicoExiste = true;
						} else {
							medicoExiste = false;
						}
						lblCrmInvlido.setVisible(false);
					}
				}
			});
			formaterCrm.install(textField_Crm);
			textField_Crm.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_Crm.getText().contains(" ")) {
						lblCrmInvlido.setVisible(true);
					} else {
						lblCrmInvlido.setVisible(false);
					}
				}
			});

			JLabel lblNomeDoMedico = new JLabel("Nome do Médico");
			lblNomeDoMedico.setBounds(210, 24, 125, 15);

			JLabel lblNomeDoMdicoNaoPodeSerNulo = new JLabel("Campo obrigatório");
			lblNomeDoMdicoNaoPodeSerNulo.setBounds(210, 58, 287, 15);
			lblNomeDoMdicoNaoPodeSerNulo.setForeground(Color.RED);
			lblNomeDoMdicoNaoPodeSerNulo.setVisible(false);

			textField_Medico = new JTextField();
			textField_Medico.setBounds(210, 39, 279, 19);
			textField_Medico.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (textField_Medico.getText().isBlank()) {
						lblNomeDoMdicoNaoPodeSerNulo.setVisible(true);
					} else {
						lblNomeDoMdicoNaoPodeSerNulo.setVisible(false);
					}
				}
			});
			textField_Medico.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField_Medico.getText().isBlank()) {
						lblNomeDoMdicoNaoPodeSerNulo.setVisible(true);
					} else {
						lblNomeDoMdicoNaoPodeSerNulo.setVisible(false);

					}

				}
			});
			panel_Medico.setLayout(null);
			panel_Medico.add(lblCrm);
			panel_Medico.add(lblNomeDoMedico);
			panel_Medico.add(lblCrmInvlido);
			panel_Medico.add(textField_Crm);
			panel_Medico.add(textField_Medico);
			panel_Medico.add(lblNomeDoMdicoNaoPodeSerNulo);

			btnSalvar = new JButton("Salvar");
			btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnSalvar.setBounds(414, 507, 78, 25);
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (textField_NomeCompleto.getText().isBlank()) {
						lblAvisoNomeNulo.setVisible(true);
					} else if (textField_DataDeNascimento.getText().contains(" ")) {
						lblDataDeNascimentoNaoNula.setVisible(true);
					} else if (textField_Crm.getText().contains(" ")) {
						lblCrmInvlido.setVisible(true);
					} else if (textField_Medico.getText().isBlank()) {
						lblNomeDoMdicoNaoPodeSerNulo.setVisible(true);
					} else if (textField_Telefone.getText().contains(" ")) {
						lblTelefoneNaoPodeSerNula.setVisible(true);
					} else if (textField_Responsavel.isEnabled() && textField_Responsavel.getText().isBlank()) {
						lblResponsvelNaoPodeSerNulo.setVisible(true);
					} else if (dtf.getRowCount() == 0) {
						lblAdicionePeloMenosUmExame.setVisible(true);
					} else {
						String[] examesSolicitados = new String[dtf.getRowCount()];
						double preco = Double.parseDouble(String.format("%.2f", valorTotalExames));
						String exameSolicitado = "";
						for (int i = 0; i < dtf.getRowCount(); i++) {
							exameSolicitado = dtf.getDataVector().elementAt(i).elementAt(0).toString();
							examesSolicitados[i] = exameSolicitado;
						}
						String formaDePagamento = comboBox_FormaPagamento.getSelectedItem().toString();
						Atendimento atendimento = new Atendimento();
						atendimento.setFormaDePagamento(formaDePagamento);
						ConexaoBD conexaoBD = new ConexaoBD();
						TelaAviso aviso = new TelaAviso();
						try {
							medico.setCRM(textField_Crm.getText());
							medico.setNome(textField_Medico.getText());
							if (!medicoExiste) {
								conexaoBD.adicionarMedico(medico);
							}
							Paciente paciente = new Paciente();
							paciente.setCpf(textField_CPF.getText());
							paciente.setDataNascimento(textField_DataDeNascimento.getText());
							paciente.setNomeCompleto(textField_NomeCompleto.getText());

							paciente.setAnos(calcularAnos());
							paciente.setMeses(calcularMeses());
							paciente.setDias(calcularDias());
							paciente.setTelefone(textField_Telefone.getText());
							paciente.setResponsavel(textField_Responsavel.getText());

							atendimento.setExame(examesSolicitados);
							atendimento.setValorTotal(preco);
							atendimento.setDataAtendimento(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));

							if (pacienteExiste) {
								Paciente pacienteExistente = conexaoBD.buscarPacientePorCpf(paciente.getCpf());
								paciente.setIdPaciente(pacienteExistente.getIdPaciente());
								conexaoBD.atualizarPaciente(paciente);
							} else {
								conexaoBD.adicionarPaciente(paciente);
							}

							Paciente pacienteEncontrado = conexaoBD.buscarPacientePorCpf(paciente.getCpf());
							int idPaciente = pacienteEncontrado.getIdPaciente();

							Medico medicoEncontrado = conexaoBD.buscarMedicoPorCRM(medico.getCRM());
							int idMedico = medicoEncontrado.getIdMedico();

							atendimento.setIdMedicoSolicitante(idMedico);
							atendimento.setIdPaciente(idPaciente);

							conexaoBD.adicionarAtendimento(atendimento);

							Atendimento atendimentoEncontrado = conexaoBD.getUltimoAtendimento();
							aviso.setNomeArquivo(
									atendimentoEncontrado.gerarComprovante(pacienteEncontrado, medicoEncontrado));
							aviso.getFrmAviso().setLocationRelativeTo(frmCadastroDePaciente); // centraliza de acordo
							aviso.getFrmAviso().setVisible(true); // torna a tela de aviso visível
							aviso.getFrmAviso().setLocationRelativeTo(frmCadastroDePaciente);

							textField_CPF.setText("");
							textField_Anos.setText("");
							textField_Meses.setText("");
							textField_Dias.setText("");

							textField_DataDeNascimento.setText("");
							textField_NomeCompleto.setText("");
							textField_Crm.setText("");
							textField_Medico.setText("");
							textField_Telefone.setText("");
							textField_Responsavel.setText("");

							for (int i = dtf.getRowCount() - 1; i >= 0; i--) {
								comboBoxExames.addItem(dtf.getDataVector().elementAt(i).elementAt(0).toString());
								dtf.removeRow(i);

							}
							lblValorTotal.setText("0.00");
							valorTotalExames = 0;

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			});
			frmCadastroDePaciente.getContentPane().add(btnSalvar);
		} catch (Exception e) {
			System.out.println(e);
		}
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
		frmCadastroDePaciente.setJMenuBar(menuBar);

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
				frmCadastroDePaciente.dispose();
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
				frmCadastroDePaciente.dispose();
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
				frmCadastroDePaciente.dispose();
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
					frmCadastroDePaciente.dispose();
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
					frmCadastroDePaciente.dispose();
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
