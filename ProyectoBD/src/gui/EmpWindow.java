package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class EmpWindow extends JFrame{
	/**
	 * Autogenerated serialVersionUID for JFrame
	 */
	private static final long serialVersionUID = -2735425409835907346L;

	private Connection conn = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmpWindow window = new EmpWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmpWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 260, 120);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel passPanel = new JPanel();
		passPanel.setLayout(new FlowLayout());
		JLabel inserte = new JLabel("Inserte Legajo: ");
		inserte.setPreferredSize(new Dimension(90, 20));
		passPanel.add(inserte);
		JTextField legajo = new JTextField();
		legajo.setPreferredSize(new Dimension(100, 20));
		passPanel.add(legajo);
		legajo.setVisible(true);
		legajo.setEditable(true);
		legajo.setFocusable(true);
		JPasswordField password = new JPasswordField(20);
		passPanel.add(password);
		password.setVisible(true);
		password.setEditable(true);
		password.setFocusable(true);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
             String pass = String.valueOf(password.getPassword());
             String nLeg = String.valueOf(legajo.getText());
             tryConnect(nLeg, pass);
          }
        });
		submit.setPreferredSize(new Dimension(80, 30));
		this.getRootPane().setDefaultButton(submit);
		passPanel.add(submit);
		this.add(passPanel);
		passPanel.setBounds(100, 100, 240, 100);
	}

	private void tryConnect(String legajo, String pass) {
		String server = "localhost:3306";
		String dataBase = "vuelos";
		String user = "admin";
		String password = "admin";
		String url = "jdbc:mysql://" + server + "/" +dataBase+
		"?serverTimezone=America/Argentina/Buenos_Aires";
		try {
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			String comando = "SELECT * FROM EMPLEADOS WHERE legajo = ' " + legajo + "' AND password = md5('" + pass + "')";
			ResultSet rs = stmt.executeQuery(comando);
			boolean found = false;
			while(!found && rs.next()) {
				found = null != rs.getString("legajo");
			}
			if (!found) {
				JOptionPane.showMessageDialog(null, "El empleado no se pudo hallar en la base de datos.", "Error de Autenticaci�n", JOptionPane.ERROR_MESSAGE);
			}
			else {
				conn.close();
				conn = DriverManager.getConnection(url, "empleado", "empleado");
			}
		}
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Connection Refused", JOptionPane.ERROR_MESSAGE);
		}
	}
}