package util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import exception.AutoException;
import exception.Error;
import model.Automobile;

public class AutoReader { 
	// Constants below are created to define and enforce .txt file format. Format was designed to support .csv files and potentially imported .xls files as well
	// Modified for version 3 by changing requisite file format, with a unique line identifier '>' for setlines
	  
	  private final int DEFLINE_ARGS = 4; // max args in Header line of text file
	  private final int SETLINE_ARGS = 2; // max args in Option Set lines of text file
	  private final int OPTLINE_ARGS = 2; // max args in Option lines of text file
	  
	  private final int DEF_MAKE_IND = 0; //<--
	  private final int DEF_MODEL_IND = 1;
	  private final int DEF_SIZE_IND = 2; //  |
	  private final int DEF_COST_IND = 3; //  |
	  									  //  |
	  private final int SET_TYPE_IND = 0; //  |---*These are constants that refer to the appropriate positioning of Automotive/OptionSet/Option object properties 
	  private final int SET_SIZE_IND = 1; //  |--- as arguments in their respective lines in the text file    
	  									  //  |
	  private final int OPT_DATA_IND = 0; //  |
	  private final int OPT_COST_IND = 1; //<--
	  
	  private final int SETLINE_MARK = 0; // location of Set Line indicator '>'; each setline should start with '>'
	  private final int SETLINE_START = 1;
	  private final char SETLINE_CHAR = '>';
	  
	  public AutoReader(){
	   
	  }
	  
	  protected void readDefline(String line, Automobile car) throws AutoException{  // Checks the Header line of the text file for proper format
	    
	      if(line == null){
	        throw new AutoException(Error.MISSING_HEADER);
	      }
	      else if(line.split(", ").length == DEFLINE_ARGS){
	    	 try {
	    		 car.setMake(line.split(", ")[DEF_MAKE_IND]);
	   	         car.setModel(line.split(", ")[DEF_MODEL_IND]);
	   	         Integer.parseInt(line.split(", ")[DEF_SIZE_IND]);
	   	         car.setBasecost(Float.parseFloat(line.split(", ")[DEF_COST_IND]));
	    		 
	    	 }
	    	 catch (NumberFormatException e) {
	    		 throw new AutoException(Error.BAD_HEADER);
	    	 }
	        
	      }
	      else{

	    	  throw new AutoException(Error.BAD_HEADER);
	      }
	    
	  }

	  protected void readSetline(String line, Automobile car) throws AutoException{  // Checks the Option Set lines of the text file for proper format
	    
	      if(line == null){
	    	
	    	  throw new AutoException(Error.MISSING_SETLINE);
	      }
	      else if(line.split(", ").length == SETLINE_ARGS){
	    	  
	    	if(line.split(", ")[SET_TYPE_IND].charAt(SETLINE_MARK)!= SETLINE_CHAR) {
	    	
	    		throw new AutoException(Error.BAD_SETLINE);
	    	}
	    	try {
	    		car.addSet(line.split(", ")[SET_TYPE_IND].substring(SETLINE_START), Integer.parseInt(line.split(", ")[SET_SIZE_IND]));
	    		
	    	}
	    	catch(NumberFormatException e){
	    		
	    		throw new AutoException(Error.BAD_SETLINE);
	    		
	    	}
	        
	      }
	      else{
	    	
	    	  throw new AutoException(Error.BAD_SETLINE);
	      }
	    
	  }

	  protected void readOptline(String line, String setline, Automobile car) throws AutoException{  // Checks the Option lines of the text file for proper format
	   
	      if(line == null){
	        
	    	  throw new AutoException(Error.MISSING_OPTLINE);
	      }
	      if(line.split(", ").length == OPTLINE_ARGS){
	        try {
	        	car.addOption(setline.split(", ")[SET_TYPE_IND].substring(SETLINE_START), line.split(", ")[OPT_DATA_IND], Float.parseFloat(line.split(", ")[OPT_COST_IND]));

	        }
	        catch(NumberFormatException e){
	        	throw new AutoException(Error.BAD_OPTLINE);
	        }
	      }
	      else{
	    	  
	    	  throw new AutoException(Error.BAD_OPTLINE);
	      }
	    
	  }
	  
	  public Automobile readFile(String filename) throws IOException{  //reads a text file to populate an Automotive object that is returned
	    
		Logger log = Logger.getLogger("Logfile");
		FileHandler fh = new FileHandler("Logfile.txt", true);
		log.addHandler(fh);
		log.setUseParentHandlers(false);
		SimpleFormatter format = new SimpleFormatter();
		fh.setFormatter(format);
		log.info(filename+" fileread started");
		
		FileReader fr = new FileReader(filename);
	    BufferedReader br = new BufferedReader(fr);
	    Automobile car = new Automobile();
	    int setSize;
	    int optSetSize;
	    String checkNextLine = "None";
	    boolean nextSet = false;
	    boolean done = false;
	    boolean setCheck = false;
	    int linecount=0;
	    
	    String defline = br.readLine();
	    
	    try {
	      linecount+=1;
	      readDefline(defline, car);      // Checks the Header (first line of the text file) Does not continue if Header is missing or has the wrong format
	    }
	    catch(AutoException e) {
	    	e.print();
	    	log.info(e.getErrMsg());
	    	System.out.println("File read failed");
	    	done = true;
	    }
	    finally {  
	      while(!done){  // Parses through Layers of Option Set lines and Option lines to populate the Automotive object. Stops if a line is missing or has the wrong format
	    	
	    	setSize = Integer.parseInt(defline.split(", ")[DEF_SIZE_IND]);
	        for (int i = 0; i<setSize; i++){
	          String setline = "None, 0";
	          if (nextSet) {
	        	  while(nextSet) {
	            		checkNextLine = br.readLine();
	            		if(checkNextLine!= null) {
	            		   linecount+=1;
	            		   if(checkNextLine.split(", ")[SET_TYPE_IND].charAt(0)=='>') {
	            			   setline = checkNextLine;
	            			   nextSet = false;
	            		   }
	            		}
	            	}
	          }
	          else {
	        	  setline = br.readLine();
	        	  linecount+=1;
	          }
	          try{
	        	   readSetline(setline, car);
	        	   setCheck = true;

	        	   
	          }
	          catch(AutoException e){
	        	  
	        	    System.out.println("----------READFILE ERROR-----------------\n");
	        	  	System.out.println("> At line "+linecount+": '"+setline+"'");
	        	    e.print();
	        	    log.info(e.getErrMsg());
	        	    if(e.getErrNo()==2||e.getErrNo()==3||e.getErrNo()==100||e.getErrNo()==101) {
	            		System.out.println("Faulty/Missing Option Set omitted\n");
	            		nextSet = true;
	            	
	        	    }
	        	    else {
	                done = true;
	        	    }
	              }
	          finally {
	            while(setCheck) {
	            optSetSize = Integer.parseInt(setline.split(", ")[SET_SIZE_IND]);
	            for (int j = 0; j<optSetSize; j++){
	                  String optline = br.readLine();
	                  linecount+=1;
	              
	                  try {
	            	  readOptline(optline, setline, car);
	          
	                  }
	                  catch(AutoException e){
	                	  
	                	  System.out.println("----------READFILE ERROR-----------------\n");
	                	  System.out.println("> At line "+linecount+": '"+optline+"'");
	                	  e.print();
	                	  log.info(e.getErrMsg());
	                	  if(e.getErrNo()==4||e.getErrNo()==5||e.getErrNo()==104||e.getErrNo()==105) {
	                		  System.out.println("Faulty/Missing Option entry omitted\n");
	                	  }
	                	  else {
	                		  done = true;
	                	  }
	            	
	                  }
	                  
	            }
	              setCheck = false;
	            }
	              }      
	          }
	        done = true;
	        }
	    br.close();
	    fr.close();
	    }
	    return car;
	    }
	  
	  public Automobile readProperties(String filename) throws IOException, AutoException{
		  
		  Properties prop = new Properties();
		  FileInputStream in = new FileInputStream(filename);
		  prop.load(in);
		  
		  return convertProp(prop);
		  
	  }
	  
	  public Automobile convertProp(Properties prop) throws AutoException {
		  Automobile car = new Automobile();
		  car.setMake(prop.getProperty("CarMake"));
		  if(!car.getMake().equals(null)) {
			  car.setModel(prop.getProperty("CarModel"));
			  car.setBasecost(Float.parseFloat(prop.getProperty("BaseCost")));
			  try {
				  int setSize = Integer.parseInt(prop.getProperty("SetTotal"));
				  for (int i=0; i<setSize;i++) {
					  int count = i+1;
					  String set = prop.getProperty("Set"+count);
					  int capacity = Integer.parseInt(prop.getProperty("Set"+count+"Size"));
					  car.addSet(set, capacity);
					  for(int j=0; j<capacity;j++) {
						  int optCount = j+1;
						  String opt = prop.getProperty("Option"+count+"-"+optCount);
						  float cost = Float.parseFloat(prop.getProperty("Option"+count+"-"+optCount+"Cost"));
						  System.out.println(set+" "+opt+" "+cost);
						  car.addOption(set, opt, cost);
					  }
				  }
			  }
			  catch(NumberFormatException e) {
				  throw new AutoException(Error.BAD_PROP);
			  }

		  }
		  else
		  {
			  throw new AutoException(Error.BAD_PROP);
		  }
		  
		  return car;
	  }

	  public void serializeAuto(Automobile car, String filename) throws IOException{  //Serializes an Automotive object into a file with provided name (filename)
		   
          ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename)); 
          out.writeObject(car);
          
          out.close();
          System.out.println("\n-----Automotive model serialized-------\n");
	  }
	  
	  public Automobile deserializeAuto(String filename) throws IOException, ClassNotFoundException{ //De-serializes a file into an Automotive object and returns the object
		  
		  Automobile car = new Automobile();
		  ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
		  car = (Automobile)in.readObject();
		  
		  in.close();
		  System.out.println("\n-----Automotive model deserialized-----\n");
		  
		  return car;
		  
	  }
	  
	 
	}
