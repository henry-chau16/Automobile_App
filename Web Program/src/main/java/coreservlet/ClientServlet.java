package coreservlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.servlet.http.HttpServlet;

//abstract class that implements all the necessary interfaces and provides client operations for the servlets
public abstract class ClientServlet extends HttpServlet implements Client{

	private static final long serialVersionUID = 1L;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected Socket sock;
	private String host = "localhost";
	private int port = 9998;
	
	public boolean openConnection() {
		try {
			sock = new Socket(host, port);
		}
		catch (IOException e) {
			sop("Error connecting to server");
			return false;
		}
			sop("Creating server object streams");
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
			return true;
		}
		catch (IOException e) {
			System.err.println("Error creating server object streams");
			return false;
		}
		
    }
    
    public void send(Object o) {
		try {
			out.writeObject(o);
		}
		catch (IOException e) {
			System.err.println("Error returning output to client");
		}
    }
    
    public static void sop(Object o) {
		System.out.println(o.toString());
	}


}
