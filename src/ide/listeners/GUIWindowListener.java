package ide.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

public class GUIWindowListener extends WindowAdapter {

	  @Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	       System.out.println("dick");
	    }

}
