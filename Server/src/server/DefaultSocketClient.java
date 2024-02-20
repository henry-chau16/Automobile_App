package server;

import java.net.*;
import java.io.*;
import adapter.*;

public class DefaultSocketClient extends Thread implements SocketInterfaceConstants, SocketClientInterface {


	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket client;
	private BuildCarModelOptions builder;
	
	public DefaultSocketClient(Socket client, BuildCarModelOptions builder ) {
		this.client = client;
		this.builder = builder;
	}
	
	public void open(Socket client) {
		this.client = client;
	}
	
	//To shorten 'System.out.println'
	public static void sop(Object o) {
		System.out.println(o.toString());
	}
	//Serializes object o and sends to client
	public void send(Object o) {
		try {
			out.writeObject(o);
		}
		catch (IOException e) {
			System.err.println("Error sending data to client ... ");
			System.exit(1);
		}
	}
	//The actual socket is 'opened' in DefaultServerSocket class, this method sets up all the object streams
	public boolean openConnection() {
		if (DEBUG)
			sop("Creating server object streams ... ");
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			return true;
		}
		catch (IOException e) {
			System.err.println("Error creating server object streams ... ");
			System.exit(1);
			return false;
		}
		
		
	}
	//This method handles all interaction with the client, and calls delegate method to handle client requests
	public void handleSession() {
		Object check = null;
		String menu = "\nMain Menu: Please select one of the following:"
				+ "\n1. Upload a new Automobile model"
				+ "\n2. Configure an existing Automobile model"
				+ "\n0. Close Session";
		
		try {
			do {
				if (DEBUG)
					sop("Sending client main menu ... ");
				send(menu);

				if (DEBUG)
					sop("Processing client request ... ");
				int request = Integer.parseInt(in.readObject().toString());
				if (request == 0) {
					send("exit");
					break;
				}
				send(builder.setRequest(request));

				if (request >= 1 && request <= 2)
					delegate(request);

			} while ((check = in.readObject()) != null);
			if (DEBUG)
				sop("Closing input stream for client at" + client.getInetAddress());
			in.close();
		}
		catch (IOException e) {
			System.err.println("Error handling client connection ... ");
			//sop(check.toString()); testing purposes only
			System.exit(1);
		}
		catch (ClassNotFoundException e) {
			System.err.println("Error receiving object");
			System.exit(1);
		}	
	}
	//Handles client interaction based on which the input received by the client
	public void delegate(int request) {
		if (request == 1) {
			if (DEBUG)
				sop("Waiting for upload");
			try {
				send(builder.processRequest(in.readObject()));
				if(DEBUG)
					sop("Request received and fulfilled");
			}
			catch(ClassNotFoundException e){
				sop("Error reading upload -- potential data corruption");
				sop(e);
			}
			catch(IOException x) {
				sop(x);
			}
		}
		else if (request == 2) {
			if(DEBUG)
				sop("Waiting for client selection");
			try {
				send(builder.processRequest(in.readObject()));
				if(DEBUG)
					sop("Request received and fulfilled");
			}
			catch(ClassNotFoundException e){
				sop("Error reading upload -- potential data corruption");
				sop(e);
			}
			catch(IOException x) {
				sop(x);
			}
		}
		else {
			sop("Invalid Selection");
		}
	}
	//Manages closing all streams and sockets
	public void closeSession() {
		
		if (DEBUG)
			sop("Closing output stream for client at" + client.getInetAddress());
		try {
			out.close();
		}
		catch (IOException e) {
			System.err.println("Error encountered while closing output stream for " + client.getInetAddress());
		}
		if (DEBUG)
			sop("Closing client socket for  " + client.getInetAddress());
		try {
			client.close();
		}
		catch (IOException e) {
			System.err.println("Error closing client socket  for " + client.getInetAddress());
		}
	
	
	}
	//Overrides the run method from Thread. Calls openConnections, and if that returns true, calls the rest of the methods from SocketClientInterface
	@Override
	public void run() {
		if(openConnection()) {
			handleSession();
			closeSession();
		}	
	}
}