package it.vectorobjectmovement.gamecore.object.actor.player;

import java.awt.Point;
import java.util.Random;

import it.vectorobjectmovement.gamecore.object.actor.AbstractActor;

public class Player extends AbstractActor {
	protected Integer score;
	protected String username;

	public Player(Point origin) {
		super(origin, new Random().nextInt(), new Random().nextInt(), new Random().nextInt());
	}

	public void addScore(Integer score) {
		this.score += score;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
