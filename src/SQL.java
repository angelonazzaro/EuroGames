import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public interface SQL {

    // In base al tipo di query inserita (una query di definizione di dati o una di manipolazione o una query di interrogazione)
    // chiama il metodo appropriato (executeUpdate (DDL-DML) - executeQuery (DQL))
    static DefaultTableModel createResultsTable(JFrame frame, Statement stmt, String query, JTextArea queryString,
            String additionalText) {

        String[] queries = query.split(";");

        DefaultTableModel model = null, tmp = null;

        for (int i = 0; i < queries.length; i++) {

            if (queries[i].charAt(0) == ' ')
                queries[i] = queries[i].substring(1, queries[i].length());

            if (isDDL(queries[i])) {

                try {
                    if (stmt.executeUpdate(queries[i]) > -1)
                        JOptionPane.showMessageDialog(frame, "Query eseguita con successo!", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(frame, "Query non eseguita!", "Errore",
                                JOptionPane.ERROR_MESSAGE);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                tmp = getResults(frame, stmt, queries[i], queryString, additionalText);

                if (tmp != null)
                    model = tmp;
            }
        }

        return model;

    }

    static boolean isDDL(String query) {
        final String keywords[] = { "create", "delete", "alter", "drop", "update", "insert" };
        String lower = query.toLowerCase();

        for (int i = 0; i < keywords.length; i++) {
            if (lower.startsWith(keywords[i]))
                return true;
        }

        return false;
    }

    // Genera il modello per la tabella contenente i risultati
    static DefaultTableModel getResults(JFrame frame, Statement stmt, String query, JTextArea queryString,
            String additionalText) {
        try (ResultSet res = stmt.executeQuery(query)) {
            ResultSetMetaData rsmd = res.getMetaData();

            int columnsCount = rsmd.getColumnCount();

            String[] columnsNames = new String[columnsCount];

            for (int i = 0; i < columnsCount; i++)
                columnsNames[i] = rsmd.getColumnName(i + 1);

            // Quante righe sono state ritornate dallo statement
            int rowsCount = res.last() ? res.getRow() : 0;

            res.beforeFirst();

            String[][] data = new String[rowsCount][columnsCount];

            int i = 0;

            // Salva il contenuto di ogni riga nella matrice di stringhe
            while (res.next()) {
                for (int j = 0; j < columnsCount; j++)
                    data[i][j] = res.getString(j + 1);
                i++;
            }

            queryString.setText("\n" + additionalText + "\n");

            DefaultTableModel model = new DefaultTableModel(data, columnsNames) {
                private static final long serialVersionUID = -6806513957377074668L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            return model;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}