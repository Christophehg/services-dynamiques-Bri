package bri.outilServer;

import java.net.*;

//  la classe Service coté serveur, à sous-classer pour implémenter le service dans run()

public abstract  class ServiceServeur implements Runnable {

	public ServiceServeur(Socket socket) {
		this.socket = socket;
	}

	private Socket socket;
	private Thread t;
	
	protected Socket getSocket() {
		return socket;
	}
	
	// implémenter le run dans la sous-classe

	@Override
	public abstract void run();

	/**
	 * start uniquement par Serveur
	 */
	void start() {
		this.t = new Thread(this);
		this.t.start();
	}
	
	// interruption du service par l'application - comportement suspect, inactivité, etc
	public void close(){
		this.t.interrupt(); 
	}

	@Override
	protected void finalize() throws Throwable {
		socket.close();
	}
	




}
