package interfaces;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TelaAviso {

	private JFrame frmAviso;
	private String nomeArquivo;
	/**
	 * Create the application.
	 */
	public TelaAviso() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAviso = new JFrame();
		frmAviso.setIconImage(Toolkit.getDefaultToolkit().getImage(TelaAviso.class.getResource("/img/iconeLaboratorio.png")));
		frmAviso.setTitle("Gerar comprovante");

		frmAviso.setBounds(100, 100, 351, 125);
		frmAviso.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAviso.getContentPane().setLayout(null);
		frmAviso.setResizable(false);
		
		JButton btnAcao = new JButton("Abrir");
		btnAcao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(new File(nomeArquivo));
						frmAviso.dispose();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnAcao.setBounds(107, 54, 117, 25);
		frmAviso.getContentPane().add(btnAcao);
		
		JLabel lblAviso = new JLabel("Comprovante de Atendimento");
		lblAviso.setFont(new Font("Dialog", Font.BOLD, 14));
		lblAviso.setBounds(54, 27, 247, 15);
		frmAviso.getContentPane().add(lblAviso);
	}

	public JFrame getFrmAviso() {
		return frmAviso;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	
}
