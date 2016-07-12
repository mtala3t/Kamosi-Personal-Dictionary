/*
 * WelcomeScreen.java
 *
 * Created on 22 íæäíæ, 2008, 05:19 ã
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author Mohamed Talaat Saad
 */
public class WelcomeScreen extends JFrame {
    
    WelcomePanel welcomePane;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of WelcomeScreen */
    public WelcomeScreen() {
        setTitle("Welcome to Kamosi Personal Dictionary.");
        setBounds((int)(width/3.5),(int)(height/3.5),480,280);
        setResizable(false);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
       
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                Exit();
            }
        });
        welcomePane=new WelcomePanel(this);
        
        
        getContentPane().add(welcomePane);
        setVisible(true);
    }
    
    public void Exit() {
        int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to exit?","Exit Confirmation",JOptionPane.YES_NO_OPTION);
        
        if(res==JOptionPane.YES_OPTION) {
            setVisible(false);
            dispose();
            System.exit(0);
        } else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
    
    
}

class WelcomePanel extends JPanel implements ActionListener {
    JLabel tetosoftLabel,progNameLabel,versionLabel,copyrightLabel,mailLabel;
    JButton loginBut,newUserBut;
    
    JFrame f;
    public WelcomePanel(JFrame f) {
        setLayout(null);
        setBackground(new Color(204,255,204));
        setBounds(0,0,480,280);
        
        this.f=f;
        
        tetosoftLabel=new JLabel("TETOSOFT");
        tetosoftLabel.setBounds(230,30,150,25);
        tetosoftLabel.setFont(new Font("MS Comic Sans",Font.BOLD,25));
        tetosoftLabel.setForeground(Color.GRAY);
        
        progNameLabel=new JLabel("Personal Dictionary");
        progNameLabel.setBounds(170,70,300,35);
        progNameLabel.setFont(new Font("MS Comic Sans",Font.BOLD,27));
        progNameLabel.setForeground(Color.BLACK);
        
        versionLabel=new JLabel("Version 1.0");
        versionLabel.setBounds(360,115,100,25);
        versionLabel.setFont(new Font("MS Comic Sans",Font.BOLD,15));
        versionLabel.setForeground(Color.RED);
        
        copyrightLabel=new JLabel("Copyright to TETOSOFT Company - 2008");
        copyrightLabel.setBounds(135,165,300,25);
        copyrightLabel.setFont(new Font("MS Comic Sans",Font.BOLD,15));
        copyrightLabel.setForeground(Color.RED);
        
        mailLabel=new JLabel("E-mail: teto_soft@yahoo.com");
        mailLabel.setBounds(25,200,240,25);
        mailLabel.setFont(new Font("MS Comic Sans",Font.BOLD,14));
        mailLabel.setForeground(Color.BLUE);
        
        loginBut=new JButton("Login");
        loginBut.setBounds(240,200,100,25);
        loginBut.addActionListener(this);
        
        newUserBut=new JButton("New User");
        newUserBut.setBounds(350,200,100,25);
        newUserBut.addActionListener(this);
        
        add(tetosoftLabel);
        add(progNameLabel);
        add(versionLabel);
        add(copyrightLabel);
        add(mailLabel);
        add(loginBut);
        add(newUserBut);
        
    }
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==loginBut) {
            f.dispose();
            f.setVisible(false);
            
            new Login();
        }
        if(obj==newUserBut) {
            f.dispose();
            f.setVisible(false);
            
            new RegisterUI();
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.drawImage(getToolkit().getImage("Images/logo.gif"),7,7,this);
    }
}