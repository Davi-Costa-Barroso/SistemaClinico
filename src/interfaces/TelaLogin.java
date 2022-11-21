package interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bd.ConexaoBD;

public class TelaLogin {

	protected JFrame frmTelaLogin;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin window = new TelaLogin();
					window.frmTelaLogin.setVisible(true);
					ConexaoBD conexaoBD = new ConexaoBD();
					conexaoBD.iniciar();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTelaLogin = new JFrame();
		frmTelaLogin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ConexaoBD.connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmTelaLogin.getContentPane().setBackground(new Color(176, 214, 223));
		frmTelaLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(TelaLogin.class.getResource("/img/iconeLaboratorio.png")));
		frmTelaLogin.setTitle("Sistema Clínico");
		frmTelaLogin.setBounds(100, 100, 922, 600);
		frmTelaLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTelaLogin.setResizable(false);
		frmTelaLogin.setLocationRelativeTo(null);
		Locale.setDefault(new Locale("US"));

		Panel panel = new Panel();
		frmTelaLogin.getContentPane().add(panel, BorderLayout.CENTER);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(verticalBox);

		Component rigidArea_7 = Box.createRigidArea(new Dimension(20, 20));
		verticalBox.add(rigidArea_7);

		JLabel lblBemVindo = new JLabel("Bem-vindo! ");
		lblBemVindo.setForeground(new Color(1, 26, 76));
		lblBemVindo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblBemVindo);

		JLabel lblFacaLoginParaAcesso = new JLabel("Faça login para ter acesso ao sistema.");
		lblFacaLoginParaAcesso.setForeground(new Color(1, 26, 76));
		lblFacaLoginParaAcesso.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFacaLoginParaAcesso.setFont(new Font("Tahoma", Font.PLAIN, 16));
		verticalBox.add(lblFacaLoginParaAcesso);

		Component rigidArea_6 = Box.createRigidArea(new Dimension(20, 20));
		verticalBox.add(rigidArea_6);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogin.setAlignmentY(Component.TOP_ALIGNMENT);
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setHorizontalTextPosition(SwingConstants.CENTER);
		lblLogin.setForeground(new Color(1, 26, 76));
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 28));
		verticalBox.add(lblLogin);

		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		verticalBox.add(rigidArea_3);

		JLabel lblUsuario = new JLabel("Usuário");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsuario.setForeground(new Color(1, 26, 76));
		lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblUsuario);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		rigidArea.setPreferredSize(new Dimension(20, 10));
		verticalBox.add(rigidArea);

		textField = new JTextField();
		textField.setForeground(Color.BLACK);
		textField.setMaximumSize(new Dimension(120, 120));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		verticalBox.add(textField);
		textField.setColumns(10);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		verticalBox.add(rigidArea_1);

		JLabel lblNewLabel_1 = new JLabel("Senha");
		lblNewLabel_1.setForeground(new Color(1, 26, 76));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		verticalBox.add(lblNewLabel_1);

		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_2.setPreferredSize(new Dimension(20, 10));
		verticalBox.add(rigidArea_2);

		passwordField = new JPasswordField();
		passwordField.setForeground(Color.BLACK);
		passwordField.setMaximumSize(new Dimension(120, 120));
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		verticalBox.add(passwordField);

		Component rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		verticalBox.add(rigidArea_4);
		Label msgErro = new Label();
		msgErro.setAlignment(Label.CENTER);
		msgErro.setForeground(Color.RED);
		msgErro.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLogin(textField.getText(), new String(passwordField.getPassword()), msgErro);

			}
		});
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setForeground(new Color(1, 26, 76));
		verticalBox.add(btnNewButton);

		Component rigidArea_5 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_5.setPreferredSize(new Dimension(20, 10));
		verticalBox.add(rigidArea_5);
		verticalBox.add(msgErro);

	}

	public void checkLogin(String usuario, String senha, Label label) {
		try {
			Statement statement = ConexaoBD.connect.createStatement();
			String comando = "SELECT * FROM Funcionario";
			ResultSet rs = statement.executeQuery(comando);
			boolean usuarioEncontrado = false;
			while (rs.next()) {
				if (rs.getString("usuario").equals(usuario)) {
					usuarioEncontrado = true;
					if (rs.getString("senha").equals(senha)) {
						frmTelaLogin.dispose();
						int permissao = (rs.getInt("id_funcionario") == 1) ? 1 : 0;
						TelaInicial telaFuncionario = new TelaInicial(permissao);
						telaFuncionario.frmTelaInicial.setVisible(true);
					} else {
						label.setText("Senha incorreta!");
						passwordField.setText("");
						break;
					}
				}
			}
			if (!usuarioEncontrado) {
				label.setText("Usuário não encontrado!");
				textField.setText("");
				passwordField.setText("");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
