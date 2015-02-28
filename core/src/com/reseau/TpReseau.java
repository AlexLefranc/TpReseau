package com.reseau;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class TpReseau extends ApplicationAdapter implements Runnable{
	
	private static VuePoint vuePoint;
	private static ConnexionReseau connexion = null;
	private ArrayList<Point<Float, Float>> listeDePoints;
	
	@Override
	public void create () {
		vuePoint = new VuePoint();
		
		Gdx.input.setInputProcessor(new InputAdapter () {
			public boolean touchDown (int x, int y, int pointer, int button) {
				
				if (button == Input.Buttons.LEFT && connexion != null && connexion.isConnected()) {					
					Point<Float, Float> pointEnvoye = new Point<Float, Float>((float) Gdx.input.getX(), (float) Gdx.graphics.getHeight() - Gdx.input.getY());
					
					try {
						connexion.envoyerPoint(pointEnvoye);
					} catch (IOException e) {
						e.printStackTrace();
					}				
				}			
				return true;
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for (Point<Float, Float> point : listeDePoints)
			vuePoint.dessinerPoint(point);
	}
	
	public static void recuperationProprietes() throws Exception {
		Field field = Class.forName("com.badlogic.gdx.graphics.Color").getField(connexion.getProp().getProperty("couleur"));
		vuePoint.initialisationParametres((Color) field.get(null), Float.parseFloat(connexion.getProp().getProperty("rayon")));
	}
	
	@Override
	public void run() {
		Point<Float, Float> point;
		
		try {
			/*Etablissement de la connexion et recuperation des proprietes*/
			if (connexion == null) connexion = new ConnexionReseau();
			recuperationProprietes();
			
			/*Synchronisation du serveur et du client lors de leurs envoi/réception*/
			synchronized(this) {
				while ((point = connexion.recevoirPoint()) != null) 
					listeDePoints.add(point);
			}
		}
		catch (Exception e) { }
	}

}
