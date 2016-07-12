package kamosi;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.*;
import java.util.*;
import java.util.EventListener.*;
import java.text.*;

public class ChangePassword extends JFrame implements ActionListener
{
    private JPanel pChange = new JPanel();
    private JLabel lbUser, lbPass, lbNew;
    public JTextField txtUser;
    public JPasswordField txtPass, txtNew;
    private JButton btnOk, btnCancel;
    
    public String userName;			//For Holding the Usename.
    
    //Constructor of Class.
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    public ChangePassword(String user) {
        //super(Title, Resizable, Closable, Maximizable, Iconifiable)
        setTitle("Change Password");
        setBounds((int)(width/2.6),(int)height/3,300,210);
        
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        //Setting the Form's Labels.
        
        lbUser = new JLabel("Username:");
        lbUser.setForeground(Color.black);
        lbUser.setBounds(20, 20, 115, 25);
        lbPass = new JLabel("Old Password:");
        lbPass.setForeground(Color.black);
        lbPass.setBounds(20, 55, 115, 25);
        lbNew = new JLabel("New Password:");
        lbNew.setForeground(Color.black);
        lbNew.setBounds(20, 90, 115, 25);
        
        //Setting the Form's TextField & PasswordField.
        
        txtUser = new JTextField();
        txtUser.setEnabled(false);
        txtUser.setBounds(120, 20, 150, 25);
        txtUser.setText(user);
        txtPass = new JPasswordField();
        txtPass.setBounds(120, 55, 150, 25);
        txtNew = new JPasswordField();
        txtNew.setBounds(120, 90, 150, 25);
        
        //Setting the Form's Buttons.
        
        btnOk = new JButton("OK");
        btnOk.setBounds(20, 130, 100, 25);
        btnOk.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(165, 130, 100, 25);
        btnCancel.addActionListener(this);
        
        //Setting Panel's Layout.
        
        pChange.setLayout(null);
        pChange.setBackground(new Color(204,255,204));
        //Adding All the Controls in Panel.
        
        pChange.add(lbUser);
        pChange.add(lbPass);
        pChange.add(lbNew);
        pChange.add(txtUser);
        pChange.add(txtPass);
        pChange.add(txtNew);
        pChange.add(btnOk);
        pChange.add(btnCancel);
        
        //Adding Panel to the Form.
        
        getContentPane().add(pChange);
        
        userName = user;	//Getting Username.
        
        setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent ae) {
        Object obj = ae.getSource();
        
        if (obj == btnOk) {		//If OK Button Pressed.
            
            String oldPass = new String(txtPass.getPassword());
            String newPass = new String(txtNew.getPassword());
            
            if (oldPass.equals("")) {
                JOptionPane.showMessageDialog(this, "Old Password not Provided.");
                txtPass.requestFocus();
            } else if (newPass.equals("")) {
                JOptionPane.showMessageDialog(this, "New Password not Provided.");
                txtNew.requestFocus();
            } else {
                String pass;			//To Hold the Password.
                boolean verify = false;		//To Confirm Logon.
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                
                try {	//SELECT Query to Retrieved the Record.
                    Statement st=conn.createStatement();
                    String query = "SELECT * FROM users WHERE username = '" + userName + "'";
                    
                    ResultSet rs = st.executeQuery(query);		//Executing the Query.
                    rs.next();					//Moving Towards the Record.
                    pass = rs.getString("Password");		//Storing Password.
                    
                    if (oldPass.equals(pass)) {		//If Found then.
                        verify = true;
                        String q = "UPDATE users SET password  = '" + newPass +
                                "' WHERE username = '" + userName + "'";
                        
                        int result = st.executeUpdate(q);
                        if (result == 1) {
                            JOptionPane.showMessageDialog(this,
                                    "Your Password has been Changed.\nRemember Your new Password");
                            setVisible(false);		//Hide the Form.
                            dispose();            		//Free the System Resources.
                        } else {
                            JOptionPane.showMessageDialog(this, "Problem in Changing Password.");
                            txtPass.setText("");
                            txtNew.setText("");
                            txtPass.requestFocus();
                        }
                    } else {
                        verify = false;
                        txtPass.setText("");
                        txtNew.setText("");
                        txtPass.requestFocus();
                        JOptionPane.showMessageDialog(this, "Incorrect Old Password.");
                    }
                } catch (SQLException sqlex) { }
            }
            
        } else if (obj == btnCancel) {		//If Cancel Button Pressed Unload the From.
            
            setVisible(false);
            dispose();
            
        }
        
    }
    
}