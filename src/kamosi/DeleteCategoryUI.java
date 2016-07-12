/*
 * DeleteCategoryUI.java
 *
 * Created on 12 ”» „»—, 2008, 01:00 „
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
public class DeleteCategoryUI extends JFrame implements ActionListener{
    
    private JLabel deleteLabel;
    private JComboBox catNameCombo;
    private JButton btnDelete, btnCancel;
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    private int userID;
    /** Creates a new instance of DeleteCategoryUI */
    public DeleteCategoryUI(int userID) {
        this.userID=userID;
        setTitle("Delete Category");
        setBounds((int)(width/3),(int)(height/3.2),350,150);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        getContentPane().setBackground(new Color(204,255,204));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        setLayout(null);
        
        deleteLabel=new JLabel("Select the category name:");
        deleteLabel.setBounds(10,10,200,25);
        deleteLabel.setForeground(Color.BLUE);
        

        
        
        
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(60, 60, 100, 25);
        btnDelete.addActionListener(this);
        btnDelete.setEnabled(false);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(200, 60, 100, 25);
        btnCancel.addActionListener(this);
        
        UpdateCatCombo();
                
        getContentPane().add(deleteLabel);
        getContentPane().add(btnDelete);
        getContentPane().add(btnCancel);
        
        setVisible(true);
        
    }
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(170,10,165,25);
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
            
            if(catNameCombo.getItemCount()==0)
            {
                btnDelete.setEnabled(false);
            }
            else
                btnDelete.setEnabled(true);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
   
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==btnDelete) 
        {
            int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this category name and all words under this category?","Delete Confirmation",JOptionPane.YES_NO_OPTION);
            
            if(res==JOptionPane.YES_OPTION) {
                String category=(""+catNameCombo.getSelectedItem());
                
                int index=category.indexOf("<");
                
                String categoryName=category.substring(0,index-1);
                String catClass=category.substring(index+1,category.length()-1);
                DBConnection connection=DBConnection.getDBConnection();
                Connection conn=connection.getConnection();
                Statement stat = null;
                try {
                    
                    stat=conn.createStatement();
                    stat.executeUpdate("Delete  from category where cat_name='"+categoryName+"'and userid='"+userID+"' and cat_class='"+catClass+"'");
                    stat.executeUpdate("Delete from word where category='"+category+"' and userId='"+userID+"'");
                    JOptionPane.showMessageDialog(this,"Deleted Successfully.");
                    setVisible(false);
                    dispose();
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                
                
                
                
                setVisible(false);
                dispose();
                
            } else {
                
            }
        }
        if(obj==btnCancel) {
            setVisible(false);
            dispose();
        }
    }
}
