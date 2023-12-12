package ide.file;

import java.util.HashMap;

public final class Properties {
	private static final HashMap<String, String> CONFIGS = new HashMap<String, String>();
	private Properties() {}
	static {
		String data = FileManager.read("config.txt", FileManager.APPLICATION_DIR);
		String[] lines = data.split("\n");
		for (String line : lines) {
			String[] parts = line.split("=");
			if (parts.length == 2) CONFIGS.put(parts[0], parts[1]);
			else CONFIGS.put(parts[0], "");
		}
	}
	public static String getProperty(String key) {
		if (CONFIGS.containsKey(key)) return CONFIGS.get(key);
		return null;
	}
	public static void updateProperty(String key, String value) {
		if (CONFIGS.containsKey(key)) CONFIGS.replace(key, value);
	}
	private static String getMapAsString() {
		String r = "";
		for (String key : CONFIGS.keySet()) {
			r += key + "=" + CONFIGS.get(key);
		}
		return r;
	}
	public static void save() {
		FileManager.saveFile("config.txt", FileManager.APPLICATION_DIR, getMapAsString());
	}
}
