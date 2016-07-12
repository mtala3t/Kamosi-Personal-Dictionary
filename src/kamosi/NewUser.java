package kamosi;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Date;

public class NewUser extends JFrame implements ActionListener {
    
    private JPanel pNew = new JPanel();
    private JLabel lbUser, lbPass;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnOk, btnCancel;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    //Constructor of Class.
    
    public NewUser() {
        
        //super(title, resizable, closable, maximizable, iconifiable)
        setTitle("Create New User");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setBounds((int)(width/2.6),(int)height/3,280,175);
        setResizable(false);
        //Setting the Form's Labels.
        
        lbUser = new JLabel("Username:");
        lbUser.setForeground(Color.black);
        lbUser.setBounds(20, 20, 100, 25);
        lbPass = new JLabel("Password:");
        lbPass.setForeground(Color.black);
        lbPass.setBounds(20, 55, 100, 25);
        
        //Setting the Form's TextField & PasswordField.
        
        txtUser = new JTextField();
        txtUser.setBounds(100, 20, 150, 25);
        txtPass = new JPasswordField();
        txtPass.setBounds(100, 55, 150, 25);
        
        //Setting the Form's Buttons.
        
        btnOk = new JButton("OK");
        btnOk.setBounds(20, 100, 100, 25);
        btnOk.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(150, 100, 100, 25);
        btnCancel.addActionListener(this);
        
        //Setting Panel's Layout.
        
        pNew.setLayout(null);
        pNew.setBackground(new Color(204,255,204));
        //Adding All the Controls in Panel.
        
        pNew.add(lbUser);
        pNew.add(lbPass);
        pNew.add(txtUser);
        pNew.add(txtPass);
        pNew.add(btnOk);
        pNew.add(btnCancel);
        
        //Adding Panel to the Form.
        
        getContentPane().add(pNew);
        
        
        
        setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent ae) {
        
        Object obj = ae.getSource();
        
        if (obj == btnOk) {		//If OK Button Pressed.
            
            String password = new String(txtPass.getPassword());
            
            if (txtUser.getText().equals("")) {
                txtUser.requestFocus();
                JOptionPane.showMessageDialog(this, "Username not Provided.");
            } else if (password.equals("")) {
                txtPass.requestFocus();
                JOptionPane.showMessageDialog(this, "Password not Provided.");
            } else {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Date date=new Date();
                int year=date.getYear()+1900;
                int month=date.getMonth()+1;
                int day=date.getDate();
                
                String dateStr=""+year+"-"+month+"-"+day;
                try {
                    Statement st=conn.createStatement();
                    //INSERT Query to Add Book Record in Table.
                    String q = "INSERT INTO users (username, password,date) " +
                            "VALUES ('" + txtUser.getText() + "', '" + password + "','"+dateStr+"')";
                    
                    int result = st.executeUpdate(q);	//Running Query.
                    if (result == 1) {			//If Query Successful.
                        
                    ResultSet rs=st.executeQuery("Select userid from users where username='"+txtUser.getText().toLowerCase()+"'");
                    int id=0;
                    while(rs.next())
                    {
                        id=rs.getInt("userid");
                    }
                    String word="mm",trans="mm",type="Noun",example="mm",related="mm";
                    st.execute("Insert into word(word,translation,type,example,relatedwords,userId,date) values('"+word+"','"+trans+"','"+type+"','"+example+"','"+related+"','"+id+"','"+dateStr+"')");
                        JOptionPane.showMessageDialog(this, "New User has been Created.");
                        setVisible(false);
                        dispose();
                    } else {					//If Query Failed.
                        JOptionPane.showMessageDialog(this, "Problem while Creating the User.");
                        txtUser.setText("");
                        txtPass.setText("");
                        txtUser.requestFocus();
                    }
                } catch (SQLException sqlex) { }
            }
            
        }
        
        if (obj == btnCancel) {		//If Cancel Button Pressed Unload the From.
            
            setVisible(false);
            dispose();
            
        }
        
    }
    
}