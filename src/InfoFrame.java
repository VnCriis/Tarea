import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InfoFrame extends JFrame {
    private Connection connection;
    private JPanel scrollPane;
    private JTextArea bioTextArea;

    public InfoFrame(String usuario) {
        connectToDatabase();
        initComponents(usuario);
    }

    private void connectToDatabase() {
        // Conectarse a la base de datos (asegúrate de cambiar la URL, usuario y contraseña según tu configuración)
        String url = "jdbc:mysql://localhost:3306/login";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initComponents(String usuario) {
        setTitle("Información");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));

        JTextArea bioTextArea = new JTextArea();
        bioTextArea.setEditable(false);

        try {
            String query = "SELECT biografia FROM informacion WHERE usuario = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, usuario);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String biografia = resultSet.getString("biografia");
                    bioTextArea.setText(biografia);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró información para el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(bioTextArea);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }
}
