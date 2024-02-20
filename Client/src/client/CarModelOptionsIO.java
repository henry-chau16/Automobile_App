package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CarModelOptionsIO {

	//creates and returns a properties object from the provided path to a properties file
	public Properties createProp(String filePath) throws IOException {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream(filePath);
		prop.load(in);
		return prop;
	}
}
