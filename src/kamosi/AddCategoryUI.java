/*
 * AddCategoryUI.java
 *
 * Created on 27 √»—Ì·, 2008, 05:35 „
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class AddCategoryUI extends JFrame implements ActionListener {
    
    /** Creates a new instance of AddCategoryUI */
    private JLabel addCateLabel,headerLabel,catClassLabel;
    private JTextField categoryField;
    private JComboBox catClassCombo;
    private JButton okButton,cancelButton;
    
    
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    private int userID;
    public AddCategoryUI(int userID) {
        this.userID=userID;
        initUI();
    }
    
    public void initUI() {
        setTitle("Add New Category");
        setBounds((int)(width/3),(int)(height/3.2),400,220);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        getContentPane().setBackground(new Color(204,255,204));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        setLayout(null);
        
        headerLabel=new JLabel("Create New Category");
        headerLabel.setForeground(Color.RED);
        headerLabel.setFont(new Font("Comic Sans MS",Font.BOLD,18));
        headerLabel.setBounds(115,15,200,25);
        
        addCateLabel=new JLabel("Enter Category Name: ");
        addCateLabel.setBounds(25,50,150,25);
        addCateLabel.setForeground(Color.BLUE);
        
        categoryField=new JTextField();
        categoryField.setBounds(25,80,180,25);
        
        catClassLabel=new JLabel("Select Category Type:");
        catClassLabel.setBounds(230,50,150,25);
        catClassLabel.setForeground(Color.BLUE);
        
        catClassCombo=new JComboBox();
        catClassCombo.setBounds(230,80,150,25);
        catClassCombo.addItem("Story");
        catClassCombo.addItem("Book");
        catClassCombo.addItem("Journal");
        catClassCombo.addItem("Other");
        
        okButton=new JButton("Ok");
        okButton.setBounds(80,130,100,25);
        okButton.addActionListener(this);
        
        cancelButton=new JButton("Cancel");
        cancelButton.setBounds(210,130,100,25);
        cancelButton.addActionListener(this);
        
        getContentPane().add(headerLabel);
        getContentPane().add(addCateLabel);
        getContentPane().add(categoryField);
        getContentPane().add(catClassCombo);
        getContentPane().add(catClassLabel);
        getContentPane().add(okButton);
        getContentPane().add(cancelButton);
        
        setVisible(true);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        Object o=e.getSource();
        
        if(o==okButton) {
            
            if(categoryField.getText().equals("")) {
                JOptionPane.showMessageDialog(this,"Please enter the category name!","Kamosi Message",JOptionPane.OK_OPTION);
                categoryField.requestFocus();
            } else {
                Date date=new Date();
                int year=date.getYear()+1900;
                int month=date.getMonth()+1;
                int day=date.getDate();
                
                String dateStr=""+year+"-"+month+"-"+day;
                
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat = null;
                ResultSet rs;
                try {
                    stat=conn.createStatement();
                    rs = stat.executeQuery("Select cat_name,cat_class from category where userid='" + userID + "'");
                    String catName="";
                    String catType="";
                    boolean flag=false;
                    while(rs.next()) {
                        catName=rs.getString("cat_name");
                        catType=rs.getString("cat_class");
                        
                        if(catName.equals(categoryField.getText())&&catType.equals(catClassCombo.getSelectedItem())) {
                            JOptionPane.showMessageDialog(this,"This category is already exist in your dictionary.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                            flag=true;
                            break;
                        }
                    }
                    if(flag==false) {
                        
                        stat.execute("Insert into category(cat_name,cat_class,userid,date) values('"+categoryField.getText()+"','"+catClassCombo.getSelectedItem()+"','"+userID+"','"+dateStr+"')");
                        
                        JOptionPane.showMessageDialog(this,"Category added successfully.","Kamosi Message",JOptionPane.INFORMATION_MESSAGE);
                        
                        setVisible(false);
                        dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
         
                
            }
        }
        if(o==cancelButton) {
            setVisible(false);
            dispose();
        }
    }
    
    
    
}
