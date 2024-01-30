import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class LoginFrame extends JFrame {
    private Connection connection;
    private JPanel LoginFrame;
    private JLabel userLabel;
    private JTextField userField;
    private JLabel passLabel;
    private JTextField passField;
    private JButton loginButton;

    public LoginFrame() {
        connectToDatabase();
        initComponents();
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/login";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        setPreferredSize(new Dimension(300, 150));

        JLabel userLabel = new JLabel("Usuario:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Contraseña:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Iniciar Sesión");

        loginButton.addActionListener(e -> {
            String usuario = userField.getText();
            String contraseña = new String(passField.getPassword());

            if (validateLogin(usuario, contraseña)) {
                InfoFrame infoFrame = new InfoFrame(usuario);
                infoFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);

        pack();
        setLocationRelativeTo(null);
    }

    private boolean validateLogin(String usuario, String contraseña) {
        try {
            String query = "SELECT * FROM informacion WHERE usuario = ? AND contraseña = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, usuario);
                preparedStatement.setString(2, contraseña);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
