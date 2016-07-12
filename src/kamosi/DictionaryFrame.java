/*
 * DictionaryPanel.java
 *
 * Created on October 3, 2008, 5:55 PM
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed
 */
public class DictionaryFrame extends JFrame implements ActionListener,KeyListener {
    
    private JLabel sourceLabel;
    private JTextField wordField;
    private JButton translateButton;
    private JTextArea translationArea;
    JScrollPane examPane;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of DictionaryPanel */
    public DictionaryFrame() {
        
        
        
        setTitle("Embeded Dictionary");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((int)(width/5.2),(int)(height/4.7),800,370);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(204,255,204));
        
        
        sourceLabel=new JLabel("Source Text: ");
        sourceLabel.setBounds(20,20,150,25);
        
        wordField=new JTextField();
        wordField.setBounds(110,20,200,25);
        wordField.addKeyListener(this);
        
        
        translationArea=new JTextArea();
        translationArea.setBounds(20,60,460,250);
        translationArea.setFont(new Font("Arial",Font.BOLD,13));
        translationArea.setForeground(Color.RED);
        //translationArea.setLineWrap(true);
        translationArea.setEditable(false);
        translationArea.setLineWrap(true);
        examPane=new JScrollPane(translationArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        examPane.setBounds(20,60,760,250);
        
        translateButton=new JButton("Translate");
        translateButton.setBounds(360,20,120,25);
        translateButton.addActionListener(this);
        add(sourceLabel);
        add(wordField);
        add(examPane);
        add(translateButton);
        
        wordField.requestFocus();
        setVisible(true);
        
    }
    public static void main(String args[]) {
        new DictionaryFrame();
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==translateButton) {
            
            String word=wordField.getText();
            
            if(!word.equals("")) {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat=null;
                ResultSet rs=null;
                try {
                    
                    stat=conn.createStatement();
                    rs=stat.executeQuery("Select * from words where word='"+word+"'");
                    String result;
                    boolean flag=false;
                    String example="";
                    while(rs.next())
                        
                    {
                        if(rs.getString("other_type").equals("")) {
                            
                            example=rs.getString("examples");
                            if(!example.equals("")) {
                                example=example.substring(1);
                                if(example.contains("#")) {
                                    example="\n  Ex: "+example.substring(0,example.indexOf("#"))+"\n"+"  Ex: "+example.substring(example.indexOf("#")+1);
                                } else
                                    example="  Ex: "+example;
                            }
                            result="  "+rs.getString("type")+" "+rs.getString("word_forms")+" "+rs.getString("definition")+"\n"+"   "+example;
                        } else {
                            example=rs.getString("examples");
                            if(!example.equals("")) {
                                example=example.substring(1);
                                if(example.contains("#")) {
                                    example="\n  Ex: "+example.substring(0,example.indexOf("#"))+"\n"+"  Ex: "+example.substring(example.indexOf("#")+1);
                                } else
                                    example="  Ex: "+example;
                            }
                            result="  "+rs.getString("type")+" "+rs.getString("word_forms")+" "+rs.getString("definition")+"\n"+"  "+rs.getString("other_type")+" "+rs.getString("other_forms")+" "+rs.getString("other_definition")+"\n"+example;
                        }
                        
                        translationArea.setText(result);
                        flag=true;
                    }
                    
                    if(flag==false) {
                        translationArea.setText("No results found.");
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else
                JOptionPane.showMessageDialog(this,"Please write a word to search.");
            
            wordField.requestFocus();
        }
    }
    
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()==KeyEvent.VK_ENTER) {
            
             String word=wordField.getText();
            
            if(!word.equals("")) {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat=null;
                ResultSet rs=null;
                try {
                    
                    stat=conn.createStatement();
                    rs=stat.executeQuery("Select * from words where word='"+word+"'");
                    String result;
                    boolean flag=false;
                    String example="";
                    while(rs.next())
                        
                    {
                        if(rs.getString("other_type").equals("")) {
                            
                            example=rs.getString("examples");
                            if(!example.equals("")) {
                                example=example.substring(1);
                                if(example.contains("#")) {
                                    example="\n  Ex: "+example.substring(0,example.indexOf("#"))+"\n"+"  Ex: "+example.substring(example.indexOf("#")+1);
                                } else
                                    example="  Ex: "+example;
                            }
                            result="  "+rs.getString("type")+" "+rs.getString("word_forms")+" "+rs.getString("definition")+"\n"+"   "+example;
                        } else {
                            example=rs.getString("examples");
                            if(!example.equals("")) {
                                example=example.substring(1);
                                if(example.contains("#")) {
                                    example="\n  Ex: "+example.substring(0,example.indexOf("#"))+"\n"+"  Ex: "+example.substring(example.indexOf("#")+1);
                                } else
                                    example="  Ex: "+example;
                            }
                            result="  "+rs.getString("type")+" "+rs.getString("word_forms")+" "+rs.getString("definition")+"\n"+"  "+rs.getString("other_type")+" "+rs.getString("other_forms")+" "+rs.getString("other_definition")+"\n"+example;
                        }
                        
                        translationArea.setText(result);
                        flag=true;
                    }
                    
                    if(flag==false) {
                        translationArea.setText("No results found.");
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            } else
                JOptionPane.showMessageDialog(this,"Please write a word to search.");
            
             wordField.requestFocus();
        }
    }
    
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
