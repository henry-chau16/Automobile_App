package driver;
import server.*;
public class Driver {
	public static void main(String[] args) {
		//driver for Server. Creates a Server socket and runs it on its own thread
		DefaultServerSocket server1 = new DefaultServerSocket(9998);
		server1.start();
}
}
