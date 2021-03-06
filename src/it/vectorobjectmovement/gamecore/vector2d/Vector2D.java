package it.vectorobjectmovement.gamecore.vector2d;

import java.awt.Point;

public class Vector2D {
	protected Point components;
	protected Float lenght;

	public Vector2D(Integer x, Integer y) {
		this.components = new Point(x, y);
		this.lenght = getLenght();
	}

	public Vector2D(Point origin) {
		this.components = origin;
		this.lenght = getLenght();
	}

	public Point getComponents() {
		return components;
	}

	public Float getLenght() {
		return (float) Math.sqrt(components.x ^ 2 + components.y ^ 2);
	}

	public Vector2D normalize() {
		return new Vector2D((int) (components.x / Math.abs(lenght)), (int) (components.y / Math.abs(lenght)));
	}

	public void sum(Vector2D vector) {
		this.components.x += vector.getComponents().x;
		this.components.y += vector.getComponents().y;
	}

	public void subtract(Vector2D vector) {
		this.components.x = this.components.x - vector.getComponents().x;
		this.components.y = this.components.y - vector.getComponents().y;
	}

	public void divide(Integer scalar) {
		this.components.x /= scalar;
		this.components.y /= scalar;
	}

	public Integer dotProduct(Vector2D vector) {
		return this.components.x * vector.getComponents().x + this.components.y * vector.getComponents().y;
	}

	public void setComponents(int x, int y) {
		components.x = x;
		components.y = y;
	}

	public void setComponents(Point origin) {
		this.components = origin;
	}

	public void setX(Integer x) {
		this.components.x = x;
	}

	public void setY(Integer y) {
		this.components.y = y;
	}

}
