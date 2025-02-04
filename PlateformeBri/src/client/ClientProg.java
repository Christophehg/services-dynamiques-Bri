package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProg {
	private final static int PORT_SERVICE = 4000;
	private final static String HOST = "localhost"; 

public static void main(String[] args) {
	Socket s = null;		
	try {
		s = new Socket(HOST, PORT_SERVICE);

		BufferedReader sin = new BufferedReader (new InputStreamReader(s.getInputStream ( )));
		PrintWriter sout = new PrintWriter (s.getOutputStream ( ), true);
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
	
		System.out.println("Connecté au serveur " + s.getInetAddress() + ":"+ s.getPort());
		
		String line;
		line = sin.readLine();
		System.out.println(line.replaceAll("##", "\n"));
		
		String choixProgrammeur = clavier.readLine();
		sout.println(choixProgrammeur);
		
		if ("I".equalsIgnoreCase(choixProgrammeur)){
			inscrireProgrammeur(sin, sout, clavier);
			connecterProgrammeur(sin, sout, clavier);
		}
		else if ("C".equalsIgnoreCase(choixProgrammeur)) {
			connecterProgrammeur(sin, sout, clavier);
		}
		
		System.out.println("Fin de la connexion" );
		s.close();	
	}
	catch (IOException e) { System.err.println("Fin de la connexion"); }
	try { if (s != null) s.close(); } 
	catch (IOException e2) { ; }		
}

private static void connecterProgrammeur(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
	try {
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		sout.println(clavier.readLine());
		
		
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		sout.println(clavier.readLine());
		
		
		String reponse = sin.readLine().replaceAll("##", "\n");
		if (reponse.equals("success")) {
			System.out.println(reponse.replaceAll("##", "\n"));
			System.out.println(sin.readLine().replaceAll("##", "\n"));
			
			// Accéder au menu programmeur après connexion réussie
            gererProgrammeur(sin, sout, clavier);
		}
				
		else {
			System.out.println(reponse.replaceAll("##", "\n"));
			return ;
		}
		
		
	} catch (IOException e) {
		e.printStackTrace();
	}
}


private static void gererProgrammeur(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
    try {
        while (true) {
            System.out.println(sin.readLine().replaceAll("##", "\n"));
            
            String choix = clavier.readLine(); 
            sout.println(choix); 

            if ("4".equals(choix)) { // Quitter
                System.out.println("Déconnexion...");
                break;
            }

            switch (choix) {
                case "1": // Fournir un nouveau service
                    fournirService(sin, sout, clavier);
                    break;
                case "2": // Mettre à jour un service
                    mettreAJourService(sin, sout, clavier);
                    break;
                case "3": // Changer l'adresse FTP
                    changerAdresseFTP(sin, sout, clavier);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static void fournirService(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
    try {
        System.out.println(sin.readLine().replaceAll("##", "\n")); // "Entrez le nom de la classe du nouveau service :"
        sout.println(clavier.readLine()); // Envoi du nom de la classe
        
        System.out.println(sin.readLine().replaceAll("##", "\n")); // Réponse du serveur
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static void mettreAJourService(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
    try {
        System.out.println(sin.readLine().replaceAll("##", "\n")); // "Entrez le numéro du service à mettre à jour :"
        sout.println(clavier.readLine()); // Envoi du numéro du service
        	
        System.out.println(sin.readLine().replaceAll("##", "\n")); 
        sout.println(clavier.readLine()); //envoie du nom de la classe
        
        System.out.println(sin.readLine().replaceAll("##", "\n")); // Réponse du serveur
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static void changerAdresseFTP(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
    try {
        System.out.println(sin.readLine().replaceAll("##", "\n")); // "Entrez la nouvelle adresse FTP :"
        sout.println(clavier.readLine()); // Envoi de la nouvelle adresse FTP

        System.out.println(sin.readLine().replaceAll("##", "\n")); // Réponse du serveur
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static void inscrireProgrammeur(BufferedReader sin, PrintWriter sout, BufferedReader clavier) {
	try {
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		sout.println(clavier.readLine());
		
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		sout.println(clavier.readLine());
		
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		sout.println(clavier.readLine());
		
		System.out.println(sin.readLine().replaceAll("##", "\n"));
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	
}

}
