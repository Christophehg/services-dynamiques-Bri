package appli;

import java.io.IOException;

import bri.ServiceRegistry;
import bri.outilServer.Serveur;
import bri.outilServer.ServiceServeur;
import bri.outilServer.ServiceManager;

public class BRiLaunch {
	
	@SuppressWarnings("resource")
    public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		args = new String[] {
		        "bri.ServiceBRiAma", "3000",  
		        "bri.ServiceBRiProg", "4000"   
		    };
		
        // Vérifiez si les arguments sont corrects
        if (args.length < 2 || args.length % 2 != 0) {
            System.err.println("Usage : java BRiLaunch <ClasseService1> <Port1> [<ClasseService2> <Port2> ...]");
            return;
        }

        // Enregistrez chaque service avec son port
        for (int i = 0; i < args.length; i += 2) {
        	
            String serviceClassName = args[i];
            int port = Integer.parseInt(args[i + 1]);
         
            // Charger la classe de service
            Class<? extends ServiceServeur > serviceClass =  (Class<? extends ServiceServeur>) Class.forName(serviceClassName);
            try {
				ServiceManager.registerService(port, serviceClass);
			} catch (Exception e) {
				e.printStackTrace();
			}

            // Démarrer le serveur sur ce port
            new Thread(new Serveur(port)).start();
            System.out.println("Serveur démarré sur le port " + port + " avec le service " + serviceClassName);
        }
    }
}
