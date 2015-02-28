package com.reseau;

import java.io.Serializable;

public class Point<X extends Number, Y extends Number> implements Serializable{

	private static final long serialVersionUID = 1L;
	private X x;
	private Y y;

	public Point(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return x;
	}
	
	public Y getY() {
		return y;
	}
}
