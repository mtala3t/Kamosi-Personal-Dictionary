/*
 * SearchWordPanel.java
 *
 * Created on 24 Улгиг, 2008, 03:52 е
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class SearchWordPanel extends JPanel implements ActionListener,KeyListener {
    JLabel wordLabel, typeLabel,examLabel,relatedLabel,translationLabel,catNameLabel;
    JTextField wordField,translationField;
    JTextArea examArea,relatedArea;
    JComboBox typeCombo,catNameCombo;
    JLabel searchTargetLabel;
    JTextField searchField;
    
    
    
    private int userID;
    
    
    JScrollPane examPane,relatedPane;
    
    JButton editButton,saveButton;
    JButton searchButton,deleteButton;
    private int wordID;
    
    public SearchWordPanel(int userID) {
        this.userID=userID;
        
        setLayout(null);
        setBounds(-15,75,700,400);
        
        searchTargetLabel=new JLabel("Enter your search : ");
        searchTargetLabel.setBounds(25,25,200,25);
        searchTargetLabel.setForeground(Color.BLUE);
        
        searchField=new JTextField();
        searchField.setBounds(140,25,150,25);
        searchField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        searchField.addKeyListener(this);
        
        wordLabel=new JLabel("Word : ");
        wordLabel.setBounds(40,105,50,25);
        wordLabel.setForeground(Color.BLUE);
        
        typeLabel=new JLabel("Word Type : ");
        typeLabel.setBounds(380,105,100,25);
        typeLabel.setForeground(Color.BLUE);
        
        examLabel=new JLabel("An Example : ");
        examLabel.setBounds(40,175,110,25);
        examLabel.setForeground(Color.BLUE);
        
        relatedLabel=new JLabel("Related words : ");
        relatedLabel.setBounds(40,335,100,25);
        relatedLabel.setForeground(Color.BLUE);
        
        translationLabel=new JLabel("Word Translation : ");
        translationLabel.setBounds(40,140,150,25);
        translationLabel.setForeground(Color.BLUE);
        
        catNameLabel=new JLabel("Category name: ");
        catNameLabel.setForeground(Color.BLUE);
        catNameLabel.setBounds(380,140,100,25);
        
        wordField=new JTextField();
        wordField.setBounds(160,105,200,25);
        wordField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        
        translationField=new JTextField();
        translationField.setBounds(160,140,200,25);
        translationField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        translationField.setLocale(new Locale("ar","EG"));
        
        UpdateCatCombo();
        
        typeCombo=new JComboBox();
        typeCombo.setBounds(480,105,180,25);
        typeCombo.addItem("Noun");
        typeCombo.addItem("Verb");
        typeCombo.addItem("Adjective");
        typeCombo.addItem("Adverb");
        
        examArea=new JTextArea();
        examArea.setAutoscrolls(true);
        examArea.setLineWrap(true);
        examArea.setFont(new Font("MS Comic Sans",Font.BOLD,14));
        
        relatedArea=new JTextArea();
        relatedArea.setAutoscrolls(true);
        relatedArea.setLineWrap(true);
        relatedArea.setFont(new Font("MS Comic Sans",Font.BOLD,14));
        
        examPane=new JScrollPane(examArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        examPane.setBounds(40,205,620,120);
        
        relatedPane=new JScrollPane(relatedArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        relatedPane.setBounds(40,365,620,60);
        
        editButton=new JButton("Edit");
        editButton.setBounds(290,440,120,25);
        editButton.addActionListener(this);
        
        saveButton=new JButton("Save");
        saveButton.setBounds(490,440,120,25);
        saveButton.addActionListener(this);
        
        searchButton=new JButton("Search");
        searchButton.setBounds(500,25,120,25);
        searchButton.addActionListener(this);
        
        deleteButton=new JButton("Delete");
        deleteButton.setBounds(90,440,120,25);
        deleteButton.addActionListener(this);
        
        disableControls();
        
        searchField.requestFocus();
        add(searchTargetLabel);
        add(searchField);
        add(wordLabel);
        add(wordField);
        add(translationField);
        add(examLabel);
        add(translationLabel);
        add(relatedLabel);
        add(examPane);
        add(catNameLabel);
        add(relatedPane);
        add(typeLabel);
        add(typeCombo);
        add(editButton);
        add(saveButton);
        add(searchButton);
        add(deleteButton);
        
    }
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(480,140,180,25);
        catNameCombo.addItem("Default Category");
        add(catNameCombo);
        
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
            
            catNameCombo.setEnabled(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    public void setWordID(int id) {
        wordID=id;
    }
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==deleteButton) {
            
            int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to delete that word?","Delete Confirmation",JOptionPane.YES_NO_OPTION);
            
            if(res==JOptionPane.YES_OPTION) {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement state=null;
                try {
                    
                    state=conn.createStatement();
                    state.executeUpdate("delete from word where Id='"+wordID+"'");
                    JOptionPane.showMessageDialog(this,"Word deleted successfully.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                
                
                searchField.setText("");
                searchField.requestFocus();
                wordField.setText("");
                typeCombo.setSelectedItem("Noun");
                translationField.setText("");
                examArea.setText("");
                relatedArea.setText("");
                
                searchButton.setText("Search");
                editButton.setEnabled(false);
                saveButton.setEnabled(false);
                deleteButton.setEnabled(false);
                
            } else {
                
            }
            
        }
        if(obj==editButton) {
            enableControls();
            editButton.setEnabled(false);
            searchButton.setEnabled(false);
            searchField.setEnabled(false);
            searchTargetLabel.setEnabled(false);
            
        }
        
        if(obj==saveButton) {
            int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to save changes ?","Save Confirmation",JOptionPane.YES_NO_OPTION);
            
            if(res==JOptionPane.YES_OPTION) {
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement state=null;
                try {
                    
                    state=conn.createStatement();
                    state.executeUpdate("Update word set word='"+wordField.getText()+"', type='"+typeCombo.getSelectedItem()+"', translation='"+translationField.getText()+"', example='"+examArea.getText()+"', relatedwords='"+relatedArea.getText()+"',category='"+catNameCombo.getSelectedItem()+"' where Id='"+wordID+"'");
                    saveButton.setEnabled(false);
                    JOptionPane.showMessageDialog(this,"Changes are updated successfully.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                    disableControls();
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    searchButton.setEnabled(true);
                    searchField.setEnabled(true);
                    searchTargetLabel.setEnabled(true);
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                saveButton.setEnabled(false);
                disableControls();
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                searchButton.setEnabled(true);
                searchField.setEnabled(true);
                searchTargetLabel.setEnabled(true);
            }
        }
        if(obj==searchButton) {
            if(searchField.getText().equals("")) {
                JOptionPane.showMessageDialog(this,"You did not enterd the searching word","Kamosi Message",JOptionPane.OK_OPTION);
                searchField.requestFocus();
            } else {
                
                
                if(searchButton.getText().equals("New Search")) {
                    searchField.setText("");
                    searchField.requestFocus();
                    wordField.setText("");
                    typeCombo.setSelectedItem("Noun");
                    translationField.setText("");
                    examArea.setText("");
                    relatedArea.setText("");
                    
                    searchButton.setText("Search");
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    
                    DBConnection connection=DBConnection.getDBConnection();
                    Connection conn=connection.getConnection();
                    Statement state=null;
                    
                    try {
                        state = conn.createStatement();
                        ResultSet rs=state.executeQuery("Select * from word where userId='"+userID+"' and word='"+searchField.getText()+"'");
                        int count=0;
                        while(rs.next()) {
                            
                            count++;
                            break;
                        }
                        if(count==1) {
                            displayResult(rs.getString("word"),rs.getString("translation"),rs.getString("type"),rs.getString("example"),rs.getString("relatedwords"),rs.getString("category"));
                            editButton.setEnabled(true);
                            deleteButton.setEnabled(true);
                            
                            setWordID(rs.getInt("Id"));
                            searchButton.setText("New Search");
                            
                        } else {
                            JOptionPane.showMessageDialog(this,"No result found in your dictionary. ","Kamosi Message",JOptionPane.OK_OPTION);
                            searchField.setText("");
                            searchField.requestFocus();
                        }
                        
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                }
                
            }
        }
    }
    public void disableControls() {
        //wordLabel.setEnabled(false);
        //translationLabel.setEnabled(false);
        //typeLabel.setEnabled(false);
        //examLabel.setEnabled(false);
        //relatedLabel.setEnabled(false);
        //catNameLabel.setEnabled(false);
        wordField.setEditable(false);
        translationField.setEditable(false);
        examArea.setEditable(false);
        typeCombo.setEnabled(false);
        catNameCombo.setEnabled(false);
        
        editButton.setEnabled(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        relatedArea.setEditable(false);
        
    }
    public void enableControls() {
        
        //wordLabel.setEnabled(true);
        //translationLabel.setEnabled(true);
        //typeLabel.setEnabled(true);
        //examLabel.setEnabled(true);
        //catNameLabel.setEnabled(true);
        //relatedLabel.setEnabled(true);
        wordField.setEditable(true);
        translationField.setEditable(true);
        examArea.setEditable(true);
        typeCombo.setEnabled(true);
        catNameCombo.setEnabled(true);
        editButton.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(false);
        relatedArea.setEditable(true);
        
        
    }
    
    public void displayResult(String word,String translation,String type,String example,String relatedWords,String category ) {
        wordField.setText(word);
        translationField.setText(translation);
        typeCombo.setSelectedItem(type);
        examArea.setText(example);
        relatedArea.setText(relatedWords);
        catNameCombo.setSelectedItem(category);
        
        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.drawLine(20,70,670,70);
        g2.drawLine(20,10,670,10);
        g2.drawLine(20,10,20,70);
        g2.drawLine(670,10,670,70);
        
    }
    
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()==KeyEvent.VK_ENTER) {
            if(searchField.getText().equals("")) {
                JOptionPane.showMessageDialog(this,"You did not enterd the searching word","Kamosi Message",JOptionPane.OK_OPTION);
                searchField.requestFocus();
            } else {
                
                
                if(searchButton.getText().equals("New Search")) {
                    searchField.setText("");
                    searchField.requestFocus();
                    wordField.setText("");
                    typeCombo.setSelectedItem("Noun");
                    translationField.setText("");
                    examArea.setText("");
                    relatedArea.setText("");
                    
                    searchButton.setText("Search");
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    
                    DBConnection connection=DBConnection.getDBConnection();
                    Connection conn=connection.getConnection();
                    Statement state=null;
                    
                    try {
                        state = conn.createStatement();
                        ResultSet rs=state.executeQuery("Select * from word where userId='"+userID+"' and word='"+searchField.getText()+"'");
                        int count=0;
                        while(rs.next()) {
                            
                            count++;
                            break;
                        }
                        if(count==1) {
                            displayResult(rs.getString("word"),rs.getString("translation"),rs.getString("type"),rs.getString("example"),rs.getString("relatedwords"),rs.getString("category"));
                            editButton.setEnabled(true);
                            deleteButton.setEnabled(true);
                            
                            setWordID(rs.getInt("Id"));
                            searchButton.setText("New Search");
                            
                        } else {
                            JOptionPane.showMessageDialog(this,"No result found in your dictionary. ","Kamosi Message",JOptionPane.OK_OPTION);
                            searchField.setText("");
                            searchField.requestFocus();
                        }
                        
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    
                }
                
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }   
}
