package driver;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import client.*;

public class Driver {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket sock;
	private String host = "localhost";
	private int port = 9998;
	
    public static void sop(Object o) {
		System.out.println(o.toString());
	}
    
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
	public static void main(String[] args) {
		//driver for client
		Driver drive = new Driver();
		if(drive.openConnection()) {
			try {
				for(int i = 1; i <4; i++) {
					drive.in.readObject();
					drive.send(1);
					drive.in.readObject();
					Properties prop = new Properties();
					FileInputStream input = new FileInputStream(i+".txt");
					prop.load(input);
					drive.send(prop);
					sop(drive.in.readObject().toString());
					drive.send(" ");
				}
				drive.in.readObject();
				drive.send(0);
			}
				catch(ClassNotFoundException e) {
					sop("Transmission error");
					sop(e);
					
					
				}
				catch(IOException e) {
					sop("Error receiving input");
					sop(e);
					
				}
	

			}
		}
	}
