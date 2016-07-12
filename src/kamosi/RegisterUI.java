/*
 * RegisterUI.java
 *
 * Created on 26 √»—Ì·, 2008, 09:53 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class RegisterUI extends JFrame implements ActionListener {
    private JLabel registerLabel,userNameLabel,userPassLabel,userRePassLabel;
    private JTextField userNameField;
    private JPasswordField passField,rePassField;
    private JButton registerButton,cancelButton;
    
    private String userName;
    private String userPassword;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of RegisterUI */
    public RegisterUI() {
        initUI();
    }
    public void initUI() {
        setTitle("Personal Dictionary|Register New user");
        setBounds((int)width/3,(int)(height/3.5),300,300);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(204,255,204));
        
        registerLabel=new JLabel("Please fill the registeration form");
        registerLabel.setBounds(10,15,300,25);
        registerLabel.setFont(new Font("Comic Sans MS",Font.BOLD,15));
        registerLabel.setForeground(Color.BLUE);
        
        userNameLabel=new JLabel("Enter User Name:");
        userNameLabel.setBounds(25,45,100,25);
        
        userNameField=new JTextField();
        userNameField.setBounds(25,75,180,25);
        
        userPassLabel=new JLabel("Enter Account Password:");
        userPassLabel.setBounds(25,105,150,25);
        
        passField=new JPasswordField();
        passField.setBounds(25,135,180,25);
        
        userRePassLabel=new JLabel("Re-enter Password Again:");
        userRePassLabel.setBounds(25,165,160,25);
        
        rePassField=new JPasswordField();
        rePassField.setBounds(25,195,180,25);
        
        registerButton=new JButton("Register");
        registerButton.setBounds(35,230,100,25);
        registerButton.addActionListener(this);
        
        cancelButton=new JButton("Cancel");
        cancelButton.setBounds(160,230,100,25);
        cancelButton.addActionListener(this);
        
        getContentPane().add(registerLabel);
        getContentPane().add(userNameLabel);
        getContentPane().add(userNameField);
        getContentPane().add(userPassLabel);
        getContentPane().add(passField);
        getContentPane().add(userRePassLabel);
        getContentPane().add(rePassField);
        getContentPane().add(registerButton);
        getContentPane().add(cancelButton);
        
        
        setVisible(true);
        
    }
    
    public void registerToServer() {
        if(userNameField.getText().equals("")|passField.getText().equals("")|rePassField.getText().equals("")) {
            JOptionPane.showMessageDialog(this,"Please fill all required information!","Kamosi Program",JOptionPane.OK_OPTION);
        } else if(!passField.getText().equals(rePassField.getText())) {
            JOptionPane.showMessageDialog(this,"Please check your password again!","Kamosi Program",JOptionPane.OK_OPTION);
        } else {
            userName=userNameField.getText().toLowerCase();
            userPassword=new String(passField.getPassword());
            
            DBConnection connection=DBConnection.getDBConnection();
            Connection conn=connection.getConnection();
            Statement state = null;
            try {
                state = conn.createStatement();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            String userName="";
            ResultSet rs;
            boolean flag=false;
            try {
                rs = state.executeQuery("Select username from users where username='" + userNameField.getText().toLowerCase() + "'");
                while(rs.next()) {
                    userName=rs.getString("username");
                    if(userName.equals(userNameField.getText().toLowerCase())) {
                        flag=true;
                        break;
                    }
                    
                }
                if(flag==false) {
                    Date date=new Date();
                    int year=date.getYear()+1900;
                    int month=date.getMonth()+1;
                    int day=date.getDate();
                    
                    String dateStr=""+year+"-"+month+"-"+day;
                    state.execute("insert into users(username,password,date)values('"+userNameField.getText().toLowerCase()+"','"+passField.getText()+"','"+dateStr+"')");
                    rs=state.executeQuery("Select userid from users where username='"+userNameField.getText().toLowerCase()+"'");
                    int id=0;
                    while(rs.next())
                    {
                        id=rs.getInt("userid");
                    }
                    String word="mm",trans="mm",type="Noun",example="mm",related="mm";
                    state.execute("Insert into word(word,translation,type,example,relatedwords,userId,date) values('"+word+"','"+trans+"','"+type+"','"+example+"','"+related+"','"+id+"','"+dateStr+"')");
                    JOptionPane.showMessageDialog(this,"New user added successfully.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    setVisible(false);
                    
                    new WelcomeScreen();
                } else {
                    JOptionPane.showMessageDialog(this,"User with that name is already exist, please enter another username.","Kamosi Message",JOptionPane.OK_OPTION);
                    userNameField.setText("");
                    passField.setText("");
                    rePassField.setText("");
                    userNameField.requestFocus();
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            
            
            
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object src=e.getSource();
        
        if(src==registerButton) {
            registerToServer();
        }
        if(src==cancelButton) {
            setVisible(false);
            dispose();
            
            new WelcomeScreen();
        }
    }
    
}
