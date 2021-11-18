package userinterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


import javax.swing.JPanel;

import gameobject.EnemyManager;
import gameobject.LandBackground;
import gameobject.MainCharacter;
import util.getImage;

public class GameScreen extends JPanel implements Runnable, KeyListener {

	private static final int START_GAME_STATE = 0;
	private static final int GAME_PLAYING_STATE = 1;
	private static final int GAME_OVER_STATE = 2;
	
	private LandBackground land;
	private MainCharacter mainCharacter;
	private EnemyManager enemyManager;
	private Thread thread;

	private boolean isKeyPressed;

	private int gameState = START_GAME_STATE;

	private BufferedImage replayButtonImage;
	private BufferedImage gameOverButtonImage;
	private BufferedImage MainMenu;
	private BufferedImage bg, bg2, bg3;

	public GameScreen() {
		mainCharacter = new MainCharacter();
		land = new LandBackground(GameStart.SCREEN_WIDTH, mainCharacter);
		enemyManager = new EnemyManager(mainCharacter);
		replayButtonImage = getImage.getResourceImage("data1/replay.png");
		gameOverButtonImage = getImage.getResourceImage("data1/gameover.png");
		MainMenu = getImage.getResourceImage("data1/main_menu.png");
		bg = getImage.getResourceImage("data1/bg.jpg");
		bg2 = getImage.getResourceImage("data1/bg2.png");
		bg3 = getImage.getResourceImage("data1/bg3.png");
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}

	public void gameUpdate() {
		if (gameState == GAME_PLAYING_STATE) {
			land.update();
			mainCharacter.update();
			enemyManager.update();
			if (enemyManager.isCollision()) {
				mainCharacter.health();
				if (mainCharacter.hp <= 0){
					gameState = GAME_OVER_STATE;
					mainCharacter.dead(true);
				}
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(MainMenu, 0,-30,null);
		g.setColor(Color.yellow);
		g.setFont(new Font("OCR A Extended", Font.BOLD, 30));
		g.drawString("Please SPACE BAR to continue", 235,380);
		switch (gameState) {
		case GAME_PLAYING_STATE:
		case GAME_OVER_STATE:
			g.drawImage(bg, 0, -60, null);
			if (mainCharacter.score >= 100){
				g.drawImage(bg2, 0, -60, null);
				mainCharacter.setSpeedX(10);
				if (mainCharacter.score >= 200) {
					g.drawImage(bg3, 0, -60, null);
					mainCharacter.setSpeedX(14);
					if (mainCharacter.score >= 400){
						mainCharacter.setSpeedX(16);
					}
				}
			} else {
				mainCharacter.setSpeedX(6);
			}
			land.draw(g);
			enemyManager.draw(g);
			mainCharacter.draw(g);
			g.setFont(new Font("OCR A Extended", Font.BOLD, 20));
			g.setColor(Color.GRAY);
			g.fillRect(40, 10, 300, 80);
			g.setColor(Color.GRAY);
			g.fillRect(135, 21, 200, 25);
			g.setColor(Color.GREEN);
			g.fillRect(135, 21, mainCharacter.hp, 25);
			g.setColor(Color.BLACK);
			g.drawRect(135, 21, 200, 25);
			g.setColor(Color.BLACK);
			g.drawString("HP    :       "+ mainCharacter.hp, 45, 40);
			g.setColor(Color.BLACK);
			g.drawString("SCORE : " + mainCharacter.score, 45, 70);
			if (gameState == GAME_OVER_STATE) {
				g.drawImage(gameOverButtonImage, 50, 30, null);
				g.drawImage(replayButtonImage, 440, 400, null);
			}
			break;
		}
	}

	@Override
	public void run() {
		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;
		
		int msSleep;
		int nanoSleep;

		long endProcessGame;
		long lag = 0;

		while (true) {
			gameUpdate();
			repaint();
			endProcessGame = System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isKeyPressed) {
			isKeyPressed = true;
			switch (gameState) {
			case START_GAME_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
				}
				break;
			case GAME_PLAYING_STATE:
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					mainCharacter.jump();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					mainCharacter.down(true);
				}
				break;
			case GAME_OVER_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
					resetGame();
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == GAME_PLAYING_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mainCharacter.down(false);
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void resetGame() {
		enemyManager.reset();
		mainCharacter.dead(false);
		mainCharacter.reset();
	}
}
