package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.getImage;

public class EnemyManager {
	
	private BufferedImage zombie1;
	private BufferedImage zombie2;
	private BufferedImage bomb;
	private BufferedImage missile;
	private Random rand;
	
	private List<EnemyAbs> enemyAbs;
	private MainCharacter mainCharacter;
	
	public EnemyManager(MainCharacter mainCharacter) {
		rand = new Random();
		zombie1 = getImage.getResourceImage("data1/zombie1.png");
		zombie2 = getImage.getResourceImage("data1/zombie2.png");
		bomb = getImage.getResourceImage("data1/bomb.png");
		missile = getImage.getResourceImage("data1/missile.png");
		enemyAbs = new ArrayList<EnemyAbs>();
		this.mainCharacter = mainCharacter;
		enemyAbs.add(createEnemy());
	}
	
	public void update() {
		for(EnemyAbs e : enemyAbs) {
			e.update();
		}
		EnemyAbs enemy = enemyAbs.get(0);
		if(enemy.isOutOfScreen()) {
			mainCharacter.upScore();
			enemyAbs.clear();
			enemyAbs.add(createEnemy());
		}
	}
	
	public void draw(Graphics g) {
		for(EnemyAbs e : enemyAbs) {
			e.draw(g);
		}
	}
	
	private EnemyAbs createEnemy() {
		int type = rand.nextInt(4);
		if(type == 0) {
			return new Enemy(mainCharacter, 900, 540, zombie1.getWidth() - 10, zombie1.getHeight() - 10, zombie1);
		} else if (type == 1){
			return new Enemy(mainCharacter, 900, 540,zombie2.getWidth() - 10, zombie2.getHeight() - 10, zombie2);
		} else if (type == 2){
			return new Enemy(mainCharacter, 900, 540,bomb.getWidth() - 10, bomb.getHeight() - 10, bomb);
		} else {
			return new Enemy(mainCharacter, 900, 420,missile.getWidth() - 10, missile.getHeight() - 10, missile);
		}
	}
	
	public boolean isCollision() {
		for(EnemyAbs e : enemyAbs) {
			if (mainCharacter.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		enemyAbs.clear();
		enemyAbs.add(createEnemy());
	}
}
