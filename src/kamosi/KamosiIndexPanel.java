/*
 * KamosiIndexPanel.java
 *
 * Created on 27 Улгиг, 2008, 03:25 у
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kamosi;

import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

/**
 *
 * @author Mohamed Talaat Saad
 */

public class KamosiIndexPanel extends JScrollPane implements HyperlinkListener {
    
    JEditorPane html;
    
    public KamosiIndexPanel(String filename) {
        
        try {
            File f = new File(filename);		//Getting the HTML File.
            String s = f.getAbsolutePath();		//Getting the AbsolutePath of File.
            s = "file:" + s;
            URL url = new URL(s);			//Setting URL.
            html = new JEditorPane(s);		//EditorPane to Display File Contents.
            html.setEditable(false);		//Set EditorPane Disabled So User only View the File.
            html.addHyperlinkListener(this);	//Adding the HyperLink Listener.
            JViewport vp = getViewport();		//Creating ViewPort to View the File.
            vp.add(html);				//Adding EditorPane to ViewPort.
        } catch (MalformedURLException e) { } catch (IOException e) {	}
        
    }
    
    //Function Perform By the HyperLinks of HTML Help File.
    
    public void hyperlinkUpdate(HyperlinkEvent e) {
        
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            linkActivated(e.getURL());
        }
    }
    
    //Function for Loading other HTML Pages in Your HTML Help.
    
    protected void linkActivated(URL u) {
        
        Cursor c = html.getCursor();
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        html.setCursor(waitCursor);
        SwingUtilities.invokeLater(new PageLoader(u, c));
        
    }
    
    //Following Class Load Other Pages Included in Our HTML Help File.
    
    class PageLoader implements Runnable {
        
        //Constructor of Class.
        
        PageLoader(URL u, Cursor c) {
            
            url = u;
            cursor = c;
            
        }
        
        public void run() {
            
            if (url == null) {
                html.setCursor(cursor);
                Container parent = html.getParent();
                parent.repaint();
            } else {
                Document doc = html.getDocument();
                try {
                    html.setPage(url);
                } catch (IOException ioe) {
                    html.setDocument(doc);
                    getToolkit().beep();
                } finally {
                    url = null;
                    SwingUtilities.invokeLater(this);
                }
            }
            
        }
        
        URL url;
        Cursor cursor;
        
    }
    
}