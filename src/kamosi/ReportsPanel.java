/*
 * To change this template, choose Tools | Templates
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mohamed
 */
public class ReportsPanel extends JPanel implements ActionListener {

    JLabel viewLabel, catLabel;
    JComboBox wordTypesCombo;
    JComboBox catNameCombo;
    private JButton viewReportBut;
    private int userID;

    public ReportsPanel(int userID) {
        this.userID = userID;

        setLayout(null);

        catLabel = new JLabel("Select the type of words to view all records: ");
        catLabel.setBounds(20, 20, 300, 25);

        viewLabel = new JLabel("Select the category name to view all records: ");
        viewLabel.setBounds(20, 50, 300, 25);

        wordTypesCombo = new JComboBox();
        wordTypesCombo.setBounds(450, 20, 200, 25);
        wordTypesCombo.addItem("Nouns");
        wordTypesCombo.addItem("Verbs");
        wordTypesCombo.addItem("Adjectives");
        wordTypesCombo.addItem("Adverbs");
        wordTypesCombo.addItem("All words");
        wordTypesCombo.setSelectedItem("All words");

        UpdateCatCombo();
        viewReportBut = new JButton("Show Report");
        viewReportBut.setBounds(250, 85, 170, 25);
        viewReportBut.addActionListener(this);

        add(wordTypesCombo);
        add(viewLabel);
        add(catLabel);
        add(viewReportBut);
    }

    public void UpdateCatCombo() {
        if (catNameCombo != null) {
            remove(catNameCombo);

        }

        catNameCombo = new JComboBox();
        catNameCombo.setBounds(450, 50, 200, 25);
        catNameCombo.addItem("Default Category");
        add(catNameCombo);
        updateUI();
        DBConnection connection = DBConnection.getDBConnection();
        Connection conn = connection.getConnection();
        Statement stat = null;
        ResultSet rs;
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery("Select cat_name,cat_class from category where userid='" + userID + "'");
            String catName = "";
            String catType = "";

            while (rs.next()) {
                catName = rs.getString("cat_name");
                catType = rs.getString("cat_class");

                catNameCombo.addItem(catName + " <" + catType + ">");

            }

            catNameCombo.addItem("All Categories");
            catNameCombo.setSelectedItem("All Categories");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == viewReportBut) 
        {
            CreateReport();
        }
    }

    public void CreateReport() {

        String reportSource = "";
        String reportDest = "./report/results/WordsReport.pdf";

        String wordTypes = "" + wordTypesCombo.getSelectedItem();
        String catName = "" + catNameCombo.getSelectedItem();
        String userName = "";

        DBConnection conn = DBConnection.getDBConnection();
        Connection con = conn.getConnection();
        try {
            Statement stat = con.createStatement();

            ResultSet rs = stat.executeQuery("Select username from users where userId='" + userID + "'");

            rs.next();
            userName = rs.getString("username");

        } catch (SQLException ex) {
            Logger.getLogger(ReportsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("reportTitle", "Words Report");
        params.put("author", userName);
        params.put("startDate", (new java.util.Date()).toString());

        if (wordTypes.equals("All words") && catName.equals("All Categories")) {

            reportSource = "./report/templates/AllCatAllWordsReport.jrxml";

        } else if (wordTypes.equals("All words")) {
            reportSource = "./report/templates/AllWordsReport.jrxml";
            params.put("category", catName);
        } else if (catName.equals("All Categories")) {

            reportSource = "./report/templates/AllCatWordsReport.jrxml";
            String type = ("" + wordTypes).substring(0, ("" + wordTypes).length() - 1);
            params.put("wordType", type);
        } else {
            reportSource = "./report/templates/CustomizedWordsReport.jrxml";
            String type = ("" + wordTypes).substring(0, ("" + wordTypes).length() - 1);
            params.put("wordType", type);
            params.put("category", catName);
        }
        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportSource);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);

            //  JasperExportManager.exportReportToPdfFile(
            //        jasperPrint, reportDest);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } // Exception handling for the Class.forName method.
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.drawLine(10,120,670,120);
        g2.drawLine(10,10,670,10);
        g2.drawLine(10,10,10,120);
        g2.drawLine(670,10,670,120);
        
    }
}
