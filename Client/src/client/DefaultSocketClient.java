package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import adapter.*;

public class DefaultSocketClient extends Thread implements  SocketClientInterface, SocketInterfaceConstants{

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket sock;
	private String host;
	private int port;
	
	private CarModelOptionsIO uploader;
	private SelectCarOption configure;
	private BufferedReader userInput;

	
	public DefaultSocketClient(String host, int port) {
		
		this.host = host;
		this.port = port;
		
	}
	//Don't want to type System.out.println a hundred times
	public static void sop(Object o) {
		System.out.println(o.toString());
	}
	//serializes Object o and sends it through the output stream to the server
	public void send(Object o) {
		try {
			out.writeObject(o);
		}
		catch (IOException e) {
			System.err.println("Error returning output to client");
			System.exit(1);
		}
	}
	//handles client side operation for each choice that doesn't terminate the session (1-2)
	public void delegate(int choice) throws IOException, ClassNotFoundException {
		if(choice == 1) {
			sop("Please input full path to Properties File\n>");
			String path = userInput.readLine();
			if(DEBUG)
				sop("Creating properties object\n");
			Properties prop = uploader.createProp(path); //creates a properties object to send to server
			if(DEBUG)
				sop("Sending properties object\n");
			send(prop);
			if(DEBUG)
				sop("Waiting for server response\n");
			sop(in.readObject().toString());
			send(" ");
		}
		else if (choice == 2) {
			sop("Please input your selection (warning: case sensitive)\n>");
			String selection = userInput.readLine();
			if(DEBUG)
				sop("Sending selection to server\n");
			send(selection);
			if(DEBUG)
				sop("Waiting for server response\n");
			configure.receiveInstance(in.readObject(), userInput); //calls on SelectCarOption class to handle car config
			send(" ");
		}
		else {
			sop("Returning to main menu\n");
			send(" ");
		}
		
		
	}
	//'opens' socket connection and sets up object streams
	public boolean openConnection() {
		try {
			sock = new Socket(host, port);
		}
		catch (IOException e) {
			if(DEBUG)
				sop("Error connecting to server");
			return false;
		}
		if (DEBUG)
			sop("Creating server object streams");
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
			return true;
		}
		catch (IOException e) {
			System.err.println("Error creating server object streams");
			System.exit(1);
			return false;
		}
	}
	// main process loop handles menu selection, and calls delegate to handle choices
	public void handleSession() {
		uploader = new CarModelOptionsIO();
		configure = new SelectCarOption();
		userInput = new BufferedReader (new InputStreamReader(System.in));
		String menu;
		
		if(DEBUG)
			sop("Waiting for Server to provide menu\n");
		try {
			
			while((menu = in.readObject().toString())!=null) {
				
				sop(menu);
				if(DEBUG)
					sop("Waiting for user selection\n");
				int choice = Integer.parseInt(userInput.readLine());
				if(DEBUG)
					sop("Sending user selection\n");
				send(choice);
				if(DEBUG)
					sop("Waiting for server response\n");
				String response = in.readObject().toString();
				if(response.equals("exit")) {
					sop("Terminating session\n");
					in.close();
					break;
				}
				sop(response);
				delegate(choice);
				
				
			}
			
		}
		catch(ClassNotFoundException e) {
			sop("Transmission error");
			sop(e);
			System.exit(1);
		}
		catch(IOException e) {
			sop("Error receiving input");
			sop(e);
			System.exit(1);
		}
		
	}

	//closes remaining open streams and socket
	public void closeSession() {
		try {
			if(DEBUG)
				sop("Closing all connections and streams");
			out.close();
			sock.close();
			userInput.close();
		}
		catch (IOException e) {
			sop("Error ending client connection");
			System.exit(1);
		}
		
	}
	//overrides Thread.run() 
	@Override
	public void run() {
		if(openConnection()) {
			handleSession();
			closeSession();
		}
	}
	
}
