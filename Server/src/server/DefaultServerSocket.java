package server;
import adapter.*;
import java.io.*;
import java.net.*;

public class DefaultServerSocket extends Thread implements SocketInterfaceConstants {

	private int port;
	private ServerSocket server;
	private static BuildCarModelOptions builder = new BuildCarModelOptions();

	public DefaultServerSocket(int port) {
		this.port = port;
		try {
			this.server = new ServerSocket(port);
		}
		catch (IOException e) {
			System.err.println("Port " + port + " unavailable ");
			System.exit(1);
		}
	}
	
	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		Socket clientSocket = null;
		System.out.println("Server online");
		while (true) {
			System.out.println("Waiting for client to connect");
			try {
				clientSocket = server.accept();
			}
			catch (IOException e) {
				System.err.println("Connection Error");
				System.exit(1);
			}

			if (DEBUG)
				System.out.println(clientSocket.getLocalAddress());
			new DefaultSocketClient(clientSocket, builder).start();

		}
	}

}
