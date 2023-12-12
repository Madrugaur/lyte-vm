package ide;

import ide.file.FileManager;

public class Project {
	private String code;
	private String name;
	public Project(String name, String code) {
		this.name = name;
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void save() {
		FileManager.saveProject(name, code);
	}

}
