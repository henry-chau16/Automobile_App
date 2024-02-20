package model;
import java.io.Serializable;

class Option<T> implements Serializable{//Template class for an option
   
	private static final long serialVersionUID = 12L; //unique class id value for the Serializable interface
    private T data;
    private float cost;
    
    protected Option(){
      data = null;
      cost = 0;
    }
    
    protected Option(T data, float cost){
      
      this.data = data;
      this.cost = cost;
      
    }
    
    protected void setData(T data){
      this.data = data;
    }
    
    protected void setCost(float cost){
      this.cost = cost;
    }
    
    protected T getData(){
      return data;
    }
    
    protected String getDataString() {
    	return data.toString();
    }
    
    protected float getCost(){
      return cost;
    }
    
    
    protected boolean compareData(Option<?> opt){ // (See note at top of Automotive)
      if(data.equals(opt.getData())){
    	  return true;
      }
      else {
    	  return false;
      }
    }
    
    protected void print(){
      System.out.print("\t"+data+"\n\tCost: $");
      System.out.printf("%.2f", cost);
      System.out.println("\n");
    }
  
  }