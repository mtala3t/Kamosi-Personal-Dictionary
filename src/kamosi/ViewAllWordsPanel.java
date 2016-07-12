/*
 * SearchWordPanel.java
 *
 * Created on 23 Улгиг, 2008, 05:29 у
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class ViewAllWordsPanel extends JPanel implements ItemListener,ActionListener {
    
    JTable resultTable;
    JScrollPane scrolPane;
    JLabel viewLabel,catLabel;
    JComboBox wordTypesCombo;
    JComboBox catNameCombo;
    
    
    private JButton showDetailsButton;
    private int userID;
    private int rec=0;
    private String rowRec[][];
    /** Creates a new instance of SearchWordPanel */
    public ViewAllWordsPanel(int userID) {
        this.userID=userID;
        
        setLayout(null);
        
        catLabel=new JLabel("Select the type of words to view all records: ");
        catLabel.setBounds(20,20,300,25);
        
        viewLabel=new JLabel("Select the category name to view all records: ");
        viewLabel.setBounds(20,50,300,25);
        
        wordTypesCombo=new JComboBox();
        wordTypesCombo.setBounds(450,20,200,25);
        wordTypesCombo.addItemListener(this);
        wordTypesCombo.addItem("Nouns");
        wordTypesCombo.addItem("Verbs");
        wordTypesCombo.addItem("Adjectives");
        wordTypesCombo.addItem("Adverbs");
        wordTypesCombo.addItem("All words");
        wordTypesCombo.setSelectedItem("All words");
        
        UpdateCatCombo();

        
        resultTable=createTable();
        
        scrolPane=new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrolPane.setBounds(20,80,650,360);
        
        showDetailsButton=new JButton("Show Word In Details");
        showDetailsButton.setBounds(250,445,170,25);
        showDetailsButton.addActionListener(this);
        
        add(scrolPane);
        add(wordTypesCombo);
        add(viewLabel);
        add(catLabel);
        add(showDetailsButton);
    }
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(450,50,200,25);
        catNameCombo.addItem("Default Category");
        catNameCombo.addItemListener(this);
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
            
            catNameCombo.addItem("All Categories");
                    catNameCombo.setSelectedItem("All Categories");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    public void itemStateChanged(ItemEvent e) {
        
        Object obj=e.getSource();
        if(obj==catNameCombo||obj==wordTypesCombo)
        {
            UpdateTableView();
            
        }
        
    }
    public JTable createTable() {
        JTable table = null;
        DBConnection connection=DBConnection.getDBConnection();
        Connection conn=connection.getConnection();
        Statement state=null;
        String records[][]=new String[100000][7];
        rec=0;
        try {
            state = conn.createStatement();
            ResultSet rs=null;
            if(wordTypesCombo.getSelectedItem().equals("All words")&&catNameCombo.getSelectedItem().equals("All Categories")) {
                
                rs=state.executeQuery("Select * from word where userId='"+userID+"' order by word ASC");
                
            } else if(wordTypesCombo.getSelectedItem().equals("All words")) {
                rs=state.executeQuery("Select * from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' order by word ASC");
            } else if(catNameCombo.getSelectedItem().equals("All Categories")) {
                String type=(""+wordTypesCombo.getSelectedItem()).substring(0,(""+wordTypesCombo.getSelectedItem()).length()-1);
                rs=state.executeQuery("Select * from word where userId='"+userID+"' and type='"+type+"' order by word ASC");
                
            }
            
            else {
                String type=(""+wordTypesCombo.getSelectedItem()).substring(0,(""+wordTypesCombo.getSelectedItem()).length()-1);
                rs=state.executeQuery("Select * from word where userId='"+userID+"' and type='"+type+"' and category='"+catNameCombo.getSelectedItem()+"'order by word ASC");
            }
            while(rs.next()) {
                if(rs.getString("word").equals("mm")) {
                    continue;
                }
                records[rec][0]=""+rs.getString("word");
                records[rec][1]=""+rs.getString("type");
                records[rec][2]=""+rs.getString("translation");
                records[rec][3]=""+rs.getString("relatedwords");
                records[rec][4]=""+rs.getString("example");
                records[rec][5]=""+rs.getString("category");
                records[rec][6]=""+rs.getString("date");
                
                rec++;
            }
            
            
            rowRec=new String[rec][7];
            for (int i = 0; i < rec; i++) {
                rowRec[i][0] = 	records[i][0];
                rowRec[i][1] = 	records[i][1];
                rowRec[i][2] = 	records[i][2];
                rowRec[i][3] = 	records[i][3];
                rowRec[i][4] = 	records[i][4];
                rowRec[i][5] =  records[i][5];
                rowRec[i][6] =  records[i][6];
            }
            
            String colsNames[]={"Word","Type","AR Translation","Related Words","Example","Category","Date"};
            
            table=new JTable(rowRec,colsNames){
                public boolean isCellEditable(int iRows, int iCols) {
                    return false;	//Disable All Columns of Table.
                }
            };
                
                table.setRowHeight(20);
                table.setGridColor(Color.RED);
                table.setSelectionBackground(Color.yellow);
                
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return table;
        
        
    }
    
    public void UpdateTableView() {
        if(resultTable!=null) {
            scrolPane.getViewport().remove(resultTable);
            resultTable=createTable();

            int rowCount=resultTable.getRowCount();
            if(rowCount>0)
            {
                showDetailsButton.setEnabled(true);
            }
            else
            {
                showDetailsButton.setEnabled(false);
            }
            scrolPane.getViewport().add(resultTable);

            this.repaint();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==showDetailsButton) 
        {
            if(resultTable!=null) {
                
                int rowCount=resultTable.getRowCount();
                
                int  row = resultTable.getSelectedRow();
                if(row!=-1) {
                    Object value=resultTable.getValueAt(row,0);
                    String word=""+value;
                    
                    DBConnection connection=DBConnection.getDBConnection();
                    Connection conn=connection.getConnection();
                    Statement state=null;
                    
                    try {
                        state = conn.createStatement();
                        ResultSet rs=null;
                        
                        rs=state.executeQuery("Select * from word where userId='"+userID+"' and word='"+word+"'");
                        
                        rs.next();
                        int wordId=rs.getInt("id");
                        
                        new EditWordFrame(userID,wordId);
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                     JOptionPane.showMessageDialog(this,"Please Select a row to view in details.");
                }
            }
            
            
        }
    }
    
    
}
