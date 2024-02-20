package model;

import java.io.Serializable;
import java.util.ArrayList;

import exception.AutoException;
import exception.Error;


class OptionSet extends ArrayList<Option<?>> implements Serializable{//Class that contains an array of Options

  private static final long serialVersionUID = 62L; //unique class id value for the Serializable interface
  private String type;
  private Option<?> choice;
  
  protected OptionSet(){
    type = "None";
    ensureCapacity(10);
    choice = null;
  }
  
  protected OptionSet(String type, int capacity){
    
	this.type = type;
    ensureCapacity(capacity);
    choice = null;
    }
  
  protected String getType(){
    return type;
  }

  //------------MUTATORS-----------------------------------------------
  
  protected void setType(String type){
    this.type = type;
  }
  
  protected void addOption(Option<?> opt) throws AutoException{
		
		if(!isDup(opt)) {
			add(opt);
		}
		else {
			throw new AutoException(Error.DUPLICATE_OPTION);
		}
	  }
  
  protected void addOption(String data, float cost) throws AutoException{
		
	  Option<String> opt = new Option<String>(data, cost);
	  if(!isDup(opt)) {
			add(opt);
		}
		else {
			throw new AutoException(Error.DUPLICATE_OPTION);
		}
	  }
	  
  protected void removeOption(String data) throws AutoException{//Code is implemented to prevent gaps of unfilled records in between filled Options
	    Option<?> opt = get(find(data));
	    remove(opt);	    
	  }
	  
  protected void updateOption(String data, String newdata, float newcost)throws AutoException {
	  
	  		Option<String> newOpt = new Option<String>(newdata, newcost);
	  		if(isDup(newOpt)){
	  			
	  			if(newOpt.getData().equals(data)) {
	  				set(find(data), newOpt);
	  			}
	  			else{
	  				throw new AutoException(Error.DUPLICATE_OPTION);
	  			}
		      }
		      else{
		        set(find(data), newOpt);
		      }
	  }
  
  protected void updateOption(String data, float newCost) throws AutoException{
	 get(find(data)).setCost(newCost);
  }

//----------------------------FIND & OTHER HELPERS---------------------------------
 
  protected int find(String data) throws AutoException{
    int cursor = -99;
    if(size() == 0) {
    	throw new AutoException(Error.EMPTY_OPTSET);
    }
    for (int i = 0; i < size(); i++){
      if(get(i).getDataString().equals(data)){
        cursor = i;
      }
    }
    if(cursor>=0) {
    	return cursor;
    }
    else {
    	throw new AutoException(Error.OPTION_NOT_FOUND);
    }  
  }
 
  protected void print(){
    System.out.println("Set Type: "+type+"\n   Number of options: "+size());
    System.out.println();
    for (int i = 0; i<size(); i++){
      if(get(i).getData() != null) {
    	get(i).print();
      }
      else {
    	  System.out.println("\tVacant Option slot\n");
      }
    }
  }
  
  protected boolean isDup(Option<?> opt){ //Helper method used in methods that alter or add Options to check if they have duplicate types (see note at top of Automotive)
    boolean check = false;
    for (int i = 0; i < size(); i++){
      if(get(i).compareData(opt)){
        check = true;
      }
    }
    return check;
  }

//----------------CHOICE METHODS---------------------------------------------
  
  protected void setChoice(Option<?> opt) {
	  choice = opt;
  }
  
  protected String getChoiceString() throws AutoException {
	  if (choice == null) {
		  throw new AutoException(Error.CHOICE_NOT_SET);
	  }
	  return choice.getDataString();
  }
  
  protected float getChoicePrice() throws AutoException{
	  if (choice == null) {
		  throw new AutoException(Error.CHOICE_NOT_SET);
	  }
	  return choice.getCost();
  }
  
  protected boolean noChoice() {
	  if(choice == null) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  
  protected void printChoice() {
	  if(choice == null) {
		  System.out.println("\t*No Selection for this Option Type\n");
	  }
	  else {
		  choice.print();
	  }
  }
	  
}
