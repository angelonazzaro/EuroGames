import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class SelezionaFrame extends JFrame {

    private static final long serialVersionUID = 721919363702193185L;
    private static SelezionaFrame frame = null;
    private static QueryFrame queryFrame = null;
    private static IMCVFrame imcvFrame = null;
    private static Connection con;
    private static Statement stmt;
    private final String username = "root", password = "francesco.1", url = "jdbc:mysql://localhost:3306/euroGames";

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new SelezionaFrame();
                    Utils.spawnCenter(frame);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SelezionaFrame() {

    	// Apre la connessione al DB
    	
        openConnection();

        setTitle("Seleziona opzione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 300, 150);
        getContentPane().setLayout(new BorderLayout(0, 0));

        
        // Quando la finestra principale si chiude, si chiude anche la connessione al DB
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.WEST);

        JPanel panel_2 = new JPanel();
        getContentPane().add(panel_2, BorderLayout.EAST);

        JPanel panel_3 = new JPanel();
        getContentPane().add(panel_3, BorderLayout.SOUTH);

        JPanel panel_4 = new JPanel();
        getContentPane().add(panel_4, BorderLayout.CENTER);
        panel_4.setLayout(new BorderLayout(0, 0));

        JPanel panel_5 = new JPanel();
        panel_4.add(panel_5, BorderLayout.NORTH);
        panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnNewButton = new JButton("I-M-C-V");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (imcvFrame == null) {

                    imcvFrame = new IMCVFrame(stmt);
                    imcvFrame.setVisible(true);
                } else {

                    imcvFrame.toFront();
                    imcvFrame.setVisible(true);
                }
            }
        });

        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel_5.add(btnNewButton);

        JPanel panel_6 = new JPanel();
        panel_4.add(panel_6, BorderLayout.SOUTH);
        panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnNewButton_1 = new JButton("Query");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (queryFrame == null) {

                    queryFrame = new QueryFrame(stmt);
                    queryFrame.setVisible(true);
                } else {

                    queryFrame.toFront();
                    queryFrame.setVisible(true);
                }

            }
        });

        btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel_6.add(btnNewButton_1);
    }

    private void openConnection() {
    	// Inizia la connessione al DB e crea un oggetto Statement
    	// in cui il cursore si può muovere sia avanti che indietro. Il result set generato
    	// sarà di sola lettura.
        try {

            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            con.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(-1);
        }
    }
}