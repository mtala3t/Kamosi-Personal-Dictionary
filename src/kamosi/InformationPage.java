/*
 * InformationPage.java
 *
 * Created on October 1, 2008, 7:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Mohamed
 */
public class InformationPage extends JFrame {
    
    private InformationPagePanel infoPanel;
    
    private int userID;
    double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /** Creates a new instance of InformationPage */
    public InformationPage(int userID) {
        
        this.userID=userID;
        
        setResizable(false);
        setTitle("Information Page");
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds((int)(width/3.2),(int)(height/8.9),400,600);
        
        
        infoPanel=new InformationPagePanel(userID);
        
        setLayout(null);
        getContentPane().setBackground(new Color(204,255,204));
        
        getContentPane().add(infoPanel);
        
        setVisible(true);
    }
    public static void main(String args[]) {
        new InformationPage(16);
    }
}
class InformationPagePanel extends JPanel implements ItemListener,ActionListener{
    JLabel titleLabel,nounsLabel,verbsLabel,adjVerbsLabel,adverbLabel,totalWordsLabel,totalCatLabel,NounsLabel,VerbsLabel,AdjLabel,AdvLabel;
    private int userID;
    
    private JLabel catNameLabel;
    private JComboBox catNameCombo;
    private JTable resultTable;
    private JScrollPane scrolPane;
    
    private int rec[]={0,0,0,0};
    private String rowRec[][];
    private JButton showDetailsButton;
    InformationPagePanel(int userID) {
        this.userID=userID;
        setBackground(new Color(204,255,204));
        setLayout(null);
        setSize(400,600);
        
        
        int nouns = 0,verbs = 0,adj = 0,adver = 0,totalWords = 0,totalCat = 0;
        String userName="";
        
        DBConnection connection=DBConnection.getDBConnection();
        Connection conn=connection.getConnection();
        Statement state=null;
        try {
            
            state=conn.createStatement();
            ResultSet rs=state.executeQuery("Select username from users where userId='"+userID+"'");
            
            rs.next();
            userName=rs.getString("username");
            
            
            String Noun="Noun",Verb="Verb",Adjective="Adjective",Adverb="Adverb";
            
            rs=state.executeQuery("Select count(word) AS nouns from word where userId='"+userID+"' and type ='"+Noun+"'  ");
            rs.next();
            nouns=(rs.getInt(1))-1;
            
            rs=state.executeQuery("Select count(word) AS verbs from word where userId='"+userID+"' and type ='"+Verb+"'  ");
            rs.next();
            verbs=rs.getInt(1);
            
            rs=state.executeQuery("Select count(word) AS adj from word where userId='"+userID+"' and type ='"+Adjective+"'  ");
            rs.next();
            adj=rs.getInt(1);
            
            rs=state.executeQuery("Select count(word) AS adv from word where userId='"+userID+"' and type ='"+Adverb+"'  ");
            rs.next();
            adver=rs.getInt(1);
            
            rs=state.executeQuery("Select count(word) AS total from word where userId='"+userID+"' ");
            rs.next();
            totalWords=(rs.getInt(1))-1;
            
            
            rs=state.executeQuery("Select count(cat_name) AS total from category where userId='"+userID+"' ");
            rs.next();
            totalCat=rs.getInt(1);
            
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        titleLabel=new JLabel("Information Page - "+userName+" -");
        titleLabel.setBounds(80,15,300,25);
        //    titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("MS Comic Sans",Font.BOLD,18));
        
        nounsLabel=new JLabel("           --> Total Number of Nouns:                                  "+nouns);
        nounsLabel.setBounds(25,60,400,25);
        //nounsLabel.setForeground(Color.BLUE);
        
        verbsLabel=new JLabel("           --> Total Number of Verbs:                                   "+verbs);
        verbsLabel.setBounds(25,90,400,25);
        //verbsLabel.setForeground(Color.BLUE);
        
        adjVerbsLabel=new JLabel("           --> Total Number of Adjectives:                           "+adj);
        adjVerbsLabel.setBounds(25,120,400,25);
        // adjVerbsLabel.setForeground(Color.BLUE);
        
        adverbLabel=new JLabel("           --> Total Number of Adverbs:                               "+adver);
        adverbLabel.setBounds(25,150,400,25);
        //   adverbLabel.setForeground(Color.BLUE);
        
        totalWordsLabel=new JLabel("           --> Total Number of Words:                                  "+totalWords);
        totalWordsLabel.setBounds(25,180,400,25);
        //totalWordsLabel.setForeground(Color.BLUE);
        
        totalCatLabel=new JLabel("Total Number of Categories:    "+totalCat);
        totalCatLabel.setBounds(90,220,400,25);
        totalCatLabel.setForeground(Color.BLUE);
        totalCatLabel.setFont(new Font("MS Comic Sans",Font.BOLD,15));
        
        catNameLabel=new JLabel("Select Category Name: ");
        catNameLabel.setBounds(20,265,200,25);
        catNameLabel.setForeground(Color.BLUE);
        
        UpdateCatCombo();
        showDetailsButton=new JButton("Show Word In Details");
        showDetailsButton.setBounds(120,545,170,25);
        showDetailsButton.addActionListener(this);
        
        
        resultTable=createTable();
        
        scrolPane=new JScrollPane(resultTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrolPane.setBounds(20,300,360,240);
        
        
        
        
        
        
        add(titleLabel);
        add(nounsLabel);
        add(verbsLabel);
        add(adjVerbsLabel);
        add(adverbLabel);
        add(totalCatLabel);
        add(totalWordsLabel);
        add(catNameLabel);
        add(scrolPane);
        add(showDetailsButton);
        
    }
    
    public JTable createTable() {
        JTable table = null;
        DBConnection connection=DBConnection.getDBConnection();
        Connection conn=connection.getConnection();
        Statement state=null;
        String records[][]=new String[100000][4];
        rec[0]=0;rec[1]=0;rec[2]=0;rec[3]=0;
        
        String nouns="Noun",verbs="Verb",adj="Adjective",adv="Adverb";
        
        try {
            state = conn.createStatement();
            ResultSet rsNouns=null,rsVerbs=null,rsAdjs=null,rsAdvs=null;
            
            if(catNameCombo.getSelectedItem().equals("All Categories")) {
                rsNouns=state.executeQuery("Select word from word where userId='"+userID+"' and type='"+nouns+"' order by word ASC ");
                
            } else {
                rsNouns=state.executeQuery("Select word from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' and type='"+nouns+"' order by word ASC ");
                
            }
            
            while(rsNouns.next()) {
                if(rsNouns.getString("word").equals("mm")) {
                    
                    continue;
                }
                records[rec[0]][0]=""+rsNouns.getString("word");
                
                
                rec[0]++;
            }
            
            if(catNameCombo.getSelectedItem().equals("All Categories")) {
                
                rsVerbs=state.executeQuery("Select word from word where userId='"+userID+"' and type='"+verbs+"' order by word ASC");
                
            } else {
                
                rsVerbs=state.executeQuery("Select word from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' and type='"+verbs+"' order by word ASC");
                
            }
            
            while(rsVerbs.next()) {
                
                records[rec[1]][1]=""+rsVerbs.getString("word");
                
                rec[1]++;
            }
            
            if(catNameCombo.getSelectedItem().equals("All Categories")) {
                
                rsAdjs=state.executeQuery("Select word from word where userId='"+userID+"' and type='"+adj+"' order by word ASC");
                
            } else {
                
                rsAdjs=state.executeQuery("Select word from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' and type='"+adj+"' order by word ASC");
                
            }
            
            while(rsAdjs.next()) {
                
                records[rec[2]][2]=""+rsAdjs.getString("word");
                
                rec[2]++;
            }
            
            if(catNameCombo.getSelectedItem().equals("All Categories")) {
                
                rsAdvs=state.executeQuery("Select word from word where userId='"+userID+"' and type='"+adv+"' order by word ASC");
            } else {
                
                rsAdvs=state.executeQuery("Select word from word where userId='"+userID+"' and category='"+catNameCombo.getSelectedItem()+"' and type='"+adv+"' order by word ASC");
            }
            
            while(rsAdvs.next()) {
                
                records[rec[3]][3]=""+rsAdvs.getString("word");
                
                
                rec[3]++;
            }
            int rec0=rec[0];
            int rec1=rec[1];
            int rec2=rec[2];
            int rec3=rec[3];
            Arrays.sort(rec);
            
            rowRec = new String [rec[3]+1][4];
            
            for (int i = 0; i < rec[3]; i++) {
                rowRec[i][0] = 	records[i][0];
                rowRec[i][1] = 	records[i][1];
                rowRec[i][2] = 	records[i][2];
                rowRec[i][3] = 	records[i][3];
            }
            
            rowRec[rec[3]][0]="Total = "+rec0;
            rowRec[rec[3]][1]="Total = "+rec1;
            rowRec[rec[3]][2]="Total = "+rec2;
            rowRec[rec[3]][3]="Total = "+rec3;
            
            String colsNames[]={"Nouns","Verbs","Adjectives","Adverbs"};
            
            table=new JTable(rowRec,colsNames){
                public boolean isCellEditable(int iRows, int iCols) {
                    return false;	//Disable All Columns of Table.
                }};
                
                table.setRowHeight(20);
                table.setGridColor(Color.RED);
                table.setSelectionBackground(Color.WHITE);
                
                
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        return table;
        
        
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.drawLine(20,52,370,52);
        g2.drawLine(20,210,370,210);
        g2.drawLine(20,210,20,52);
        g2.drawLine(370,210,370,52);
        
        g2.drawLine(20,180,370,180);
    }
    
    public void UpdateCatCombo() {
        if(catNameCombo!=null) {
            remove(catNameCombo);
            
        }
        
        catNameCombo=new JComboBox();
        catNameCombo.setBounds(190,265,180,25);
        catNameCombo.addItemListener(this);
        catNameCombo.addItem("Default Category");
        
        
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
            add(catNameCombo);
            catNameCombo.setSelectedItem("All Categories");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void itemStateChanged(ItemEvent e) {
        Object obj=e.getSource();
        if(obj==catNameCombo) {
            UpdateTableView();
            
        }
    }
    public void UpdateTableView() {
        if(resultTable!=null) {
            scrolPane.getViewport().remove(resultTable);
            resultTable=createTable();
            int rowCount=resultTable.getRowCount();
            if(rowCount>1) {
                showDetailsButton.setEnabled(true);
            } else {
                showDetailsButton.setEnabled(false);
            }
            
            scrolPane.getViewport().add(resultTable);
            this.repaint();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==showDetailsButton) {
            if(resultTable!=null) {
                
                int rowCount=resultTable.getRowCount();
                
                int  row = resultTable.getSelectedRow();
                int  col = resultTable.getSelectedColumn();
                if(row!=-1&&col!=-1) {
                    Object value=resultTable.getValueAt(row,col);
                    String word=""+value;
                    System.out.println("Word= "+word);
                    if(row!=-1&&row!=rowCount-1&&value!=null) {
                        
                        
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
                    } else if(row==rowCount-1||value==null) {
                        JOptionPane.showMessageDialog(this,"Please Select a valid cell that contains a word.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this,"Please Select a word to view in details.");
                }
            }
            
            
        }
    }
    
}