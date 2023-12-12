package ide.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ide.GUI;
import ide.file.FileManager;
import ide.file.Properties;

public class FileMenuActionListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == null) return;
		switch (command) {
		case "Save":
			save();
			break;
		case "Open":
			break;
		case "New":
			_new();
			break;
		}
	}
	private void save() {
		GUI.INSTANCE.saveChanges();
	}
	private void open() {
		
	}
	private void _new() {
		GUI.INSTANCE.newProject();
	}
}
