package it.vectorobjectmovement.gamegui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.vectorobjectmovement.gamecore.GameWorld;
import it.vectorobjectmovement.gamecore.input.PlayerControls;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public static Dimension getScreen() {
		return screen;
	}

	private JFrame viewport;
	private Boolean fullscreen;

	public GamePanel(String Name, Boolean fullscreen) {
		super(null);
		this.fullscreen = fullscreen;
		viewport = createFrame(Name, fullscreen);
		setViewport(viewport);
		setFocusable(true);
		requestFocus();
		addKeyListener(new PlayerControls());
	}

	private JFrame createFrame(String Name, Boolean fullscreen) {
		JFrame frame = new JFrame(Name);
		frame.setResizable(!fullscreen);
		frame.setUndecorated(fullscreen);
		frame.setPreferredSize(screen);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		return frame;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// paint panel background
		g2d.fillRect(0, 0, screen.width, screen.height);
		// paint World
		GameWorld.paint(g2d);
	}

	private void setViewport(JFrame viewport) {
		if (fullscreen) {
			// viewport.setExtendedState(JFrame.MAXIMIZED_BOTH);
			device.setFullScreenWindow(viewport);
		}
		viewport.add(this);
		viewport.pack();
		viewport.setVisible(true);
	}
}
