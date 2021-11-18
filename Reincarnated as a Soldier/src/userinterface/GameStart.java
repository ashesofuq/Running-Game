package userinterface;

import javax.swing.JFrame;

public class GameStart extends JFrame {
	
	public static final int SCREEN_WIDTH = 978;
	private GameScreen gameScreen;
	
	public GameStart() {
		super("Reincarnated as a Soldier");
		setSize(SCREEN_WIDTH, 640);
		setLocation(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		gameScreen = new GameScreen();
		addKeyListener(gameScreen);
		add(gameScreen);
	}
	
	public void startGame() {
		setVisible(true);
		gameScreen.startGame();
	}
	
	public static void main(String args[]) {
		(new GameStart()).startGame();
	}
}
