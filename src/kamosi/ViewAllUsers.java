package kamosi;

import java.awt.Color;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ViewAllUsers extends JFrame  {
    
    private JPanel usersPanel = new JPanel();
    private JLabel listLabel;
    private JScrollPane scrolPane;
    private JTable resultTable;	//Table for Displaying Records.
    
    
    private int rec = 0;
    private int total = 0;
    
    private String rowRec[][];	//String Type Array use to Load Records into File.
    
    //Constructor of Class.
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    public ViewAllUsers() 
    {
        setTitle("View All Users");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((int)(width/2.5),(int)(height/3.7),280,350);
        setResizable(false);
        
        listLabel=new JLabel("List of all registered users: ");
        listLabel.setBounds(10,10,300,25);
        //Setting Panel's Layout.
        usersPanel.setLayout(null);
        usersPanel.setBackground(new Color(204,255,204));
        
        resultTable = createTable();			//Creating Table.
        
        scrolPane = new JScrollPane(resultTable);	//Adding Table to ScrollPane.
        scrolPane.setBounds(10, 35, 250, 260);	//Aligning ScrollPane.
        
        //Adding All the Controls in Panel.
        
        usersPanel.add(scrolPane);
        
        //Adding Panel to the Form.
        getContentPane().add(listLabel);
        getContentPane().add(usersPanel);

        setVisible(true);
        
    }
    
    public static void main(String args[]) {
        new ViewAllUsers();
    }
    //Function to Create the Table and Add Data to Show.
    
    private JTable createTable() 
    {
        JTable table = null;
        String records[][] = new String [500][2];		//String Type Array use to Load Records From Table.
        DBConnection connection=DBConnection.getDBConnection();
        Connection con=connection.getConnection();
        Statement st=null;
        try {	//Opening the Required Table.
            String q = "SELECT * FROM users ORDER BY username";	//Query use to Retrieve the Records.
            
            st = con.createStatement();			//Creating Statement Object.
            ResultSet rs = st.executeQuery(q);		//Running Query.
            
            while (rs.next()) {
                records[rec][0] = rs.getString("username");
                records[rec][1] = ""+rs.getString("date");
                
                rec++;
            }
            
            total = rec;
            
            if (total == 0) {
                JOptionPane.showMessageDialog(null, "Table is Empty");
            } else {
                rowRec = new String [total][2];
                for (int i = 0; i < total; i++) {
                    rowRec[i][0] = 	records[i][0];
                    rowRec[i][1] = 	records[i][1];
                    
                }
            }
        } catch (Exception sqlEx) { }
        
        //String Array For Table Columns.
        String colsName[] = {"User name", "Registeration Date",};
        
        table = new JTable(rowRec, colsName) {
            public boolean isCellEditable(int iRows, int iCols) {
                return false;	//Disable All Columns of Table.
            }
        };
        
        table.setRowHeight(20);	//Set Rows Height.
        table.setGridColor(Color.RED);
        table.setSelectionBackground(Color.YELLOW);
        
        
        
        return table;
        
    }
    
}