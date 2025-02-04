package bri;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

import bri.outilServer.ServiceServeur;


public class ServiceBRiAma extends ServiceServeur {
	

	public ServiceBRiAma(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(super.getSocket().getInputStream ( )));
			PrintWriter out = new PrintWriter (super.getSocket().getOutputStream ( ), true);
			
			out.println(ServiceRegistry.toStringue()+ "##Tapez le numéro de service désiré :");
			int choix = Integer.parseInt(in.readLine());
			
			//Chargement du service
			Class<?> classServiceDemande = ServiceRegistry.getServiceClass(choix);
			Service ServiceDemande = (Service) classServiceDemande.getConstructor(Socket.class).newInstance(super.getSocket());
			ServiceDemande.run();
			
			}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			System.err.println("Erreur lors du chargement du service " + e);
		}

		try {super.getSocket().close();} catch (IOException e2) {}
	}


}
