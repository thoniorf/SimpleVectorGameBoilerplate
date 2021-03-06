package it.vectorobjectmovement.gamecore;

import javax.swing.JPanel;

import it.vectorobjectmovement.gamecore.collision.CollisionHandler;
import it.vectorobjectmovement.gamecore.input.PlayerControls;
import it.vectorobjectmovement.gamecore.object.direction.Direction;
import it.vectorobjectmovement.gamegui.GamePanel;

public class GameManager implements Runnable {
	private final static int MAX_FPS = 33;
	private final static int MAX_FRAME_SKIP = 5;
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

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
		GameManager.setState(State.Ready);
	}

	public static void getInput() {
		if (PlayerControls.getKeys().get("W")[0] == 1) {
			GameWorld.getPlayer().setMoveDirection(Direction.nord);
		} else if (PlayerControls.getKeys().get("A")[0] == 1) {
			GameWorld.getPlayer().setMoveDirection(Direction.ovest);
		} else if (PlayerControls.getKeys().get("S")[0] == 1) {
			GameWorld.getPlayer().setMoveDirection(Direction.sud);
		} else if (PlayerControls.getKeys().get("D")[0] == 1) {
			GameWorld.getPlayer().setMoveDirection(Direction.est);
		} else if (PlayerControls.getKeys().get("SPACE")[0] == 1) {
			GameWorld.getPlayer().charge();
		} else if (PlayerControls.getKeys().get("ESCAPE")[0] == 1) {
			if (!state.equals(State.Pause)) {
				state = State.Pause;
			} else {
				state = State.Run;
			}
		} else {
			GameWorld.getPlayer().setMoveDirection(Direction.stop);
		}
	}

	@Override
	public void run() {
		// set times and frames for constant FPS
		long beginTime = 0; // the time when the cycle begun
		long timeDiff = 0; // the time it took for the cycle to execute
		int sleepTime = 0; // ms to sleep (<0 if we're behind)
		int framesSkipped = 0; // number of frames being skipped
		// start first level
		GameManager.setState(State.Run);
		// main loop
		while (!state.equals(State.Stop)) {
			while (state.equals(State.Run)) {
				// frame start time
				beginTime = System.currentTimeMillis();
				framesSkipped = 0;
				// main cycle
				GameManager.getInput();
				GameWorld.update();
				CollisionHandler.check();
				viewport.repaint();
				// frame diff after cycle
				timeDiff = System.currentTimeMillis() - beginTime;
				sleepTime = (int) (FRAME_PERIOD - timeDiff);

				if (sleepTime > 0) {
					// if sleepTime > 0 we're OK
					try {
						// send the thread to sleep for a short period
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIP) {
					GameWorld.update();
					sleepTime += FRAME_PERIOD;
					framesSkipped++;
				}
			}
			if (state.equals(State.Next)) {
				GameWorld.makeLevel(GameWorld.getMax_enemy() + 1);
				GameManager.setState(State.Run);
			} else {
				break;
			}

		}
		System.exit(0);
	}

}
