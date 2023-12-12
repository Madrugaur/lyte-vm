package ide;

import java.io.IOException;

import ide.async.ConsoleReader;
import ide.file.FileManager;

public class RunManager {
	public static void run() {
		GUI.INSTANCE.getConsole().setText("");
		try
	    {
			Project project = GUI.INSTANCE.getCurrentProject();
			final Process p = Runtime.getRuntime().exec(String.format("java -jar %s %s","\"C:\\Program Files (x86)\\Lyte\\lyte-sdk\\lyte.jar\"" , FileManager.saveProject(project).getAbsolutePath()));
	        final ConsoleReader stderr = new ConsoleReader(p.getErrorStream(), "STDERR");
	        final ConsoleReader stdout = new ConsoleReader(p.getInputStream(), "STDOUT");
	        stderr.start();
	        stdout.start();
	        final int exitValue = p.waitFor();
	        GUI.INSTANCE.getConsole().append(stdout.toString());
	        GUI.INSTANCE.getConsole().append(stderr.toString());
	        if (p.isAlive()) {
	        	System.out.println("Killing process...");
	        	p.destroyForcibly();
	        }
	        
	    }
	    catch (final IOException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (final InterruptedException e)
	    {
	        throw new RuntimeException(e);
	    }
	}
}
