package adapter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exception.AutoException;
import scale.*;
import model.*;
import util.*;


public abstract class proxyAutomobile {
	private static LinkedHashMap<String, Automobile> car;
	Logger log = Logger.getLogger("Logfile");
	FileHandler fh;
	
	public proxyAutomobile() {
		car = new LinkedHashMap<String, Automobile>();
		try {
		fh = new FileHandler("Logfile2.txt", true); //For some reason the parameter boolean append = true does not work: The logger still creates a separate file instead of appending to the existing one
		log.addHandler(fh);
		log.setUseParentHandlers(false);
		SimpleFormatter format = new SimpleFormatter();
		fh.setFormatter(format);
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
		
	public boolean buildAuto(String filename, String fileType) {
		
		AutoReader reader = new AutoReader();
		Automobile auto = new Automobile();
		if(fileType.equals("TXT")) {
			try {
				auto = reader.readFile(filename);
				addModel(auto);
				return true;
			}
			catch(IOException e) {
				System.out.println("Error "+e);
				log.info(e.toString());
				return false;
			}
		}
		else if (fileType.equals("PROP")){
			try {
				auto = reader.readProperties(filename);
				addModel(auto);
				return true;
			}
			catch(IOException e) {
				System.out.println("Error "+e);
				log.info(e.toString());
				return false;
			}
			catch(AutoException a) {
				System.out.println("\n-------PARSING ERROR------------");
				a.print();
				log.info(a.getErrMsg());
				return false;
			}
		
		}
		else {
			return false;
		}
		
	}
	
	public boolean buildAuto(Properties prop) {
		AutoReader reader = new AutoReader();
		Automobile auto = new Automobile();
		try {
			auto = reader.convertProp(prop);
			addModel(auto);
			printAuto((auto.getMake()+" "+auto.getModel()));
			return true;
		}
		catch(AutoException a) {
			System.out.println("\n-------PARSING ERROR------------");
			a.print();
			log.info(a.getErrMsg());
			return false;
		}
	}
	
	public synchronized void addModel(Automobile auto) {
		car.put(auto.getMake()+" "+auto.getModel(), auto);
	}
	
	public synchronized String getAllModels() {
		String modelList = "";
		Iterator<String> iter = car.keySet().iterator();
		for (int i = 0;i<car.keySet().size();i++) {
			modelList+=iter.next()+"\n";
		}
		return modelList;
	}
	
	public synchronized Automobile getAuto(String key) {
		if(car.containsKey(key)) {
			return car.get(key);
		}
		else {
			return null;
		}
	}
	
	public void print() {
		Iterator<String> iter = car.keySet().iterator();
		for (int i = 0;i<car.keySet().size();i++) {
			car.get(iter.next()).print();
		}
	}
	
	public synchronized void printAuto(String key) {
		
		if(car.containsKey(key)) {
			
			System.out.println("\n-------------------"+key+" Full Printout-------------------------------\n");
			car.get(key).print();
			System.out.println("\n------End------");
		}
		else {
			System.out.println("No Records found for: "+key);
		}
	}
	
	public synchronized void updateOptionSetType(String key, String optionType, String newType) {
		if(car.containsKey(key)) {
			try {
				car.get(key).updateSet(optionType, newType);
			}
			catch(AutoException e) {
				System.out.println("\n-------UPDATE ERROR------------");
				e.print();
				log.info(e.getErrMsg());
			
			}
		}
		else {
			System.out.println("No Records found for: "+key);
		}
		
	}
	
	public synchronized void updateOptionPrice(String key, String optionType, String data, float newprice) {
		if(car.containsKey(key)) {
			try {
				  car.get(key).updateOption(optionType, data, newprice);
				}
				catch(AutoException e) {
					System.out.println("\n-------UPDATE ERROR------------");
					e.print();
					log.info(e.getErrMsg());
					
				}
		}
		else {
			System.out.println("No Records found for: "+key);
		}
		
		
	}

	public boolean setOptionChoice(String key, String optionType, String data) {
		if(car.containsKey(key)) {
			try {
				car.get(key).setOptionChoice(optionType, data);
				return true;
			}
			catch(AutoException e) {
				System.out.println("\n-------SELECTION ERROR------------");
				e.print();
				log.info(e.getErrMsg());
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public synchronized void printChoices(String key) {
		if(car.containsKey(key)) {
			System.out.println("\n-------------------"+key+" Selections Printout-------------------------------\n");
			car.get(key).printChoice();
			System.out.println("\n------End------");
		}
		else {
			System.out.println("No Records found for: "+key);
		}
	}

	public void editOpt(String key, String optionType, String data, float newprice) {
		EditOptions editor = new EditOptions(this, key, optionType, data, newprice);
		
		editor.start();
		
		
	}
}
