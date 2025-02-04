package bri;

import java.util.ArrayList;
import java.util.List;

public class Programmeur {

	private String pseudo;
	private String password;
	private String ftpUrl;
	private List<Integer> numeroServices;
	
	Programmeur(String pseudo, String password, String ftpUrl){
		this.pseudo = pseudo;
		this.password = password;
		this.ftpUrl = ftpUrl;
		numeroServices = new ArrayList<Integer>();
	}
	
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
	
	public List<Integer> getNumeroServices(){
		return this.numeroServices;
	}
	
	public void addNumeroServie(int numero) {
		this.numeroServices.add(numero);
	}
	
	public int getNbService() {
		return this.numeroServices.size();
	}
}
