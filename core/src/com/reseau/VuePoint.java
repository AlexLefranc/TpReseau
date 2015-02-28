package com.reseau;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class VuePoint extends ShapeRenderer{
	
	private float rayonPoint;
	
	public VuePoint() {
		super();
	}
	
	public void initialisationParametres(Color couleurPoint, float rayonPoint) {
		this.rayonPoint = rayonPoint;
		this.setColor(couleurPoint);
	}
	
	public void dessinerPoint(Point<Float, Float> point) {
		this.begin(ShapeType.Filled);
		this.circle(point.getX(), point.getY(), rayonPoint);;
		this.end();
	}
}
