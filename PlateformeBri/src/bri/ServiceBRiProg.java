package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import bri.outilServer.ServiceServeur;

public class ServiceBRiProg extends ServiceServeur {
	
	public ServiceBRiProg(Socket socket) {
		super(socket);
	}
	
	private static List<Programmeur> programmers;
	
	static {
		programmers = new ArrayList<>();
		
		programmers.add(new Programmeur("matthieu", "matt", "ftp://admin:admin@localhost:2121/bin/"));
	}
	


	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(super.getSocket().getInputStream ( )));
			PrintWriter out = new PrintWriter (super.getSocket().getOutputStream ( ), true);
		
			out.println("Bienvenue dans le service pour les programmeurs. Veuillez vous authentifier. ##"
					+ "[I/C] Tapez 'I' pour inscription ou 'C' pour connexion :");
			String choix = in.readLine();
			
			if ("i".equalsIgnoreCase(choix)){
				inscrireProgrammeur(in, out);
				connecterProgrammeur(in, out);
			}
			else if ("c".equalsIgnoreCase(choix)) {
				connecterProgrammeur(in, out);
			}
			else {
				out.println("Choix invalide. Déconnexion.");
			}
	        
			}
		catch ( IOException e) {
			//Fin du service
			System.err.println("Erreur lors du chargement du service " + e);
		}

		try {super.getSocket().close();} catch (IOException e2) {}
	}
	
	
	private static void connecterProgrammeur(BufferedReader in, PrintWriter out) {

		try {
			out.println("=== Connexion === ##" +
					"Entrez votre pseudo : ");
			
			String pseudo = in.readLine();
			
			
			out.println("Entrez votre mot de passe : ");
		    String password = in.readLine();

		    Programmeur prog = getProgrammeur(pseudo, password);
		    if (prog != null ) {
		    	out.println("success");
		    	
		        out.println("Connexion réussie ! ## "+
		        		"Votre serveur FTP est : " + prog.getFtpUrl());

		        
		        gererProgrammeur(prog, in, out);
		        
		        
		    } else {
		        out.println("Login ou mot de passe incorrect.");
		    }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void gererProgrammeur(Programmeur programmeur, BufferedReader in, PrintWriter out) {
	    try {
	        while (true) {
	            out.println("=== Menu Programmeur === ##" +
	                        "1. Fournir un nouveau service ##" +
	                        "2. Mettre à jour un service ##" +
	                        "3. Changer l'adresse FTP ##" +
	                        "4. Quitter ##" +
	                        "Entrez votre choix :");
	            
	            String choix = in.readLine();
	            if (choix == null || choix.equals("4")) {
	                out.println("Déconnexion. Merci d'avoir utilisé BRi !");
	                break;
	            }

	            switch (choix) {
	                case "1":
	                    fournirService(programmeur, in, out);
	                    break;
	                case "2":
	                    mettreAJourService(programmeur, in, out);
	                    break;
	                case "3":
	                    changerAdresseFTP(programmeur, in, out);
	                    break;
	                default:
	                    out.println("Choix invalide.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private static void fournirService(Programmeur programmeur, BufferedReader in, PrintWriter out) {
	    try {
	        out.println("Entrez le nom de la classe du nouveau service :");
	        String className = in.readLine();
	        
	        try {
	        	String url = programmeur.getFtpUrl();
	            URLClassLoader urlcl  = new URLClassLoader(new URL[] {new URI(url).toURL()});
	            
	            Class<?> classeChargée = null;
	            classeChargée = (Class<?>) urlcl.loadClass(className);
	            
				int numeroService = ServiceRegistry.addService(classeChargée, programmeur.getPseudo());
				programmeur.addNumeroServie(numeroService); // Ajout à la liste du programmeur
	            
				out.println("Service " + className + " ajouté avec succès !");
	        } catch (Exception  e) {
	        	out.println("Erreur : Impossible de charger la classe : " + e);

	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private static void mettreAJourService(Programmeur programmeur, BufferedReader in, PrintWriter out) {
	    try {
	    	
	    	int nbServiceUtilisateur = programmeur.getNbService();
	    	List<Integer> numerosServiceUser = programmeur.getNumeroServices();
	    	
	    	StringBuilder result = new StringBuilder();
			result.append("Vos Activités : " + nbServiceUtilisateur + "##");

			for (int i = 0; i < nbServiceUtilisateur ; i++) {
				
				int numeroServiceChargee = numerosServiceUser.get(i);
				
				Class<?> serviceUser = ServiceRegistry.getServiceClass(numeroServiceChargee);
				
				result.append("Activité N°").append(i+1).append(" : " )
					  .append(
							  serviceUser.getSimpleName()
							  )
					  .append("##");
			}
			
			out.println(result + "##"+
					"Entrez le numéro du service à mettre à jour :");
	    
	        String serviceNumero = in.readLine();
	        int numero = Integer.parseInt(serviceNumero);

	        

	        if (numero < 1 || numero > nbServiceUtilisateur + 1 ) {
	            out.println("Service introuvable.");
	            return;
	        }
	        
	       
	        try {
		        out.println("Entrez le nom de la classe du nouveau service :");
		        String className = in.readLine();
	        	
	        	String url = programmeur.getFtpUrl();
	            URLClassLoader urlcl  = new URLClassLoader(new URL[] {new URI(url).toURL()});
	            
	            Class<?> classeChargée = null;
	            classeChargée = (Class<?>) urlcl.loadClass(className);
	            
	            ServiceRegistry.miseJourService(numero, classeChargée, programmeur.getPseudo());

	            out.println("Service " + classeChargée.getSimpleName() + " mis à jour avec succès !");
	        } catch (Exception e) {
	            out.println("Erreur lors du chargement de la classe mise à jour : " + e);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private static void changerAdresseFTP(Programmeur programmeur, BufferedReader in, PrintWriter out) {
	    try {
	        out.println("Entrez la nouvelle adresse FTP :");
	        String newFtpUrl = in.readLine();

	        programmeur.setFtpUrl(newFtpUrl);
	        out.println("Adresse FTP mise à jour avec succès !");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	private static void inscrireProgrammeur(BufferedReader in, PrintWriter out) {
		try {
			out.println("=== Inscription === ##" +
					"Entrez un pseudo :");
			String pseudo = in.readLine();
			
			
			out.println("Entrez un mot de passe : ");
		    String password = in.readLine();

		    out.println("Entrez l'URL de votre serveur FTP : ");
		    String ftpUrl = in.readLine();
		    
		    // Ajouter le programmeur
		    programmers.add(new Programmeur(pseudo, password ,ftpUrl));
		    out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static Programmeur getProgrammeur(String pseudo, String password) {
		Programmeur prog = null;
		
		for (int i = 0; i < programmers.size(); i++) {
			if (programmers.get(i).getPseudo().equals(pseudo) && programmers.get(i).getPassword().equals(password))
				prog = programmers.get(i);
		}
		return prog;
		
	}

}
