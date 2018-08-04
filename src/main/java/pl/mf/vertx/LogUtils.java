package pl.mf.vertx;


import java.util.Date;

public class LogUtils {
	public static void printMessageWithDate(String message) {
		System.out.println("[" + new Date().toString() + "] " + message);
	}
}
