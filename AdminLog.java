import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.SwingUtilities;

public class AdminLog extends JFrame {

    private JPanel AdLog;
    private JTextField AdminUser;
    private JButton btn_Login;
    private JPasswordField AdminPass;
    private JButton BackButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminLog frame = new AdminLog();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AdminLog() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1026, 645);
        AdLog = new JPanel();
        AdLog.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(AdLog);
        setLocationRelativeTo(null);
        AdLog.setLayout(null);

        AdminUser = new JTextField();
        AdminUser.setBounds(64, 318, 257, 43);
        AdLog.add(AdminUser);

        JCheckBox showHideButton = new JCheckBox("Show Password");
        showHideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showHideButton.isSelected()) {
                    AdminPass.setEchoChar((char) 0);
                } else {
                    AdminPass.setEchoChar('•');
                }
            }
        });
        showHideButton.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        showHideButton.setBounds(207, 394, 114, 23);
        AdLog.add(showHideButton);

        AdminPass = new JPasswordField();
        AdminPass.setBounds(64, 424, 257, 43);
        AdLog.add(AdminPass);

        btn_Login = new JButton("Login");
        btn_Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = AdminUser.getText();
                String password = new String(AdminPass.getPassword());

                if (username.equals("NCProjectsAdmin") && password.equals("Admin2023")) {
                    AdminUser.setText("");
                    AdminPass.setText("");
                    JOptionPane.showMessageDialog(null, "Login Successfully!");
                    // Perform action on successful login.
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username and/or Password!");
                }
            }
        });
        btn_Login.setBounds(142, 501, 139, 40);
        AdLog.add(btn_Login);

        BackButton = new JButton("Back");
        BackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
                AdLog.setVisible(false);
            }
        });
        BackButton.setBounds(142, 556, 139, 40);
        AdLog.add(BackButton);

        JLabel lbl_AdminLogBG = new JLabel("");
        lbl_AdminLogBG.setIcon(new ImageIcon("path/to/your/background/image.png"));
        lbl_AdminLogBG.setBounds(0, 0, 1026, 617);
        AdLog.add(lbl_AdminLogBG);
    }
}
