package bri.outilServer;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.net.Socket;

public class ServiceManager {

	
    // Map pour associer chaque port à sa classe de service
    private static ConcurrentHashMap<Integer, Class<? extends ServiceServeur>> portServiceMap = new ConcurrentHashMap<>();

    // Enregistrer un service pour un port spécifique
    public static void registerService(int port, Class<? extends ServiceServeur> serviceClass) throws Exception  {
    	
		if (!bri.outilServer.ServiceServeur.class.isAssignableFrom(serviceClass)) {
            throw new Exception("La classe ne respecte pas la norme : elle n'implémente pas l'interface outilServer.Service");
        }
    	
        portServiceMap.put(port, serviceClass);
    }

    // Obtenir une classe de service pour un port donné en donnant la socket_client
    public static ServiceServeur newService(Socket client_socket, int port) {    	
		ServiceServeur service = null;
		try {
			service = portServiceMap.get(port).getConstructor(Socket.class).newInstance(client_socket);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return service;
    }

    
  
}
