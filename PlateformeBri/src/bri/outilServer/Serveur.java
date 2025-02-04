package bri.outilServer;

import java.io.*;
import java.net.*;

public class Serveur implements Runnable {
	private ServerSocket listen_socket; 
	private Thread thread;
	private int portServeur;
	
	public Serveur(int port) throws IOException {  
			listen_socket = new ServerSocket(port);
			this.thread = new Thread(this);		
			this.portServeur = port;
	}

	public void start(){
		this.thread.start();
	}

	public void run() {
		System.out.println("Serveur lancé");
		try {
			while(true) {
				Socket client_socket = listen_socket.accept();
				ServiceServeur service = ServiceManager.newService(client_socket, portServeur);
				service.start();
			}
		}
		catch (IOException e) { }
		finally {
			this.close();
		}
		
		System.err.println("Serveur arreté ");
	}
	public void close() {
		try {this.listen_socket.close();} catch (IOException e) {}
	}
}
