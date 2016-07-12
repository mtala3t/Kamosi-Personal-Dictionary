/*
 * DeleteWordsUI.java
 *
 * Created on 12 ”» „»—, 2008, 02:07 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class DeleteWordsUI extends JFrame implements ActionListener {
    
    private JLabel wordTypeLabel,catNameLabel;
    private JComboBox wordTypeCombo,catNameCombo;
    private JButton btnDelete, btnCancel;
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    private int userID;
    /** Creates a new instance of DeleteCategoryUI */
    public DeleteWordsUI(int userID) {
        this.userID=userID;
        setTitle("Delete Words");
        setBounds((int)(width/3),(int)(height/3.2),400,180);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        getContentPane().setBackground(new Color(204,255,204));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        
        wordTypeLabel=new JLabel("Select the type of words:");
        wordTypeLabel.setForeground(Color.BLUE);
        wordTypeLabel.setBounds(10,10,170,25);
        
        catNameLabel=new JLabel("Select the category name:");
        catNameLabel.setForeground(Color.BLUE);
        catNameLabel.setBounds(10,45,170,25);
        
        UpdateCatCombo();
        
        
        wordTypeCombo=new JComboBox();
        wordTypeCombo.setBounds(180,10,200,25);
        wordTypeCombo.addItem("Nouns");
        wordTypeCombo.addItem("Verbs");
        wordTypeCombo.addItem("Adjectives");
        wordTypeCombo.addItem("Adverbs");
        wordTypeCombo.addItem("All Words");
        
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(70, 95, 100, 25);
        btnDelete.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(210, 95, 100, 25);
        btnCancel.addActionListener(this);
        
        getContentPane().add(wordTypeLabel);
        getContentPane().add(catNameLabel);
        getContentPane().add(wordTypeCombo);
        getContentPane().add(btnCancel);
        getContentPane().add(btnDelete);
        
        setVisible(true);
        
    }
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(180,45,200,25);
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
            catNameCombo.addItem("All Categories");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==btnCancel) {
            setVisible(false);
            dispose();
        }
        if(obj==btnDelete) {
            int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to delete these words?","Delete Confirmation",JOptionPane.YES_NO_OPTION);
            
            if(res==JOptionPane.YES_OPTION) {
                
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement state = null;
                try {
                    state=conn.createStatement();
                                            String ww="mm";
                    if(wordTypeCombo.getSelectedItem().equals("All Words")&&catNameCombo.getSelectedItem().equals("All Categories")) {

                        state.executeUpdate("Delete from word where userId='"+userID+"' and word <>'"+ww+"'");
                        
                    } else if(wordTypeCombo.getSelectedItem().equals("All Words")) {
                        state.executeUpdate("Delete from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' and word <>'"+ww+"'");
                    } else if(catNameCombo.getSelectedItem().equals("All Categories")) {
                        String type=(""+wordTypeCombo.getSelectedItem()).substring(0,(""+wordTypeCombo.getSelectedItem()).length()-1);
                        state.executeUpdate("Delete from word where userId='"+userID+"' and type='"+type+"' and word <>'"+ww+"'");
                        
                    }
                    
                    else {
                        String type=(""+wordTypeCombo.getSelectedItem()).substring(0,(""+wordTypeCombo.getSelectedItem()).length()-1);
                        System.out.println(type+"---"+catNameCombo.getSelectedItem());
                        state.executeUpdate("Delete from word where userId='"+userID+"' and type='"+type+"' and category='"+catNameCombo.getSelectedItem()+"' and word <>'"+ww+"'");
                    }
                    JOptionPane.showMessageDialog(this,"Deleted Successfully.");
                    setVisible(false);
                    dispose();
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else
            {
                
            }
        }
    }
    
}
