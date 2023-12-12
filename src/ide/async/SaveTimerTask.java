package ide.async;

import java.util.TimerTask;

import ide.GUI;

public class SaveTimerTask extends TimerTask {

	@Override
	public void run() {
		GUI.INSTANCE.saveChanges();
	}

}
