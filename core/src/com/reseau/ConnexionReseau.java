package com.reseau;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnexionReseau {
	
	private Socket socket = null;
	private boolean isServer = false;
	private Properties proprietes = null;
	
	public ConnexionReseau() throws Exception {
		//Creation de la connexion en tant que client
		try {
			socket = new Socket("127.0.0.1", 10666);
		}
		catch (Exception e) {
			//Aucun serveur n'a �t� trouv�, il devient donc le serveur
			initialisationServeur();
			isServer = true;
		}
		
		if (socket != null)
			comparerProprietes();
	}
	
	private void initialisationServeur() {
		try {
			socket = (new ServerSocket(10666)).accept();
		} catch (Exception e) {
			socket = null;
		}
	}
	
	private void comparerProprietes() throws Exception {
		Properties proprietesClient = null;
		Properties mesProprietes = null;
		
		/*R�cuperation de mes propri�t�s*/
		if (socket != null) {
			mesProprietes = new Properties();
			mesProprietes.load(new FileInputStream("../configurationAffichage.properties"));		
		} else {
			throw new RuntimeException("R�cup�ration de mes propri�t�s impossible");
		}

		/*Envoi de mes propri�t�s au client*/
		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		output.writeObject(mesProprietes);
		
		/*R�cup�ration des propri�t�s du client*/
		ObjectInputStream intput = new ObjectInputStream(socket.getInputStream());
		proprietesClient = (Properties) intput.readObject();

		
		/*Comparaison des deux*/
		if (mesProprietes.equals(proprietesClient)) {
			proprietes = mesProprietes;
		}
		else {
			throw new RuntimeException("Mes propri�t�s et celle du client sont diff�rentes");
		}
	}
	
	public Properties getProp(){
		return proprietes;
	}
	
	public boolean isConnected() {
		return (socket != null);
	}
	
	public boolean getIsServer() {
		return isServer;
	}

	public void envoyerPoint(Point<Float, Float> pointEnvoye) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		output.writeObject(pointEnvoye);
	}
	
	@SuppressWarnings("unchecked")
	public Point<Float, Float> recevoirPoint() throws Exception {
		Point<Float, Float> pointRecu = null;
	
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		pointRecu = (Point<Float, Float>) input.readObject();
		
		return pointRecu;
	}
}
