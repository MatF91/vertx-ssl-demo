package pl.mf.vertx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReaderUtil {
	public static String getProperty(String property) {
		String propertyValue = "";
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String fileName = "custom.properties";
			input = PropertiesReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
			prop.load(input);
			propertyValue = prop.getProperty(property);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		LogUtils.printMessageWithDate("Property: '" + property + "', value: " + propertyValue);
		return propertyValue;
	}
}
