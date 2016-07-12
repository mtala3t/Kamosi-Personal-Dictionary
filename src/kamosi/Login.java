package kamosi;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/*
 * Login.java
 *
 * Created on 01 „«—”, 2007, 04:59 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohammed
 */
public class Login extends JFrame implements ActionListener,KeyListener {
    private JPanel loginPanel;
    private JLabel userNameLabel,passLabel;
    private JTextField userNameTxt;
    private JPasswordField passTxt;
    private JButton okButton,cancelButton;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of Login */
    public Login() {
        
        super("Login Screen!");
        setBounds((int)width/3,(int)height/3,275,160);
        
        setResizable(false);
        setIconImage(getToolkit().getImage("Images/Keys.gif"));
        addWindowListener(new WindowAdapter() {
            
            public void windowClosing(WindowEvent event) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        
        loginPanel=new JPanel();
        userNameLabel=new JLabel("User Name :");
        userNameLabel.setBounds(20, 15, 75, 25);
        passLabel=new JLabel("Password :");
        passLabel.setBounds(20, 50, 75, 25);
        userNameTxt=new JTextField();
        userNameTxt.setBounds(100, 15, 150, 25);
        passTxt=new JPasswordField();
        passTxt.setBounds(100, 50, 150, 25);
        passTxt.addKeyListener(this);
        okButton=new JButton("OK");
        okButton.setBounds(20, 90, 100, 25);
        okButton.addActionListener(this);
        cancelButton=new JButton("Cancel");
        cancelButton.setBounds(150, 90, 100, 25);
        cancelButton.addActionListener(this);
        
        loginPanel.setLayout(null);
        loginPanel.add(userNameLabel);
        loginPanel.add(passLabel);
        loginPanel.add(userNameTxt);
        loginPanel.add(passTxt);
        loginPanel.add(okButton);
        loginPanel.add(cancelButton);
        loginPanel.setBackground(new Color(204,255,204));
        getContentPane().add(loginPanel);
        
        setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==okButton) {
            if(userNameTxt.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Please, Enter your User Name!");
                userNameTxt.requestFocus();
            } else if(passTxt.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Please, Enter your Pasword!");
                passTxt.requestFocus();
            } else {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat = null;
                try {
                    
                    stat=conn.createStatement();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    String userName="";
                    int userID=0;
                    ResultSet rs=stat.executeQuery("Select userId,username,password from users where username='"+userNameTxt.getText().toLowerCase()+"'"+"and password='"+passTxt.getText()+"'");
                    while(rs.next()) {
                        userID=rs.getInt("userId");
                        userName=rs.getString("username");
                    }
                    
                    if(!userName.equals("")) {
                        new KamosiUI(userName,userID);
                        setVisible(false);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,"Incorrect User Information!");
                        
                        userNameTxt.setText("");
                        passTxt.setText("");
                        userNameTxt.requestFocus();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            }
            
        }
        
        if(obj==cancelButton) {
            setVisible(false);
            dispose();
            new WelcomeScreen();
        }
    }
    
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()==KeyEvent.VK_ENTER) {
            if(userNameTxt.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Please, Enter your User Name!");
                userNameTxt.requestFocus();
            } else if(passTxt.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Please, Enter your Pasword!");
                passTxt.requestFocus();
            } else {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat = null;
                try {
                    
                    stat=conn.createStatement();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    String userName="";
                    int userID=0;
                    ResultSet rs=stat.executeQuery("Select userId,username,password from users where username='"+userNameTxt.getText().toLowerCase()+"'"+"and password='"+passTxt.getText()+"'");
                    while(rs.next()) {
                        userID=rs.getInt("userId");
                        userName=rs.getString("username");
                    }
                    
                    if(!userName.equals("")) {
                        new KamosiUI(userName,userID);
                        setVisible(false);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,"Incorrect User Information!");
                        
                        userNameTxt.setText("");
                        passTxt.setText("");
                        userNameTxt.requestFocus();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            }
            
        }
    }
    
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
