package ide.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ide.GUI;
import ide.RunManager;
import ide.file.FileManager;

public class RunMenuActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command) {
		case "Run":
			run();
		}
		
	}
	private void run() {
		RunManager.run();
	}

}
