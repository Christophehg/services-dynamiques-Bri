package addService;

import java.lang.reflect.*;
import java.net.Socket;
import java.util.Vector;

public class BRiServiceManager {
    private static final Vector<Class<?>> services = new Vector<>();

    public static void addService(Class<?> classService) throws Exception {
        if (!BRi.Service.class.isAssignableFrom(classService)) {
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
                throw new Exception("La classe ne respecte pas la norme : la méthode toStringue() n'est pas conforme.");
            }
        } catch (NoSuchMethodException e) {
            throw new Exception("La classe ne respecte pas la norme : elle n'a pas de méthode toStringue().");
        }

        services.add(classService);
        System.out.println("Classe ajoutée avec succès : " + classService.getName());
    }

    public static void listServices() {
        for (Class<?> service : services) {
            System.out.println("Service ajouté : " + service.getName());
        }
    }
}
