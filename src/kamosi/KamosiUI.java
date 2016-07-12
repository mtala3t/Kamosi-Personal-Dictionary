/*
 * KamosiUI.java
 *
 * Created on 22 íæäíæ, 2008, 01:46 Õ
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class KamosiUI extends JFrame implements ActionListener,ItemListener,ChangeListener {
    private JMenuBar myMenuBar;
    private JMenu fileMenu,optionMenu,windowMenu,editMenu,helpMenu,viewMenu;
    private JMenuItem exitItem,addWordItem,deleteWordItem,addCatItem,deleteCatItem,changeBgColor,changeStyle;
    private JMenuItem changePasswordItem,createUserItem;
    private JMenuItem viewUsersItem,viewWordsItem,viewInfoPageItem,viewDicItem;
    private JMenuItem helpContentsItem,aboutItem;
    private JMenuItem searchWordItem,searchByDateItem;
    private JTabbedPane myTabbedPane;
    
    private KamosiIndexPanel indexPanel;
    private AddWordPanel addWordPan;
    private SearchWordPanel searchPanel;
    private ViewAllWordsPanel allWorsPanel;
    private SearchByDatePanel searchByDatePanel;
    private ReportsPanel reportsPan;
    
    private String strings[] = {"1. Metal", "2. Motif", "3. Windows"};
    private UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();
    private ButtonGroup group = new ButtonGroup();
    private JRadioButtonMenuItem radio[] = new JRadioButtonMenuItem[strings.length];
    
    Dimension d=Toolkit.getDefaultToolkit().getScreenSize() ;
    int width=(int)d.getWidth();
    int height=(int)d.getHeight();
    private String userName;
    private int userID;
    /** Creates a new instance of KamosiUI */
    public KamosiUI(String userName,int userID) {
        setTitle("::Welcome to Mohamed Talaat's Personal Dictionary <Kamosi>::");
        setBounds(300,60,700,580);
        setIconImage(getToolkit().getImage("Images/Home.gif"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        this.userName=userName;
        this.userID=userID;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                Exit();
            }
        });
        
        
        myMenuBar=new JMenuBar();
        
        fileMenu=new JMenu("File");
        fileMenu.setMnemonic('F');
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic((int)'E');
        viewMenu = new JMenu("View");
        viewMenu.setMnemonic((int)'V');
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic((int)'H');
        
        exitItem=new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,Event.CTRL_MASK));
        exitItem.setMnemonic('Q');
        exitItem.addActionListener(this);
        
        addWordItem = new JMenuItem("Add New Word", new ImageIcon("Images/add.gif"));
        addWordItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        addWordItem.setMnemonic((int)'N');
        addWordItem.addActionListener(this);
        
        deleteWordItem = new JMenuItem("Delete Words", new ImageIcon("Images/Recycle.gif"));
        deleteWordItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));
        deleteWordItem.setMnemonic((int)'W');
        deleteWordItem.addActionListener(this);
        
        addCatItem = new JMenuItem("Add New Category", new ImageIcon("Images/new.gif"));
        addCatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        addCatItem.setMnemonic((int)'C');
        addCatItem.addActionListener(this);
        
        
        deleteCatItem = new JMenuItem("Delete Category", new ImageIcon("Images/delete.gif"));
        deleteCatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
        deleteCatItem.setMnemonic((int)'L');
        deleteCatItem.addActionListener(this);
        
        
        searchWordItem = new JMenuItem("Search Word", new ImageIcon("Images/find.gif"));
        searchWordItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        searchWordItem.setMnemonic((int)'S');
        searchWordItem.addActionListener(this);
        
        searchByDateItem = new JMenuItem("Search By Date", new ImageIcon("Images/Search.gif"));
        searchByDateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
        searchByDateItem.setMnemonic((int)'T');
        searchByDateItem.addActionListener(this);
        
        viewUsersItem = new JMenuItem("View All Users", new ImageIcon("Images/Refresh.gif"));
        viewUsersItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        viewUsersItem.setMnemonic((int)'V');
        viewUsersItem.addActionListener(this);
        
        viewWordsItem = new JMenuItem("View All Words", new ImageIcon("Images/Fruit.gif"));
        viewWordsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        viewWordsItem.setMnemonic((int)'A');
        viewWordsItem.addActionListener(this);
        
        viewDicItem = new JMenuItem("Small Dictionary", new ImageIcon("Images/Open.gif"));
        viewDicItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
        viewDicItem.setMnemonic((int)'I');
        viewDicItem.addActionListener(this);
        
        viewInfoPageItem = new JMenuItem("Information Page", new ImageIcon("Images/Info.gif"));
        viewInfoPageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
        viewInfoPageItem.setMnemonic((int)'F');
        viewInfoPageItem.addActionListener(this);
        
        optionMenu=new JMenu("Options");
        optionMenu.setMnemonic('O');
        
        windowMenu = new JMenu("Window");
        windowMenu.setMnemonic((int)'W');
        
        changeBgColor=new JMenuItem("Change Background Color");
        changeBgColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,Event.CTRL_MASK));
        changeBgColor.setMnemonic('G');
        changeBgColor.addActionListener(this);
        
        
        
        changeStyle = new JMenu("Change Look and Feel");
        changeStyle.setMnemonic((int)'S');
        for( int i = 0; i < radio.length ; i++ ) {
            radio[i] = new JRadioButtonMenuItem(strings[i]);
            radio[0].setSelected(true);
            radio[i].addItemListener(this);
            group.add(radio[i]);
            changeStyle.add(radio[i]);
        }
        
        changePasswordItem = new JMenuItem("Change Your Password",new ImageIcon("Images/Keys.gif"));
        changePasswordItem.setMnemonic((int)'P');
        changePasswordItem.addActionListener(this);
        createUserItem = new JMenuItem("Create New User");
        createUserItem.setMnemonic((int)'N');
        createUserItem.addActionListener(this);
        
        helpContentsItem=new JMenuItem("Help Contents", new ImageIcon("Images/Glass.gif"));
        helpContentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
        helpContentsItem.setMnemonic((int)'H');
        helpContentsItem.addActionListener(this);
        
        aboutItem = new JMenuItem("About Kamosi Personal Dictionary", new ImageIcon("Images/Windows.gif"));
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
        aboutItem.setMnemonic((int)'L');
        aboutItem.addActionListener(this);
        
        
        optionMenu.add(changeBgColor);
        optionMenu.addSeparator();
        optionMenu.add(changeStyle);
        
       
        fileMenu.add(addWordItem);
        fileMenu.add(addCatItem);
        fileMenu.addSeparator();
        fileMenu.add(deleteWordItem);
        fileMenu.add(deleteCatItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        editMenu.add(searchWordItem);
        editMenu.add(searchByDateItem);
                
        viewMenu.add(viewWordsItem);
        viewMenu.add(viewUsersItem);
        viewMenu.add(viewDicItem);
        viewMenu.add(viewInfoPageItem);
        
        optionMenu.add(changeBgColor);
        optionMenu.addSeparator();
        optionMenu.add(changeStyle);
        
        windowMenu.add(changePasswordItem);
        windowMenu.add(createUserItem);
        
        helpMenu.add(helpContentsItem);
        helpMenu.add(aboutItem);
        
        myMenuBar.add(fileMenu);
        myMenuBar.add(editMenu);
        myMenuBar.add(viewMenu);
        myMenuBar.add(optionMenu);
        myMenuBar.add(windowMenu);
        myMenuBar.add(helpMenu);
        
        
        setJMenuBar(myMenuBar);
        
        myTabbedPane=new JTabbedPane(JTabbedPane.TOP);
        
        indexPanel=new KamosiIndexPanel("Help/Index.htm");
        indexPanel.setName("Index");
        addWordPan=new AddWordPanel(userID);
        addWordPan.setName("Add Word");
        searchPanel=new SearchWordPanel(userID);
        searchPanel.setName("Search Words");
        allWorsPanel=new ViewAllWordsPanel(userID);
        allWorsPanel.setName("View All Words");
        searchByDatePanel=new SearchByDatePanel(userID);
        searchByDatePanel.setName("Search By Date");
        
        reportsPan=new ReportsPanel(userID);
        reportsPan.setName("Reports");
        
        myTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        myTabbedPane.addTab("Kamosi Index",new ImageIcon("Images/Home.gif"),indexPanel);
        myTabbedPane.addTab("Add Word",new ImageIcon("Images/add.gif"),addWordPan);
        myTabbedPane.addTab("Search Words",new ImageIcon("Images/find.gif"),searchPanel);
        
        myTabbedPane.addTab("View All Words",new ImageIcon("Images/Mirror.gif"),allWorsPanel);
        myTabbedPane.addTab("Search By Date",new ImageIcon("Images/Search.gif"),searchByDatePanel);
        myTabbedPane.addTab("Reports",new ImageIcon("Images/Search.gif"),reportsPan);
        
        Component comps[]=myTabbedPane.getComponents();
        for(int i=0;i<comps.length;i++) {
            comps[i].setBackground(new Color(204,255,204));
            comps[i].repaint();
        }
        myTabbedPane.addChangeListener(this);
        add(myTabbedPane);
        setVisible(true);
        
        
    }
    
    public void Exit() {
        int res=JOptionPane.showConfirmDialog(this,"Are you sure you want to exit?","Exit Confirmation",JOptionPane.YES_NO_OPTION);
        
        if(res==JOptionPane.YES_OPTION) {
            
            SoundManger souManger=new SoundManger("Sounds/goodbye.wav");
            souManger.play();
            
            setVisible(false);
            dispose();
            System.exit(0);
           
        } else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        if(obj.equals(exitItem)) {
            Exit();
        }
        if(obj==addWordItem) {
            addWordPan.UpdateCatCombo();
            myTabbedPane.setSelectedComponent(addWordPan);
            
        }
        if(obj==deleteWordItem)
        {
            new DeleteWordsUI(userID);
        }
        if(obj==addCatItem) {
            new AddCategoryUI(userID);
            addWordPan.UpdateCatCombo();
            
        }
        if(obj==deleteCatItem)
        {
            new DeleteCategoryUI(userID);
            allWorsPanel.UpdateCatCombo();
            addWordPan.UpdateCatCombo();
        }
        if(obj==searchWordItem) {
            myTabbedPane.setSelectedComponent(searchPanel);
        }
         if(obj==searchByDateItem) {
            myTabbedPane.setSelectedComponent(searchByDatePanel);
        }
        if(obj==changeBgColor) {
            Color color = myTabbedPane.getBackground();
            color = JColorChooser.showDialog(this, "Choose Background Color", color);
            if(color==null){} else {
                Component comps[]=myTabbedPane.getComponents();
                for(int i=0;i<comps.length;i++) {
                    comps[i].setBackground(color);
                    comps[i].repaint();
                }
                
            }
        }
        
        if(obj==changePasswordItem) {
            
            new ChangePassword(userName);
            
        }
        if(obj==createUserItem) {
            new NewUser();
        }
        if(obj==viewWordsItem) {
            myTabbedPane.setSelectedComponent(allWorsPanel);
            allWorsPanel.UpdateCatCombo();
        }
        if(obj==viewUsersItem) {
            new ViewAllUsers();
        }
        if(obj==viewDicItem)
        {
            new DictionaryFrame();
        }
        if(obj==viewInfoPageItem) {
            new InformationPage(userID);
        }
        if(obj==helpContentsItem) {
            KamosiHelp contHelp = new KamosiHelp("Kamosi Personal Dictionary Help", "Help/Index.htm");
            
            contHelp.show();
            
        }
        if(obj==aboutItem) {
            String msg = "Mohamed Talaat's Personal Dictionary.\n\n" + "Created & Designed By:\n" +
                    "Mohamed Talaat Saad\n\n" + "E-mail me:\n teto_soft@yahoo.com";
            JOptionPane.showMessageDialog(this, msg, "About Kamosi Personal Dictionary", JOptionPane.PLAIN_MESSAGE);
        }
    }
    public void itemStateChanged(ItemEvent e) {
        for( int i=0;i<radio.length; i++ )
            if(radio[i].isSelected()) {
            changeLookAndFeel(i);
            }
    }
    
    public void changeLookAndFeel(int value) {
        try {
            UIManager.setLookAndFeel(looks[value].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) { }
        
    }
    
    public void stateChanged(ChangeEvent e) {
        
        if(myTabbedPane.getSelectedComponent().getName().equals("View All Words")) {
            allWorsPanel.UpdateTableView();
        }
         if(myTabbedPane.getSelectedComponent().getName().equals("View All Words")) {
            allWorsPanel.UpdateCatCombo();
        }
        if(myTabbedPane.getSelectedComponent().getName().equals("Add Word")) {
            addWordPan.UpdateCatCombo();
        }
        if(myTabbedPane.getSelectedComponent().getName().equals("Search Words")) {
            searchPanel.UpdateCatCombo();
        }
        if(myTabbedPane.getSelectedComponent().getName().equals("Search By Date")) {
            searchByDatePanel.UpdateTableView();
        }
       if(myTabbedPane.getSelectedComponent().getName().equals("Reports")) {
            reportsPan.UpdateCatCombo();
        }
    }
    
    
}
