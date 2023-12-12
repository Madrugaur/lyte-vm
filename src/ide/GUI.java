package ide;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.TextArea;
import java.util.Timer;

import javax.swing.JOptionPane;

import BreezyGUI.GBFrame;
import ide.async.SaveTimerTask;
import ide.file.FileManager;
import ide.file.Properties;
import ide.listeners.CodeAreaTextListener;
import ide.listeners.FileMenuActionListener;
import ide.listeners.RunMenuActionListener;
import ide.time.Time;
public class GUI extends GBFrame {
	public static GUI INSTANCE;
	private TextArea codeArea;
	private TextArea consoleArea;
	private Label savingLabel;
	private Label consoleLabel;
	private MenuBar menuBar;
	
	private Project current;
	public static void main(String[] args) {
		INSTANCE = new GUI();
	}
	public GUI() {
		this.setName("Lyte IDE");
		this.setTitle(this.getName());
		this.setSize(700,510);
		this.setVisible(true);
		this.setResizable(false);
		initElements();
		initSaveTimerTask();
		restoreWorkspace();
		this.validate();
	}
	private void initSaveTimerTask() {
		Timer timer = new Timer();
		SaveTimerTask task = new SaveTimerTask();
		timer.scheduleAtFixedRate(task, 1000, (1000 * 60));
	}
	private void initElements() {
		codeArea = addTextArea("", 1, 1, 7, 10);
		codeArea.setEnabled(false);
		codeArea.addTextListener(new CodeAreaTextListener());
		consoleLabel = addLabel("Console:", 11, 1, 1, 1);
		consoleArea = addTextArea("", 12, 1, 7, 3);
		consoleArea.setEditable(false);
		savingLabel = addLabel("Saving", 15, 1, 5, 5);
		
		initMenuBar();
	}
	private void initMenuBar() {
		/* figure out later */
		menuBar = new MenuBar();
		menuBar.setName("IDE Menu Bar");
		/* !- File Menu Item - ! */
		Menu fileMenu = new Menu("File");
		fileMenu.addActionListener(new FileMenuActionListener());
		fileMenu.add("New");
		fileMenu.add("Save");
		fileMenu.add("Open");
		/* !- Run Menu Item - ! */
		Menu runMenu = new Menu("Run");
		runMenu.addActionListener(new RunMenuActionListener());
		runMenu.add("Run");
		/* !- Add Menus - ! */
		menuBar.add(fileMenu);
		menuBar.add(runMenu);
		this.setMenuBar(menuBar);
	}
	public void setText(String text) {
		codeArea.setText(text);
	}
	public String getText() {
		return codeArea.getText();
	}
	private void restoreWorkspace() {
		String name = Properties.getProperty("project.previous");
		String code = "";
		if (!name.isBlank()) code = FileManager.read(FileManager.getProjectSubPath(name), FileManager.PROJECT_DIR);
		if (!code.isBlank()) {
			current = new Project(name, code);	
			updateWorkspaceToProject();
		} 
	}
	public void newProject() {
		String name = JOptionPane.showInputDialog(this,"New Project Name: ");
		if (name.isBlank()) return;
		current = new Project(name, "//Project Name: " + name);
		updateWorkspaceToProject();
		Properties.updateProperty("project.previous", name);
	}
	public void updateWorkspaceToProject() {
		codeArea.setEnabled(true);
		codeArea.setText(current.getCode());
	}
	public void saveChanges() {
		editSaveLabel("Saving...");
		current.setCode(codeArea.getText());
		current.save();
		Properties.save();
		editSaveLabel("Saved " + Time.getFormattedDateTime());
	}
	public void notifyContentChanged() {
		editSaveLabel("Unsaved");
	}
	private void editSaveLabel(String message) {
		savingLabel.setText(message);
		savingLabel.revalidate();
	}
	public Project getCurrentProject() {
		return current;
	}
	public TextArea getConsole() {
		return consoleArea;
	}

}
