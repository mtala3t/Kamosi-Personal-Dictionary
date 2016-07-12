/*
 * EditWord.java
 *
 * Created on September 29, 2008, 7:59 AM
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
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class EditWordFrame extends JFrame implements ActionListener {
    JLabel wordLabel, typeLabel,examLabel,relatedLabel,translationLabel,catNameLabel;
    JTextField wordField,translationField;
    JTextArea examArea,relatedArea;
    JComboBox typeCombo,catNameCombo;
    JButton editButton,saveButton,deleteButton;
    JScrollPane examPane,relatedPane;
    
    private int wordID;
    private int userID;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of EditWord */
    public EditWordFrame(int userID,int wordID) {
        
        this.userID=userID;
        this.wordID=wordID;
        
        setTitle("Selected Word In details");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((int)(width/4.2),(int)(height/4.7),700,470);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(204,255,204));
        
        
        
        wordLabel=new JLabel("Word : ");
        wordLabel.setBounds(40,50,50,25);
        wordLabel.setForeground(Color.BLUE);
        
        typeLabel=new JLabel("Word Type : ");
        typeLabel.setBounds(380,50,100,25);
        typeLabel.setForeground(Color.BLUE);
        
        examLabel=new JLabel("An Example : ");
        examLabel.setBounds(40,120,110,25);
        examLabel.setForeground(Color.BLUE);
        
        relatedLabel=new JLabel("Related words : ");
        relatedLabel.setBounds(40,280,100,25);
        relatedLabel.setForeground(Color.BLUE);
        
        translationLabel=new JLabel("Word Translation : ");
        translationLabel.setBounds(40,85,150,25);
        translationLabel.setForeground(Color.BLUE);
        
        catNameLabel=new JLabel("Category name: ");
        catNameLabel.setForeground(Color.BLUE);
        catNameLabel.setBounds(380,85,100,25);
        
        wordField=new JTextField();
        wordField.setBounds(160,50,200,25);
        wordField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        
        translationField=new JTextField();
        translationField.setBounds(160,85,200,25);
        translationField.setFont(new Font("MS Comic Sans",Font.PLAIN,14));
        translationField.setLocale(new Locale("ar","EG"));
        
        //UpdateCatCombo();
        
        typeCombo=new JComboBox();
        typeCombo.setBounds(480,50,180,25);
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
        examPane.setBounds(40,150,620,120);
        
        relatedPane=new JScrollPane(relatedArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        relatedPane.setBounds(40,310,620,60);
        
        editButton=new JButton("Edit");
        editButton.setBounds(290,385,120,25);
        editButton.addActionListener(this);
        
        saveButton=new JButton("Save");
        saveButton.setBounds(490,385,120,25);
        saveButton.addActionListener(this);
        
        
        deleteButton=new JButton("Delete");
        deleteButton.setBounds(90,385,120,25);
        deleteButton.addActionListener(this);
        
        disableControls();
        
        
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
        add(deleteButton);
        
        
        DisplayWordDetails();
        
        setVisible(true);
    }
    
    
    public static void main(String args[]) {
//        new EditWordFrame();
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
                
                
                dispose();
                setVisible(false);
                
            } else {
                
            }
            
        }
        if(obj==editButton) {
            enableControls();
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            
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
                    
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                saveButton.setEnabled(false);
                disableControls();
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                
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
        
        saveButton.setEnabled(false);
        
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
        saveButton.setEnabled(true);
        relatedArea.setEditable(true);
                
        
        
    }
    public void DisplayWordDetails() {
        DBConnection connection=DBConnection.getDBConnection();
        Connection conn=connection.getConnection();
        Statement state=null;
        
        try {
            state = conn.createStatement();
            ResultSet rs=state.executeQuery("Select * from word where id='"+wordID+"'");
            int count=0;
            while(rs.next()) {
                
                count++;
                break;
            }
            if(count==1) {
                displayResult(rs.getString("word"),rs.getString("translation"),rs.getString("type"),rs.getString("example"),rs.getString("relatedwords"),rs.getString("category"));
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                
                
            } else {
                JOptionPane.showMessageDialog(this,"No result found in your dictionary. ","Kamosi Message",JOptionPane.OK_OPTION);
                
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void displayResult(String word,String translation,String type,String example,String relatedWords,String category ) {
        wordField.setText(word);
        translationField.setText(translation);
        typeCombo.setSelectedItem(type);
        examArea.setText(example);
        relatedArea.setText(relatedWords);
        catNameCombo.setSelectedItem(category);
        
        
    }
    
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(480,85,180,25);
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
            
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
}
