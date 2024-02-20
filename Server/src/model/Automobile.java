package model;
import java.io.Serializable;
import java.util.ArrayList;

import exception.AutoException;
import exception.Error;

//Refactored to support user selections via choice variable, and use ArrayLists to store records
public class Automobile implements Serializable{ //Class that contains an ArrayList of Option Sets, representative of any particular car model

  private static final long serialVersionUID = 16L; //unique class id value for the Serializable interface
  private float basecost;
  private ArrayList<OptionSet> arr;
  private OptionSet choice;
  
  private String make;
  private String model;
  
  
  public Automobile(){
	  
    basecost = 0;
    make = "none";
    model = "none";
    choice = new OptionSet();
    choice.setType("Choice");
    arr = new ArrayList<OptionSet>();
    arr.ensureCapacity(10);
  }
  
  public Automobile(String make, String model, int capacity, float basecost){
    
    this.make = make;
    this.model = model;
    this.basecost = basecost;
    choice = new OptionSet();
    choice.setType("Choice");
    arr = new ArrayList<OptionSet>();
   	arr.ensureCapacity(capacity);
    
  }
  
//-------------------------ACCESSORS-------------------------------
  
  public synchronized String getMake(){
    return make;
  }
  
  public synchronized String getModel(){
	    return model;
	  }
  
  public synchronized int getSize(){
    return arr.size();
  }
  
  public synchronized float getBaseCost(){
    return basecost;
  }
  
  public synchronized OptionSet getOptSet(int index) throws IndexOutOfBoundsException{
    return arr.get(index);
  }
  
//--------------------------FIND----------------------------------------
  
  public int findSet(String setType) throws AutoException {
    int cursor = -99;
    
    if(arr.size()>0) {
   	  for (int i = 0; i<arr.size(); i++){
   		
        if(arr.get(i).getType().equals(setType)){
          cursor = i;
        }
      }
    }
    else {
    	throw new AutoException(Error.EMPTY_AUTOSET);
    }
    if(cursor>=0) {
    	return cursor;
    }
    else {
    	
    	throw new AutoException(Error.OPTSET_NOT_FOUND);
    }
    
  }

  public int findOption(String setName, String optionData) throws AutoException{ 
	if (arr.size()>0) {
	  
	  int cursor = findSet(setName);
	  return arr.get(cursor).find(optionData);
	  
	}
	else {
		throw new AutoException(Error.EMPTY_AUTOSET);
	}
    
    
  }
//------------------------MUTATORS-----------------------------------  
  
  public synchronized void setMake(String make){
    this.make = make;
  }
  
  public synchronized void setModel(String model){
	    this.model = model;
	  }
  
  public synchronized void ensureCapacity(int capacity){
    arr.ensureCapacity(capacity);
  }
  
  public synchronized void setBasecost(float basecost){
    this.basecost = basecost;
  }
  
  public synchronized void addSet(String setName, int capacity) throws AutoException{
    
      OptionSet optSet = new OptionSet(setName, capacity);
      if(isDup(optSet)){
    	  throw new AutoException(Error.DUPLICATE_OPTSETNAME);
      }
      else{
        arr.add(optSet);
      }

  }
  
  public synchronized void removeSet(String setName) throws AutoException { 
	
	  int cursor = findSet(setName);
      arr.remove(cursor);
      
   	}
	

  public synchronized void addOption(String setType, String data, float cost) throws AutoException{
	
      int cursor = findSet(setType);
	  arr.get(cursor).addOption(data, cost);
	
  }

  public synchronized void removeOption(String setType, String type) throws AutoException{
	  
	
	    int cursor = findSet(setType);
	    arr.get(cursor).removeOption(type);
	    
  }
  
  public synchronized void updateSet(String setType, String newType, int newCapacity) throws AutoException {// Updates all properties of OptionSet (resizing creates new array of blank Options)
	
	  int cursor = findSet(setType);
	  OptionSet optSet = new OptionSet(newType, newCapacity);
	  if(isDup(optSet)){
		  if(arr.get(cursor).getType().equals(newType)) {
			  arr.set(cursor, optSet);
		  }
		  else {
	      throw new AutoException(Error.DUPLICATE_OPTSETNAME);
	  
		  }
	  }  
	  else {
	      arr.set(cursor, optSet);   
	    } 

  }

  public synchronized void updateSet(String setType, String newType) throws AutoException {// Overload of updateSet to only modify name of Option Set
	 
		  int cursor = findSet(setType);
		  OptionSet optSet = new OptionSet();
		  optSet.setType(newType);
		  if(isDup(optSet)){
			  if(arr.get(cursor).getType().equals(newType)) {
				  //new name is identical to original name: nothing changes
			  }
			  else {
				  throw new AutoException(Error.DUPLICATE_OPTSETNAME);
			  }
		      
		  }
		  else {
			  arr.get(cursor).setType(newType);   
		    }
	  
	  
  }

  public synchronized void updateOption(String setType, String optionData, String newData, float newCost) throws AutoException {
	  
	  	int cursor = findSet(setType);
		arr.get(cursor).updateOption(optionData, newData, newCost);
	  
  }

  public synchronized void updateOption(String setType, String optionData, float newCost) throws AutoException{
	  
	  int cursor = findSet(setType);
	  arr.get(cursor).updateOption(optionData, newCost);
	  
  } 

  //----------------------HELPERS----------------------------------------
  
  protected boolean isDup(OptionSet optSet){ //Helper method used in methods that alter or add Option Sets to check if they have duplicate names(see note at top)
    boolean check = false;
    for (int i = 0; i < arr.size(); i++){
      if(arr.get(i).getType().equals(optSet.getType())){
        check = true;
      }
    }
    return check;
  }
  
  public void print(){
	System.out.println("Make: "+make);
    System.out.println("Model: "+model);
    System.out.println("Number of Option Sets: "+arr.size());
    System.out.print("Base Cost: $");
    System.out.printf("%.2f", basecost);
    System.out.println("\n");
    
    for (int i = 0; i<arr.size(); i++){
      System.out.print((i+1)+ ". ");
      
      if (!arr.get(i).getType().equals("None")) {
        arr.get(i).print();
        System.out.println("\n");
      }
      else {
    	  System.out.println("Vacant Set slot\n");
      }
    }
    
  }
  
  public void printChoice() {
	  System.out.println("Make: "+make);
	  System.out.println("Model: "+model);
	  System.out.println("Selected Options: \n");
	  for (int i = 0; i<arr.size(); i++) { 
		System.out.println(arr.get(i).getType()+":\n");
		arr.get(i).printChoice();
		
	  }
	  System.out.print("Total Cost: $");
	  System.out.println(getTotalCost());
  }

//------------------------CHOICE METHODS----------------------------------------------
  
  public void setOptionChoice(String setType, String optionData) throws AutoException {
	  
	  int cursor = findSet(setType);
	  int cursor2 = findOption(setType, optionData);
	  
	  if (arr.get(cursor).noChoice()) { //Check if choice exists: return true is choice is null for that Option Set
		  arr.get(cursor).setChoice(arr.get(cursor).get(cursor2));
	      choice.addOption(arr.get(cursor).get(cursor2));
	  }
	  else {
		  choice.removeOption(arr.get(cursor).getChoiceString()); //Overrides existing choice if a choice is passed for an Option choice that already has a choice selected
		  arr.get(cursor).setChoice(arr.get(cursor).get(cursor2));
	      choice.addOption(arr.get(cursor).get(cursor2));
	  }
	  
	  
  }
  
  public String getOptionChoice(String setType) throws AutoException{
	  return arr.get(findSet(setType)).getChoiceString();
  }
  
  public float getOptionChoicePrice(String setType) throws AutoException{
	  return arr.get(findSet(setType)).getChoicePrice();
  }
  
  public float getTotalCost() {
	  float cost = 0;
	  for (int i = 0; i<choice.size();i++) {
		  cost+=choice.get(i).getCost();
	  }
	  return cost+basecost;
  }
}





