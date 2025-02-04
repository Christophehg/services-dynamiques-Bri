package bri;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.*;
import java.net.Socket;


public class ServiceRegistry {
	// cette classe est un registre de services pour les programmeurs
	// partagée en concurrence par les clients et les "ajouteurs" de services,

	static {
		servicesClasses = new Vector<>();
	}
	
	//Ressource partagées
	private static Vector<Class<?>> servicesClasses;
	
	
	// ajoute une classe de service après controle de la norme BLTi
	public static int addService(Class<?> classService, String pseudoProgrammeur) throws Exception {
		return addService(-3, classService, pseudoProgrammeur);
	}
	
	private static int addService(int indexe, Class<?> classService, String pseudoProgrammeur) throws Exception {
		// vérifier la conformité par introspection
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector
		
		Package pkg = classService.getPackage();
		if( pkg == null || !pkg.getName().equals(pseudoProgrammeur)) {
			throw new Exception("La classe ne respecte pas la norme : elle doit être dans un package portant votre pseudo lors de votre authentification.");
		}
		
		
		if (!bri.Service.class.isAssignableFrom(classService)) {
            throw new Exception("La classe ne respecte pas la norme : elle n'implémente pas l'interface BRi.Service.");
        }

        if (Modifier.isAbstract(classService.getModifiers())) {
            throw new Exception("La classe ne respecte pas la norme : elle est abstract.");
        }

        if (!Modifier.isPublic(classService.getModifiers())) {
            throw new Exception("La classe ne respecte pas la norme : elle n'est pas publique.");
        }

        try {
            Constructor<?> constructor = classService.getConstructor(Socket.class);
            if (!Modifier.isPublic(constructor.getModifiers())) {
                throw new Exception("La classe ne respecte pas la norme : le constructeur (Socket) n'est pas public.");
            }
            
            Class<?>[] exceptionTypes = constructor.getExceptionTypes();
            if(exceptionTypes.length > 0) {
            	throw new Exception("La classe ne respect pas la norme : le constructeur (Socket) ne doit pas déclarer d'exceptions.");
            }
            
        } catch (NoSuchMethodException e) {
            throw new Exception("La classe ne respecte pas la norme : elle n'a pas de constructeur (Socket).");
        }
        

        boolean hasValidSocketField = false;
        for (Field field : classService.getDeclaredFields()) {
            if (field.getType().equals(Socket.class) &&
                Modifier.isPrivate(field.getModifiers()) &&
                Modifier.isFinal(field.getModifiers())) {
                hasValidSocketField = true;
                break;
            }
        }
        if (!hasValidSocketField) {
            throw new Exception("La classe ne respecte pas la norme : elle n'a pas d'attribut Socket private final.");
        }

        try {
            Method method = classService.getMethod("toStringue");
            if (!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isStatic(method.getModifiers()) ||
                !method.getReturnType().equals(String.class)) {
                throw new Exception("La classe ne respecte pas la norme : la méthode toStringue() n'est pas conforme. public static String method !");
            }
            
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            if(exceptionTypes.length > 0) {
            	throw new Exception("La classe ne respect pas la norme : la méthode toStringue() ne doit pas déclarer d'exceptions.");
            }
        } catch (NoSuchMethodException e) {
            throw new Exception("La classe ne respecte pas la norme : elle n'a pas de méthode toStringue().");
        }
        
		if(indexe == -3) {
			System.out.println("Service : " + classService.getSimpleName() +  " -> ajouté a la ressource partagées");
			servicesClasses.add(classService);
			return servicesClasses.size() ;
		}
		else {
			System.out.println("Mise à jour Service : " + classService.getSimpleName() +  " -> avec success");
			servicesClasses.set(indexe - 1, classService);
			return indexe;
		}
		
	}
	
	
	
	// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
	// liste les activités présentes
	public static String toStringue() {
		int nbActivite = servicesClasses.size();
		StringBuilder result = new StringBuilder();
		result.append("Activités présentes : " + nbActivite + "##");
		for (int i = 0; i < nbActivite ; i++) {
			result.append("Activité N°").append(i+1).append(" : " )
				  .append(servicesClasses.get(i).getSimpleName())
				  .append("##");
		}
		return result.toString();
	}
	
	public static void miseJourService(int indexe, Class<?> service, String pseudoProgrammeur) throws Exception {
		addService(indexe, service, pseudoProgrammeur );
	}

}
