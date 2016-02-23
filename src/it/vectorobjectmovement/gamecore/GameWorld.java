package it.vectorobjectmovement.gamecore;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import it.vectorobjectmovement.gamecore.arena.Arena;
import it.vectorobjectmovement.gamecore.ia.IAFocus;
import it.vectorobjectmovement.gamecore.object.Entity;
import it.vectorobjectmovement.gamecore.object.actor.Enemy;
import it.vectorobjectmovement.gamecore.object.actor.player.Player;

public class GameWorld {
	private static GameWorld world;

	private static ArrayList<Entity> objects;

	private static Arena arena;

	private static Integer max_enemy;

	private static Integer enemies;

	private static Player player;

	private static void drawOrder() {
		objects.sort(null);
	}

	public static Integer getMax_enemy() {
		return max_enemy;
	}

	public static ArrayList<Entity> getObjects() {
		return objects;
	}

	public static Player getPlayer() {
		return player;
	}

	public static GameWorld getWorld() {
		if (world == null) {
			world = new GameWorld();
		}
		return world;
	}

	public static void paint(Graphics2D g) {
		arena.paint(g);
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).getAlive()) {
				objects.get(i).paint(g);
			}

		}
	}

	public static void makeLevel(int n_enemies) {
		// Initialize arena
		arena = new Arena(new BufferedImage(768, 768, BufferedImage.TYPE_INT_RGB));
		// level max enemies
		max_enemy = new Integer(n_enemies);
		// level enemies number
		enemies = new Integer(max_enemy);
		// object list
		objects = new ArrayList<Entity>();
		// spawn Player
		player = new Player(arena.getSpawn().get(0));
		objects.add(player);
		// spawn enemies
		for (int i = 0; i < enemies; i++) {
			objects.add(new Enemy(arena.getSpawn().get(i + 1)));
			((Enemy) objects.get(i + 1)).setStrategy(new IAFocus((Enemy) objects.get(i + 1), arena, objects));
		}
	}

	public static void setMax_enemy(Integer max_enemies) {
		if (max_enemies > 8) {
			max_enemy = max_enemies;
		} else {
			max_enemy = max_enemies;
		}
	}

	public static void update() {
		drawOrder();
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).getAlive()) {
				objects.get(i).update();
				if (!arena.getShape().contains(objects.get(i).getOrigin())) {
					objects.get(i).setAlive(Boolean.FALSE);
					// game over
					if (objects.get(i) instanceof Player) {
						GameManager.setState(State.Over);
						return;
					} else if (objects.get(i) instanceof Enemy) {
						enemies += -1;
					}
				}
			}
		}
		// no more enemies
		if (enemies == 0) {
			GameManager.setState(State.Next);
			return;
		}

	}

	private GameWorld() {
		// Initialize arena
		arena = new Arena(new BufferedImage(768, 768, BufferedImage.TYPE_INT_RGB));
		// level max enemies
		max_enemy = new Integer(1);
		// level enemies number
		enemies = new Integer(max_enemy);
		// object list
		objects = new ArrayList<Entity>();
		// spawn Player
		player = new Player(arena.getSpawn().get(0));
		objects.add(player);
		// spawn enemies
		for (int i = 0; i < enemies; i++) {
			objects.add(new Enemy(arena.getSpawn().get(i + 1)));
			((Enemy) objects.get(i + 1)).setStrategy(new IAFocus((Enemy) objects.get(i + 1), arena, objects));
		}
	}
}
