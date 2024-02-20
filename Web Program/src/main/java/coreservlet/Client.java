package coreservlet;


//interface for client operations
public interface Client {

	public boolean openConnection();
	public void send(Object o);
}
