package ide.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Time {
	private Time() {}
	public static String getFormattedDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
	}
}
