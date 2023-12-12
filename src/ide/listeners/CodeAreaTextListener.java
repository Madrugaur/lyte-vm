package ide.listeners;

import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import ide.GUI;

public class CodeAreaTextListener implements TextListener {

	@Override
	public void textValueChanged(TextEvent e) {
		if (e.getID() == TextEvent.TEXT_VALUE_CHANGED) {
			GUI.INSTANCE.notifyContentChanged();
		}
		
	}

}
