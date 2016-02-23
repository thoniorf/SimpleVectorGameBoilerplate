package it.vectorobjectmovement.core;

import javax.swing.JPanel;

import it.vectorobjectmovement.core.collision.CollisionHandler;
import it.vectorobjectmovement.core.input.PlayerControls;
import it.vectorobjectmovement.core.object.direction.Direction;
import it.vectorobjectmovement.gui.GamePanel;

public class GameManager implements Runnable {

	public static void main(String[] args) {
		GameManager.getManager();
		GameWorld.getWorld();
		GameManager.getManager().setViewport(new GamePanel("Vector Object Movement", true));
		new Thread(GameManager.getManager()).start();

	}

	private static GameManager manager;
	private static Integer round;
	private static State state;

	private JPanel viewport;

	public static GameManager getManager() {
		if (manager == null) {
			manager = new GameManager();
		}

		return manager;
	}

	public static Integer getRound() {
		return round;
	}

	public static State getState() {
		return state;
	}

	public static void setRound(Integer round) {
		GameManager.round = round;
	}

	public static void setState(State state) {
		GameManager.state = state;
	}

	public JPanel getViewport() {
		return viewport;
	}

	public void setViewport(JPanel viewport) {
		this.viewport = viewport;
	}

	private GameManager() {
		state = State.Ready;
	}

	public static void getInput() {
		if (PlayerControls.getKeys().get("W")[0] == 1) {
			GameWorld.getWorld().getPlayer().setMoveDirection(Direction.nord);
		} else if (PlayerControls.getKeys().get("A")[0] == 1) {
			GameWorld.getWorld().getPlayer().setMoveDirection(Direction.ovest);
		} else if (PlayerControls.getKeys().get("S")[0] == 1) {
			GameWorld.getWorld().getPlayer().setMoveDirection(Direction.sud);
		} else if (PlayerControls.getKeys().get("D")[0] == 1) {
			GameWorld.getWorld().getPlayer().setMoveDirection(Direction.est);
		} else if (PlayerControls.getKeys().get("SPACE")[0] == 1) {
			GameWorld.getWorld().getPlayer().charge();
		} else if (PlayerControls.getKeys().get("ESCAPE")[0] == 1) {
			if (!state.equals(State.Pause)) {
				state = State.Pause;
			} else {
				state = State.Run;
			}
		} else {
			GameWorld.getWorld().getPlayer().setMoveDirection(Direction.stop);
		}
	}

	@Override
	public void run() {
		while (!state.equals(State.Stop)) {
			state = State.Run;
			while (state.equals(State.Run)) {
				GameManager.getInput();
				GameWorld.update();
				CollisionHandler.check();
				viewport.repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					System.out.println("Game interrupted");
				}
			}

		}

	}

}
