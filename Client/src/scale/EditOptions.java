package scale;
import adapter.*;

public class EditOptions extends Thread {

	private proxyAutomobile cars;
	private String model;
	private String optType;
	private String data;
	private float newprice;
	
	public EditOptions(proxyAutomobile cars, String model, String optType, String data, float newprice) {
		this.cars = cars;
		this.model = model;
		this.optType = optType;
		this.data = data;
		this.newprice = newprice;
	}
	
	public void run() {
		
		try{
			cars.updateOptionPrice(model, optType, data, newprice);
			sleep(1000);
		}
		catch(InterruptedException x) {
			System.out.println(x);
		}
	
		
		
	}
}
