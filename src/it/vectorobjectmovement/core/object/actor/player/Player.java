package it.vectorobjectmovement.core.object.actor.player;

import java.awt.Point;

import it.vectorobjectmovement.core.object.actor.AbstractActor;

public class Player extends AbstractActor {
	protected static Player player;
	protected static Integer score;
	protected static String username;

	public static Integer getScore() {
		return score;
	}

	public static String getUsername() {
		return username;
	}

	public static void addScore(Integer score) {
		Player.score += score;
	}

	public static void setUsername(String username) {
		Player.username = username;
	}

	protected Player(Point origin) {
		super(origin);
	}

	public static Player newPlayer(Point origin) {
		if (player == null) {
			player = new Player(origin);
		}

		return player;
	}

}
