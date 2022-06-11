import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.sql.Statement;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class IMCVFrame extends JFrame implements SQL {

	private static final long serialVersionUID = 8056549773322115623L;
	private JPanel contentPane;
	private static Statement stmt;
	private JTextField emailField;
	private JTextField roadField;
	private JTextField nickField;
	private JTextField civicoField;
	private JPasswordField passwordField;
	private JTextField capField;
	private JTextField villageField;
	private JTextField dataField;
	private JTextField whereField;

	public IMCVFrame(Statement stmt) {

		IMCVFrame.stmt = stmt;

		setTitle("EuroGames - Inserimento, modifica, cancellazione e visualizzazione");
		Utils.spawnCenter(this);
		initialize();

	}

	private void initialize() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		JComboBox<String> operationsBox = new JComboBox<String>();
		String[] operations = { "INSERT", "UPDATE", "DELETE", "SELECT" };

		for (String operation : operations)
			operationsBox.addItem(operation);

		panel_1.add(operationsBox);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new CardLayout(0, 0));

		JPanel insertPanel = new JPanel();
		panel_2.add(insertPanel, "insertPanel");
		insertPanel.setLayout(new MigLayout("", "[][][][grow]", "[][][][][][][][][][][][][][]"));

		JLabel lblNewLabel = new JLabel("E-Mail");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel, "cell 1 1");

		emailField = new JTextField();
		insertPanel.add(emailField, "flowx,cell 3 1,growx");
		emailField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Nickname");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_1, "cell 1 3");

		JLabel lblNewLabel_2 = new JLabel("Via/Piazza");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_2, "cell 3 1");

		roadField = new JTextField();
		insertPanel.add(roadField, "cell 3 1,growx");
		roadField.setColumns(10);

		nickField = new JTextField();
		insertPanel.add(nickField, "flowx,cell 3 3,growx");
		nickField.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Numero civico");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_3, "cell 3 3");

		civicoField = new JTextField();
		insertPanel.add(civicoField, "cell 3 3,growx");
		civicoField.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Password");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_4, "cell 1 5");

		passwordField = new JPasswordField();
		insertPanel.add(passwordField, "flowx,cell 3 5,growx");

		JLabel lblNewLabel_5 = new JLabel("CAP");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_5, "cell 3 5");

		capField = new JTextField();
		insertPanel.add(capField, "cell 3 5,growx");
		capField.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("Paese");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_6, "cell 1 7");

		villageField = new JTextField();
		insertPanel.add(villageField, "flowx,cell 3 7,growx");
		villageField.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Data");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_7, "cell 3 7");
		lblNewLabel_7.setVisible(false);

		dataField = new JTextField();
		insertPanel.add(dataField, "cell 3 7,growx");
		dataField.setColumns(10);

		JLabel lblNewLabel_8 = new JLabel("WHERE");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 15));
		insertPanel.add(lblNewLabel_8, "cell 1 9");
		lblNewLabel_8.setVisible(false);

		whereField = new JTextField();
		insertPanel.add(whereField, "cell 3 9,growx");
		whereField.setColumns(10);
		whereField.setVisible(false);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JTextArea queryString = new JTextArea();
		queryString.setFont(new Font("Monospaced", Font.PLAIN, 15));
		queryString.setEditable(false);
		panel_3.add(queryString, BorderLayout.CENTER);

		JButton executeInsBtn = new JButton("Esegui");
		insertPanel.add(executeInsBtn, "cell 0 13 4 1,alignx center,aligny center");

		JPanel selectPanel = new JPanel();
		panel_2.add(selectPanel, "selectPanel");
		selectPanel.setLayout(new MigLayout("", "[][][][grow]", "[][][][][][][][][][][][][][]"));

		executeInsBtn.addActionListener(e -> {

			final String operation = (String) operationsBox.getSelectedItem();

			String nick = nickField.getText(), email = emailField.getText(),
					password = new String(passwordField.getPassword()), civico = civicoField.getText(),
					road = roadField.getText(), village = villageField.getText(), cap = capField.getText();

			if (operation.equalsIgnoreCase("Insert")) {

				if (nick.length() == 0 || email.length() == 0 || password.length() == 0 || civico.length() == 0
						|| road.length() == 0 || village.length() == 0 || cap.length() == 0) {
					JOptionPane.showMessageDialog(this, "Tutti i campi (*) sono obbligatori!", "Errore",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String query = String.format(
						"INSERT INTO Utenti VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", email.trim(),
						nick.trim(), password, new java.sql.Date(System.currentTimeMillis()).toString(), civico, road,
						village, cap);

				executeDML(query, queryString);

			} else if (operation.equalsIgnoreCase("Update")) {

				String data = dataField.getText(), where = whereField.getText();

				if (nick.length() == 0 && email.length() == 0 && password.length() == 0 && civico.length() == 0
						&& road.length() == 0 && village.length() == 0 && cap.length() == 0 && data.length() == 0) {

					JOptionPane.showMessageDialog(this, "Popola almeno un campo!", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (where.length() == 0) {
					JOptionPane.showMessageDialog(this, "La clausola WHERE non può essere vuota!", "Errore",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String query = "UPDATE Utenti SET ";

				if (nick.length() > 0)
					query += "nickname = '" + nick + "', ";

				if (email.length() > 0)
					query += "email = '" + email + "', ";

				if (password.length() > 0)
					query += "password_utente = '" + password + "', ";

				if (civico.length() > 0)
					query += "n_civico = " + civico + ", ";

				if (village.length() > 0)
					query += "paese = '" + village + "', ";

				if (road.length() > 0)
					query += "via_piazza = '" + road + "', ";

				if (cap.length() > 0)
					query += "cap = '" + cap + "', ";

				if (data.length() > 0)
					query += "data_registrazione = '" + data + "', ";

				query = query.substring(0, query.length() - 2) + " ";

				query += "WHERE " + where;

				executeDML(query, queryString);

			} else if (operation.equalsIgnoreCase("Delete")) {

				String where = whereField.getText();

				if (where.length() == 0) {
					JOptionPane.showMessageDialog(this, "La clausola WHERE non può essere vuota!", "Errore",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				executeDML("DELETE FROM Utenti WHERE " + where, queryString);
			}

		});
		dataField.setVisible(false);

		operationsBox.addItemListener(e -> {

			final String operation = (String) operationsBox.getSelectedItem();

			nickField.setText(null);
			passwordField.setText(null);
			emailField.setText(null);
			capField.setText(null);
			civicoField.setText(null);
			roadField.setText(null);
			nickField.setText(null);
			villageField.setText(null);
			dataField.setText(null);
			whereField.setText(null);
			
			if (operation.equalsIgnoreCase("Update")) {

				nickField.setVisible(true);
				passwordField.setVisible(true);
				emailField.setVisible(true);
				capField.setVisible(true);
				civicoField.setVisible(true);
				roadField.setVisible(true);
				villageField.setVisible(true);

				lblNewLabel.setVisible(true);
				lblNewLabel_1.setVisible(true);
				lblNewLabel_2.setVisible(true);
				lblNewLabel_3.setVisible(true);
				lblNewLabel_4.setVisible(true);
				lblNewLabel_5.setVisible(true);
				lblNewLabel_6.setVisible(true);

				dataField.setVisible(true);
				lblNewLabel_7.setVisible(true);
				whereField.setVisible(true);
				lblNewLabel_8.setVisible(true);

			} else if (operation.equalsIgnoreCase("Insert")) {

				nickField.setVisible(true);
				passwordField.setVisible(true);
				emailField.setVisible(true);
				capField.setVisible(true);
				civicoField.setVisible(true);
				roadField.setVisible(true);
				villageField.setVisible(true);

				lblNewLabel.setVisible(true);
				lblNewLabel_1.setVisible(true);
				lblNewLabel_2.setVisible(true);
				lblNewLabel_3.setVisible(true);
				lblNewLabel_4.setVisible(true);
				lblNewLabel_5.setVisible(true);
				lblNewLabel_6.setVisible(true);

				dataField.setVisible(false);
				lblNewLabel_7.setVisible(false);
				whereField.setVisible(false);
				lblNewLabel_8.setVisible(false);

			} else if (operation.equalsIgnoreCase("Delete")) {

				whereField.setVisible(true);
				lblNewLabel_8.setVisible(true);

				nickField.setVisible(false);
				passwordField.setVisible(false);
				emailField.setVisible(false);
				dataField.setVisible(false);
				capField.setVisible(false);
				civicoField.setVisible(false);
				roadField.setVisible(false);
				villageField.setVisible(false);

				lblNewLabel.setVisible(false);
				lblNewLabel_1.setVisible(false);
				lblNewLabel_2.setVisible(false);
				lblNewLabel_3.setVisible(false);
				lblNewLabel_4.setVisible(false);
				lblNewLabel_5.setVisible(false);
				lblNewLabel_6.setVisible(false);
				lblNewLabel_7.setVisible(false);
			}

			if (operation.equalsIgnoreCase("select"))
				((CardLayout) panel_2.getLayout()).show(panel_2, "selectPanel");
			else
				((CardLayout) panel_2.getLayout()).show(panel_2, "insertPanel");

		});

		JCheckBox emailBox = new JCheckBox("E-Mail");
		emailBox.setSelected(true);
		emailBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(emailBox, "cell 1 1 2 1,alignx center");

		JCheckBox roadBox = new JCheckBox("Via/Piazza");
		roadBox.setSelected(true);
		roadBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(roadBox, "cell 3 1,alignx center");

		JCheckBox nickBox = new JCheckBox("Nickname");
		nickBox.setSelected(true);
		nickBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(nickBox, "cell 1 3 2 1,alignx center");

		JCheckBox civicoBox = new JCheckBox("Numero civico");
		civicoBox.setSelected(true);
		civicoBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(civicoBox, "cell 3 3,alignx center");

		JCheckBox passwordBox = new JCheckBox("Password");
		passwordBox.setSelected(true);
		passwordBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(passwordBox, "cell 1 5 2 1,alignx center");

		JCheckBox capBox = new JCheckBox("CAP");
		capBox.setSelected(true);
		capBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(capBox, "cell 3 5,alignx center");

		JCheckBox villageBox = new JCheckBox("Paese");
		villageBox.setSelected(true);
		villageBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(villageBox, "cell 1 7 2 1,alignx center");

		JCheckBox dataBox = new JCheckBox("Data");
		dataBox.setSelected(true);
		dataBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(dataBox, "cell 3 7,alignx center");

		JLabel lblNewLabel_8_select = new JLabel("WHERE");
		lblNewLabel_8_select.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectPanel.add(lblNewLabel_8_select, "cell 1 9");
		lblNewLabel_8_select.setVisible(true);

		JTextField whereField_select = new JTextField();
		selectPanel.add(whereField_select, "cell 3 9,growx");
		whereField_select.setColumns(10);

		JButton executeSelectBtn = new JButton("Esegui");
		selectPanel.add(executeSelectBtn, "cell 0 13 4 1,alignx center");

		JPanel tablePanel = new JPanel();
		panel_2.add(tablePanel, "tablePanel");
		tablePanel.setLayout(new BorderLayout(0, 0));

		JTextArea queryStringSelect = new JTextArea();
		tablePanel.add(queryStringSelect, BorderLayout.SOUTH);
		whereField_select.setVisible(true);

		executeSelectBtn.addActionListener(e -> {
			String query = "SELECT ";

			if (emailBox.isSelected())
				query += "email, ";

			if (nickBox.isSelected())
				query += "nickname, ";

			if (passwordBox.isSelected())
				query += "password_utente, ";

			if (dataBox.isSelected())
				query += "data_registrazione, ";

			if (civicoBox.isSelected())
				query += "n_civico, ";

			if (roadBox.isSelected())
				query += "via_piazza, ";

			if (villageBox.isSelected())
				query += "paese, ";

			if (capBox.isSelected())
				query += "cap, ";

			query = query.substring(0, query.length() - 2) + " ";

			query += " FROM Utenti";

			BorderLayout layout = (BorderLayout) tablePanel.getLayout();

			if (layout.getLayoutComponent(BorderLayout.CENTER) != null)
				tablePanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

			if (whereField_select.getText().length() > 0)
				query += " WHERE " + whereField_select.getText();

			DefaultTableModel model = SQL.createResultsTable(this, stmt, query, queryStringSelect, query);
			
			queryString.setText(null);
			
			if (model == null)
				return;
			
			tablePanel.add(
					new JScrollPane(new JTable(model)),
					BorderLayout.CENTER);

			((CardLayout) panel_2.getLayout()).show(panel_2, "tablePanel");
		});

	}

	private void executeDML(String query, JTextArea queryString) {
		try {
			// Ritorna >= 0 se va a buon fine la query (> 0 solo per l'insert)
			if (stmt.executeUpdate(query) > -1)
				queryString.setText("\n" + query + "\n");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}