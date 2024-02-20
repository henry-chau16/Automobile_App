package server;
import java.util.Properties;

import adapter.*;

public class BuildCarModelOptions extends proxyAutomobile implements AutoServer{


	private static final int WAITING = 0;
	private static final int REQUEST_BUILD_AUTO = 1;
	private static final int REQUEST_CONFIGURE_AUTO = 2;

	private int state = WAITING;

	public BuildCarModelOptions() {

	}
	public static void sop(Object o) {
		System.out.println(o.toString());
	}

	public Object processRequest(Object obj) {
		Object toClient = null;

		if (state == REQUEST_BUILD_AUTO) { // 1
			
			if(buildAuto((Properties) obj)) { //attempts to build an automobile obj from the input object and returns true if succesfull
				toClient = "Automobile model successfully added to database\n"
						+ "Returning to main menu...\n";
			}
			else {
				toClient = "Error encountered while building Automobile";
			}

		}
		else if (state == REQUEST_CONFIGURE_AUTO) { // 2
			toClient = getAuto((String) obj); //returns a single automobile object to be serialized and sent to client
			
			if (toClient == null) {
				toClient = "Error Invalid Selection";
			}
		}
		else {

		}

		this.state = WAITING;

		return toClient;
	}

	public String setRequest(int i) {

		if (i == 1) {
			this.state = REQUEST_BUILD_AUTO;
			return "Upload a file to create an Automobile";
		}
		else if (i == 2) {
			this.state = REQUEST_CONFIGURE_AUTO;
			
			String test = "Select an Automobile from the following list to configure: \n" +
					super.getAllModels();
			sop(test);
			return test;
		}
		else {
			return "Invalid request";
		}
	}
	}

