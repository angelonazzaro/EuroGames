import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.awt.FlowLayout;

public class QueryFrame extends JFrame implements SQL {

	private static final long serialVersionUID = 1236520936553783185L;
	private JPanel contentPane;
	private static Statement stmt;

	public QueryFrame(Statement stmt) {

		QueryFrame.stmt = stmt;
		
		Utils.spawnCenter(this);
		initialize();
	}

	private void initialize() {
		
		UIManager.put("Table.font", new FontUIResource("Tahoma", Font.PLAIN, 15));
		
		setTitle("EuroGames - Query");
		setBounds(100, 100, 541, 456);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		contentPane = (JPanel) getContentPane();

		contentPane.setLayout(new CardLayout(0, 0));

		generateContent();

	}
	
	private void generateContent() {

		JPanel crudPanel = new JPanel();
		getContentPane().add(crudPanel, "crudPanel");
		crudPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		crudPanel.add(panel, BorderLayout.NORTH);

		JComboBox<String> operationsBox = new JComboBox<String>();
		operationsBox.setMaximumRowCount(9);

		operationsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				// Se viene selezionata la prima opzione,
				// è possibile inserire una query a mano
				
				if (operationsBox.getSelectedIndex() == 0) {
					operationsBox.setEditable(true);
					operationsBox.setSelectedItem("");

				} else if (operationsBox.getSelectedIndex() != -1) {
					operationsBox.setEditable(false);
				}

			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel = new JLabel("Query");
		panel.add(lblNewLabel);
		panel.add(operationsBox);

		JLabel label = new JLabel("");
		panel.add(label);

		JLabel label_1 = new JLabel("");
		panel.add(label_1);

		JButton executeBtn = new JButton("Esegui");
		panel.add(executeBtn);

		String[] operations = { "QUERY PERSONALIZZATA",
				"Op.1 - SELEZIONARE LE CONSOLE DELLA SONY O DELLA MICROSOFT PUBBLICATE TRA IL 2010 E IL 2021 ORDINATE PER PREZZO (* - {titolo})",
				"Op.2 - SELEZIONARE GLI UTENTI CHE HANNO ACQUISTATO LA CONSOLE \"PS5\" NELL'ULTIMO MESE (email, nickname)",
				"Op.3 - VISUALIZZARE L'INCASSO GIORNALIERO (incasso_giornaliero)",
				"Op.4 - VISUALIZZARE L'INCASSO MENSILE PER OGNI UTENTE(email, nickname, incasso_mensile)",
				"Op.5 - SELEZIONARE I GIOCHI CON UN NUMERO DI VENDITE SUPERIORE A 100 (codice_prodotto, titolo, data_pubblicazione, n_vendite)",
				"Op.6 - SELEZIONARE GLI UTENTI CHE HANNO ACQUISTATO IL MAGGIOR NUMERO DI GIOCHI CON UNA SPESA COMPLESSIVA SUPERIORE A 100€ (email, nickname, spesa_complessiva)",
				"Op.7 - SELEZIONARE LE CARTE DI CREDITO CHE SONO STATE ASSOCIATE ALL'ACCOUNT \"mickymouse@gmail.com\" MA CHE NON SONO STATE ASSOCIATE ALL'ACCOUNT \"giampix@gmail.com\" (Carte *)",
				"Op.8 - SELEZIONARE TUTTI GLI UTENTI CHE HANNO ACQUISTATO TUTTE LE CONSOLE (Utenti *)" };
		
		
		for (int i = 0; i < operations.length; i++)
			operationsBox.addItem(operations[i]);

		operationsBox.setPreferredSize(new Dimension(300, 25));
		operationsBox.setMaximumSize(new Dimension(300, 25));

		JPanel panel_1 = new JPanel();
		crudPanel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JTextArea queryString = new JTextArea();
		queryString.setFont(new Font("Tahoma", Font.PLAIN, 15));
		queryString.setEditable(false);
		panel_1.add(queryString);

		String[] queries = {
				"SELECT codice_prodotto, tipo, data_pubblicazione, descrizione, prezzo_base, quantita_disponibile, n_vendite, modello, produttore "
						+ "FROM Prodotti  "
						+ "WHERE data_pubblicazione >= \"2010-01-01\" AND data_pubblicazione <= \"2021-12-31\"  "
						+ "      AND tipo = \"Console\" AND (produttore = \"Sony\" OR produttore = \"Microsoft\") "
						+ "ORDER BY prezzo_base;",

				"SELECT u.email, u.nickname, a.quantita, a.prezzo_finale " + "FROM utenti u, acquisti a, prodotti p "
						+ "WHERE u.email = a.email_utente AND a.codice_prodotto = p.codice_prodotto  "
						+ "      AND p.tipo = \"Console\" AND p.modello = \"PS5\"  "
						+ "      AND a.data_ora_acquisto >= DATE_ADD(NOW(), INTERVAL -1 MONTH) AND a.data_ora_acquisto <= NOW();",

				"SELECT SUM(prezzo_finale) AS incasso_giornaliero " + "FROM Acquisti "
						+ "WHERE DATE(data_ora_acquisto) = CURDATE();",

				"SELECT u.email, u.nickname, SUM(a.prezzo_finale) AS incasso_mensile " + "FROM utenti u, acquisti a "
						+ "WHERE u.email = a.email_utente   "
						+ "      AND a.data_ora_acquisto >= DATE_ADD(NOW(), INTERVAL -1 MONTH) AND a.data_ora_acquisto <= NOW() "
						+ "GROUP BY u.email, u.nickname;",

				"SELECT codice_prodotto, titolo, data_pubblicazione, n_vendite " + "FROM Prodotti "
						+ "WHERE tipo = \"Gioco\" AND n_vendite > 3;",

				"CREATE VIEW SpeseComplessive AS ( "
						+ "    SELECT u.email, u.nickname, SUM(a.prezzo_finale) AS spesa_complessiva, SUM(a.quantita) AS tot_giochi "
						+ "    FROM Utenti u, Acquisti a, Prodotti p "
						+ "    WHERE u.email = a.email_utente AND a.codice_prodotto = p.codice_prodotto  "
						+ "          AND p.tipo = \"Gioco\" " + "    GROUP BY u.email, u.nickname " + ");" + ""
						+ "SELECT email, nickname, spesa_complessiva " + "FROM SpeseComplessive "
						+ "WHERE tot_giochi >= ALL (SELECT tot_giochi "
						+ "                     FROM SpeseComplessive) AND spesa_complessiva > 100.00;",

				"SELECT c.* " + "FROM Carte c, Associare a "
						+ "WHERE c.n_carta = a.n_carta AND a.email_utente = \"mickymouse@gmail.com\"  "
						+ "      AND c.n_carta NOT IN (SELECT c.n_carta "
						+ "                            FROM Carte c, Associare a "
						+ "                            WHERE c.n_carta = a.n_carta AND a.email_utente = \"giampix@gmail.com\");",

				"SELECT u.* " + "FROM Utenti u " + "WHERE NOT EXISTS (SELECT c.codice_prodotto "
						+ "                  FROM Prodotti c "
						+ "                  WHERE c.tipo = \"Console\" AND NOT EXISTS (SELECT a.email_utente "
						+ "                                                           FROM Acquisti a "
						+ "                                                           WHERE u.email = a.email_utente AND c.codice_prodotto = a.codice_prodotto));" };

		String[] queriesString = {

				"SELECT codice_prodotto, tipo, data_pubblicazione, descrizione, prezzo_base, quantita_disponibile, n_vendite, modello, produttore\n"
						+ "FROM Prodotti \n"
						+ "WHERE data_pubblicazione >= \"2010-01-01\" AND data_pubblicazione <= \"2021-12-31\" \n"
						+ "      AND tipo = \"Console\" AND (produttore = \"Sony\" OR produttore = \"Microsoft\")\n"
						+ "ORDER BY prezzo_base;",

				"SELECT u.email, u.nickname, a.quantita, a.prezzo_finale\n" + "FROM utenti u, acquisti a, prodotti p\n"
						+ "WHERE u.email = a.email_utente AND a.codice_prodotto = p.codice_prodotto \n"
						+ "      AND p.tipo = \"Console\" AND p.modello = \"PS5\" \n"
						+ "      AND a.data_ora_acquisto >= DATE_ADD(NOW(), INTERVAL -1 MONTH) AND a.data_ora_acquisto <= NOW();",

				"SELECT SUM(prezzo_finale) AS incasso_giornaliero\n" + "FROM Acquisti\n"
						+ "WHERE DATE(data_ora_acquisto) = CURDATE();",

				"SELECT u.email, u.nickname, SUM(a.prezzo_finale) AS incasso_mensile\n" + "FROM utenti u, acquisti a\n"
						+ "WHERE u.email = a.email_utente  \n"
						+ "      AND a.data_ora_acquisto >= DATE_ADD(NOW(), INTERVAL -1 MONTH) AND a.data_ora_acquisto <= NOW()\n"
						+ "GROUP BY u.email, u.nickname;",

				"SELECT codice_prodotto, titolo, data_pubblicazione, n_vendite\n" + "FROM Prodotti\n"
						+ "WHERE tipo = \"Gioco\" AND n_vendite > 3;",

				"CREATE VIEW SpeseComplessive AS (\n"
						+ "    SELECT u.email, u.nickname, SUM(a.prezzo_finale) AS spesa_complessiva, SUM(a.quantita) AS tot_giochi\n"
						+ "    FROM Utenti u, Acquisti a, Prodotti p\n"
						+ "    WHERE u.email = a.email_utente AND a.codice_prodotto = p.codice_prodotto \n"
						+ "          AND p.tipo = \"Gioco\"\n" + "    GROUP BY u.email, u.nickname\n" + ");" + ""
						+ "\nSELECT email, nickname, spesa_complessiva\n" + "FROM SpeseComplessive\n"
						+ "WHERE tot_giochi >= ALL (SELECT tot_giochi\n"
						+ "                     FROM SpeseComplessive) AND spesa_complessiva > 100.00;",

				"SELECT c.*\n" + "FROM Carte c, Associare a\n"
						+ "WHERE c.n_carta = a.n_carta AND a.email_utente = \"mickymouse@gmail.com\" \n"
						+ "      AND c.n_carta NOT IN (SELECT c.n_carta\n"
						+ "                            FROM Carte c, Associare a\n"
						+ "                            WHERE c.n_carta = a.n_carta AND a.email_utente = \"giampix@gmail.com\");",

				"SELECT u.*\n" + "FROM Utenti u\n" + "WHERE NOT EXISTS (SELECT c.codice_prodotto\n"
						+ "                  FROM Prodotti c\n"
						+ "                  WHERE c.tipo = \"Console\" AND NOT EXISTS (SELECT a.email_utente\n"
						+ "                                                           FROM Acquisti a\n"
						+ "                                                           WHERE u.email = a.email_utente AND c.codice_prodotto = a.codice_prodotto));"

		};

		executeBtn.addActionListener(a -> {

			// Ogni volta che si seleziona un'operazione dal comboBox e si clicca sul pulsante "Esegui"
			// si rimuove la tabella precedentemente creata, si controlla se è un'operazione prevista o una query 
			// personalizzata, se è un operazione prevista si esegue la query selezionando da queriesString 
			// la stringa da mandare in output
			
			String query = (String) operationsBox.getSelectedItem();

			if (query.length() == 0) {
				JOptionPane.showMessageDialog(this, "Inserisci una Query!");
				return;
			}

			BorderLayout layout = (BorderLayout) crudPanel.getLayout();

			if (layout.getLayoutComponent(BorderLayout.CENTER) != null)
				crudPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

			JTable table = null;

			if (query.contains("Op."))
				table = new JTable(SQL.createResultsTable(this, stmt, queries[operationsBox.getSelectedIndex() - 1], queryString,
						query + "\n" + queriesString[operationsBox.getSelectedIndex() - 1]));
			else
				table = new JTable(SQL.createResultsTable(this, stmt, query, queryString, query));

			crudPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		});
	}

}