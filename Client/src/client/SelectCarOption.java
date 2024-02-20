package client;
import model.Automobile;

import java.io.BufferedReader;
import java.io.IOException;

import adapter.*;

public class SelectCarOption extends proxyAutomobile implements ChooseAuto{
	
public static void sop(Object o) {
		System.out.println(o.toString());
	}

//handles instanced car configuration. Attempts to cast the object received to an Automobile object and prints the choices
public void receiveInstance(Object o, BufferedReader userInput) throws IOException {
	try {
		Automobile a = (Automobile) o;
		addModel(a);
		a.print();
		configureCar(a.getMake()+" "+a.getModel(), userInput);
		sop("Configuration successful\nReturning to main menu...\n");
	}
	catch(ClassCastException e) {
		sop(o.toString());
		sop("Configuration failed\nExiting to main menu...\n");
	}
}
//manages user input and choice to configure an automobile object
public void configureCar(String key, BufferedReader userInput) throws IOException {
	boolean done = false;
	String optType;
	String optChoice;
	while(!done) {
		sop("\nPlease Select an Option Type\n>");
		optType = userInput.readLine();
		sop("\nPlease Select an Option choice\n>");
		optChoice = userInput.readLine();
		if(setOptionChoice(key, optType, optChoice)) {
			sop("Choice logged... continue?(enter 'n' to quit)");
		}
		else {
			sop("Invalid selection... continue?(enter 'n' to quit)");
		}
		String entry = userInput.readLine();
		if(entry.equals("n") || entry.equals("N")) {
			sop("Printing current car configuration for "+ key);
			printChoices(key);
			done = true;
		}
	}
}
}
