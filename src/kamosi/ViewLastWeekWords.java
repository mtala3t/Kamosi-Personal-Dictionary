/*
 * ViewLastWeekWords.java
 *
 * Created on September 28, 2008, 8:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Mohamed
 */
public class ViewLastWeekWords extends JFrame 
{
    private JLabel listLabel;
 
    private JScrollPane scrolPane;
    private JTable resultTable;	//Table for Displaying Records.
    
    private int rec = 0;
    private int total = 0;
    
    private String rowRec[][];	//String Type Array use to Load Records into File.
    
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    private int userID;
    
    /** Creates a new instance of ViewLastWeekWords */
    public ViewLastWeekWords(int userID) 
    {
        this.userID=userID;
        
        setTitle("Words Added Last Week");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((int)(width/3.75),(int)(height/4.3),650,500);
        
        setLayout(null);
        getContentPane().setBackground(new Color(204,255,204));
        
        listLabel=new JLabel("List of all words added last week: ");
        listLabel.setBounds(10,10,300,25);
        
        resultTable = createTable();			//Creating Table.
        
        scrolPane = new JScrollPane(resultTable);	//Adding Table to ScrollPane.
        scrolPane.setBounds(10, 35, 610, 420);	//Aligning ScrollPane.
        
        
        getContentPane().add(listLabel);
        getContentPane().add(scrolPane);
        setVisible(true);
    }
    
    
    public static void main(String ars[])
    {
        new ViewLastWeekWords(16);
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
            
            rs=state.executeQuery("Select * from word where userId='"+userID+"'order by word ASC");
            
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
                table.setGridColor(Color.YELLOW);
                table.setSelectionBackground(Color.GRAY);
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return table;
        
        
    }
    
}
