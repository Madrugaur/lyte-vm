package ide.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ide.Project;

public class FileManager{
	public static final Path APPLICATION_DIR = Paths.get(System.getProperty("user.home"), File.separator, "LyteIDE");
	public static final Path PROJECT_DIR = Paths.get(APPLICATION_DIR.toString(), File.separator, "projects");
	public static final String EXTENSION = ".lyte";
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);
	public static File saveProject(final Project project) {
		return saveProject(project.getName(), project.getCode());
	}
	public static File saveProject(final String name, final String text) {
		Future<File> saveProjectThread = EXECUTOR.submit(new Callable<File>() {
			@Override
			public File call() throws Exception {
				String current_project_path = PROJECT_DIR.toString() + File.separator + name;
				File project_dir = new File(current_project_path);
				if (!project_dir.exists()) project_dir.mkdirs();
				File file = new File(current_project_path, name + EXTENSION);
				try {
					if (!file.exists()) file.createNewFile();
					Files.writeString(file.toPath(), text);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return file;
			}
		});
		try {
			return saveProjectThread.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void saveFile(final String name, final Path path, final String data) {
		Thread saveFileThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Files.writeString(Paths.get(path.toString(), File.separator, name), data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		});
		saveFileThread.start();
	}
	public static String read(final String name, final Path dir_path) {	
		Future<String> readFuture = EXECUTOR.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Path file_path = Paths.get(dir_path.toString() + File.separator + name);
				return Files.readString(file_path);
			}	
		});
		try {
			return readFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	public static String getProjectSubPath(String name) {
		return getProjectSubPath(name, EXTENSION);
	}
	public static String getProjectSubPath(String name, String ext) {
		return name + File.separator + name + ext;
	}
	
}
