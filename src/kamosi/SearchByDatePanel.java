/*
 * SearchByDatePanel.java
 *
 * Created on September 26, 2008, 9:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Mohamed
 */
public class SearchByDatePanel extends JPanel implements ActionListener {
    private JLabel searchLabel,dayLabel,monthLabel,yearLabel;
    private JComboBox dayCombo,monthCombo,yearCombo;
    private JButton searchButton;
    private JTable resultTable;
    private JScrollPane scrolPane;
    private JButton showDetailsButton;
    
    private int userID;
    
    private int rec=0;
    private String rowRec[][];
    /** Creates a new instance of SearchByDatePanel */
    public SearchByDatePanel(int userID) {
        this.userID=userID;
        setLayout(null);
        
        
        searchLabel=new JLabel("Please select the date to search: ");
        searchLabel.setBounds(25,25,200,25);
        
        
        dayLabel=new JLabel("Day: ");
        dayLabel.setBounds(240,25,50,25);
        dayLabel.setForeground(Color.BLUE);
        
        dayCombo=new JComboBox();
        
        for(int i=1;i<=31;i++) {
            if(i<10)
                dayCombo.addItem("0"+i);
            else
                dayCombo.addItem(""+i);
        }
        dayCombo.setBounds(280,25,50,25);
        
        monthLabel=new JLabel("Month: ");
        monthLabel.setBounds(350,25,50,25);
        monthLabel.setForeground(Color.BLUE);
        
        
        monthCombo=new JComboBox();
        
        for(int i=1;i<=12;i++) {
            if(i<10)
                monthCombo.addItem("0"+i);
            else
                monthCombo.addItem(""+i);
        }
        monthCombo.setBounds(400,25,50,25);
        
        yearLabel=new JLabel("Year: ");
        yearLabel.setForeground(Color.BLUE);
        yearLabel.setBounds(470,25,50,25);
        
        yearCombo=new JComboBox();
        
        for(int i=2008;i<=2020;i++) {
            yearCombo.addItem(""+i);
        }
        yearCombo.setBounds(510,25,80,25);
        
        
        searchButton=new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setBounds(290,70,120,25);
        
        
        resultTable=createTable();
        
        scrolPane=new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrolPane.setBounds(20,110,650,330);
        
        showDetailsButton=new JButton("Show Word In Details");
        showDetailsButton.setBounds(250,445,170,25);
        showDetailsButton.addActionListener(this);
        
        
        add(searchLabel);
        add(dayLabel);
        add(dayCombo);
        add(monthLabel);
        add(monthCombo);
        add(yearLabel);
        add(yearCombo);
        add(searchButton);
        add(scrolPane);
        add(showDetailsButton);
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
            String date=yearCombo.getSelectedItem()+"-"+monthCombo.getSelectedItem()+"-"+dayCombo.getSelectedItem();
            
            rs=state.executeQuery("Select * from word where userId='"+userID+"' and date='"+date+"' order by word ASC");
            
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
                }};
                
                table.setRowHeight(20);
                table.setGridColor(Color.RED);
                table.setSelectionBackground(Color.yellow);
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
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
        
        
        if(obj==searchButton){
            
            UpdateTableView();
            if(resultTable.getRowCount()==0)
                  JOptionPane.showMessageDialog(this,"No words added at this date .");
        }
        if(obj==showDetailsButton) {
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
                } else {
                    JOptionPane.showMessageDialog(this,"Please Select a row to view in details.");
                }
            }
            
            
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.drawLine(20,100,670,100);
        g2.drawLine(20,10,670,10);
        g2.drawLine(20,10,20,100);
        g2.drawLine(670,10,670,100);
        
    }
}
