package server;
import model.Automobile;

import java.util.Properties;

public interface AutoServer {

	
	public boolean buildAuto(Properties prop);
	public Automobile getAuto(String key);
	public String getAllModels();
}
