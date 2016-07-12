/*
 * AddWordPanel.java
 *
 * Created on 22 íæäíæ, 2008, 03:14 ã
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class AddWordPanel extends JPanel implements ActionListener {
    
    JLabel wordLabel, typeLabel,examLabel,relatedLabel,translationLabel,catNameLabel;
    JTextField wordField,translationField;
    JTextArea examArea,relatedArea;
    JComboBox typeCombo,catNameCombo;
    JScrollPane examPane,relatedPane;
    
    JButton addButton,clearButton;
    
    private int userID;
    /** Creates a new instance of AddWordPanel */
    public AddWordPanel(int userID) {
        this.userID=userID;
        
        setLayout(null);
        
        wordLabel=new JLabel("Word : ");
        wordLabel.setBounds(40,60,50,25);
        wordLabel.setForeground(Color.BLUE);
        
        typeLabel=new JLabel("Word Type : ");
        typeLabel.setBounds(365,60,100,25);
        typeLabel.setForeground(Color.BLUE);
        
        catNameLabel=new JLabel("Category Name : ");
        catNameLabel.setBounds(365,95,100,25);
        catNameLabel.setForeground(Color.BLUE);
        
        examLabel=new JLabel("An Example : ");
        examLabel.setBounds(40,130,110,25);
        examLabel.setForeground(Color.BLUE);
        
        relatedLabel=new JLabel("Related words : ");
        relatedLabel.setBounds(40,290,100,25);
        relatedLabel.setForeground(Color.BLUE);
        
        translationLabel=new JLabel("Word Translation : ");
        translationLabel.setBounds(40,95,150,25);
        translationLabel.setForeground(Color.BLUE);
        
        wordField=new JTextField();
        wordField.setBounds(150,60,200,25);
        wordField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        
        translationField=new JTextField();
        translationField.setBounds(150,95,200,25);
        translationField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        translationField.setLocale(new Locale("ar","EG"));
        
        typeCombo=new JComboBox();
        typeCombo.setBounds(460,60,200,25);
        typeCombo.addItem("Noun");
        typeCombo.addItem("Verb");
        typeCombo.addItem("Adjective");
        typeCombo.addItem("Adverb");
        
        
        
        UpdateCatCombo();
        
        examArea=new JTextArea();
        examArea.setAutoscrolls(true);
        examArea.setLineWrap(true);
        examArea.setFont(new Font("MS Comic Sans",Font.BOLD,14));
        
        relatedArea=new JTextArea();
        relatedArea.setAutoscrolls(true);
        relatedArea.setLineWrap(true);
        relatedArea.setFont(new Font("MS Comic Sans",Font.BOLD,14));
        
        examPane=new JScrollPane(examArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        examPane.setBounds(40,160,620,120);
        
        relatedPane=new JScrollPane(relatedArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        relatedPane.setBounds(40,320,620,60);
        
        addButton=new JButton("Add Word");
        addButton.setBounds(170,405,120,25);
        addButton.addActionListener(this);
        
        clearButton=new JButton("Clear Fields");
        clearButton.setBounds(420,405,120,25);
        clearButton.addActionListener(this);
        
        wordField.requestFocus();
        
        add(wordLabel);
        add(wordField);
        add(translationField);
        add(examLabel);
        add(translationLabel);
        add(relatedLabel);
        add(examPane);
        add(relatedPane);
        add(typeLabel);
        add(typeCombo);

        add(catNameLabel);
        add(addButton);
        add(clearButton);
        
    }
    
    public void UpdateCatCombo() {
        if(catNameCombo!=null) 
        {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(460,95,200,25);
        catNameCombo.addItem("Default Category");
        add(catNameCombo);
        updateUI();
        DBConnection connection=DBConnection.getDBConnection();
        Connection conn=connection.getConnection();
        Statement stat = null;
        ResultSet rs;
        try {
            stat=conn.createStatement();
            rs = stat.executeQuery("Select cat_name,cat_class from category where userid='" + userID + "'");
            String catName="";
            String catType="";
            
            while(rs.next()) {
                catName=rs.getString("cat_name");
                catType=rs.getString("cat_class");
                
                catNameCombo.addItem(catName+" <"+catType+">");
                
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==addButton) {
            if(wordField.getText().equals("")||examArea.getText().equals("")||translationField.getText().equals("")) {
                JOptionPane.showMessageDialog(this,"You must enter all required fields.","Kamosi Message",JOptionPane.OK_OPTION);
            } else if(wordField.getText().contains(" ")) {
                JOptionPane.showMessageDialog(this,"You must enter single word without spaces.","Kamosi Message",JOptionPane.OK_OPTION);
                
            } else {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat;
                boolean added;
                
                Date date=new Date();
                int year=date.getYear()+1900;
                int month=date.getMonth()+1;
                int day=date.getDate();
                String dayStr = ""+day,monthStr = ""+month;
                if(day<10)
                     dayStr="0"+day;
                
                if(month<10)
                     monthStr="0"+month;
                
                String dateStr=""+year+"-"+monthStr+"-"+dayStr;
                System.out.println(dateStr);
                try {
                    
                    
                    stat = conn.createStatement();
                    ResultSet rs=stat.executeQuery("Select word,category from word where userId='"+userID+"'");
                    String word="",cat="";
                    boolean flag=false;
                    while(rs.next()) {
                        word=rs.getString("word");
                        cat=rs.getString("category");
                        if(word.equals(wordField.getText())) {
                            JOptionPane.showMessageDialog(this,"This word is already exist in your dictionary.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                            flag=true;
                            break;
                        }
                    }
                    if(flag==false) {
                        
                        added=stat.execute("Insert into word(word,category,translation,type,example,relatedwords,userId,date) values('"+wordField.getText()+"','"+catNameCombo.getSelectedItem()+"','"+translationField.getText()+"','"+typeCombo.getSelectedItem()+"','"+examArea.getText()+"','"+relatedArea.getText()+"','"+userID+"','"+dateStr+"')");
                        
                        JOptionPane.showMessageDialog(this,"Word added successfully.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                    clearFields();
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                
            }
        }
        if(obj==clearButton) {
            clearFields();
        }
        
    }
    
    public void clearFields() {
        wordField.setText("");
        examArea.setText("");
        relatedArea.setText("");
        translationField.setText("");
        wordField.requestFocus();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("MS Comic Sans",Font.BOLD,25));
        g2.drawString("Add New Word",250,30);
        g2.setColor(Color.BLACK);
        g2.drawRect(25,45,650,430);
    }
}
