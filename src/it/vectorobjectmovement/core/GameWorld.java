package it.vectorobjectmovement.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import it.vectorobjectmovement.core.arena.Arena;
import it.vectorobjectmovement.core.object.Entity;
import it.vectorobjectmovement.core.object.actor.Enemy;
import it.vectorobjectmovement.core.object.actor.player.Player;

public class GameWorld {
	private static GameWorld world;

	public static GameWorld getWorld() {
		if (world == null) {
			world = new GameWorld();
		}
		return world;
	}

	public static void paint(Graphics2D g) {
		world.arena.paint(g);
		for (int i = 0; i < world.objects.size(); i++) {
			if (world.objects.get(i).getAlive()) {
				world.objects.get(i).paint(g);
			}

		}

	}

	public static void reset() {

	}

	public static void update() {
		world.drawOrder();
		for (int i = 0; i < world.objects.size(); i++) {
			if (world.getObjects().get(i).getAlive()) {
				world.objects.get(i).update();
				if (!world.arena.getShape().contains(world.objects.get(i).getOrigin())) {
					world.objects.get(i).setAlive(Boolean.FALSE);

				}
			}
		}

	}

	private ArrayList<Entity> objects;
	private Arena arena;
	private Integer max_enemy;
	private Player player;

	private GameWorld() {
		arena = new Arena(new BufferedImage(768, 768, BufferedImage.TYPE_INT_RGB));
		max_enemy = 1;
		objects = new ArrayList<Entity>();
		player = Player.newPlayer(arena.getSpawn().get(0));
		objects.add(player);
		for (int i = 0; i < max_enemy; i++) {
			objects.add(new Enemy(arena.getSpawn().get(i + 1)));
		}
		reset();
	}

	private void drawOrder() {
		objects.sort(null);
	}

	public Integer getMax_enemy() {
		return max_enemy;
	}

	public ArrayList<Entity> getObjects() {
		return world.objects;
	}

	public Player getPlayer() {
		return player;
	}

	public void setMax_enemy(Integer max_enemy) {
		this.max_enemy = max_enemy;
	}
}
