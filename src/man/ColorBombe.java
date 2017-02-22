package man;

import java.awt.Color;
import java.util.Random;
import robocode.*;

/**
 * 
 * @author Ana Germano up201105083
 *
 */

public class ColorBombe extends AdvancedRobot {

	private double lastTurnEnergy = 100;
	private int moveDirection = 1;

	public void run() {
	
		// Configuration 
		setRandomColors();
		setTurnGunRight(360);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		setRandomColors();

		// virar o robot até ao inimigo para ficar frente a frente
		// 90 graus e angulo de 30
		setTurnRight(e.getBearing()+ 90 - 30 * moveDirection);

		// verificar se o inimigo disparou, para escapar ao tiro
		double energy = lastTurnEnergy - e.getEnergy();
		if (energy <= 3 && energy > 0) {
			// desviar de um tiro
			moveDirection = -moveDirection;
			setAhead((e.getDistance() / 4 + 25) * moveDirection);
		}

		// apontar a arma, é feito depois do desvio
		// pois o robot pode ficar numa posicao diferente
		setTurnGunRight(360 * -moveDirection);

		// calcula a intensidade e dispara
		calcFireRate(e);

		// actualizar a energia do inimigo
		lastTurnEnergy = e.getEnergy();
	}

	// calcula a intensidade do tiro
	private void calcFireRate(ScannedRobotEvent e) {
		if (e.getDistance() > getBattleFieldWidth() * 0.8) // 80%
			fire(0.1);
		else if (e.getDistance() > getBattleFieldWidth() * 0.6) // 60%
			fire(1);
		else if (e.getDistance() > getBattleFieldWidth() * 0.4) // 40%
			fire(2);
		else if (e.getDistance() > getBattleFieldWidth() * 0.1) // 10%
			fire(3);
	}

	// quando o robot bate na parede
	public void onHitWall(HitWallEvent e) {
		setTurnRight(100);
	}

	// quando o robot bate noutro robot
	public void onHitRobot(HitRobotEvent e) {
		setTurnRight(100);
	}

	// Quando o robo leva um tiro
	public void onHitByBullet(HitByBulletEvent e) {
		setTurnRight(100);
	}

	// muda aleatoriamente a cor do robot
	private void setRandomColors() {

		Random randomGenerator = new Random();
		int rValue = randomGenerator.nextInt(255);
		int gValue = randomGenerator.nextInt(255);
		int bValue = randomGenerator.nextInt(255);

		setBodyColor(new Color(rValue, gValue, bValue));
		setGunColor(new Color(255 - rValue, 255 - gValue, 255 - bValue));
		setRadarColor(new Color((int) ((255 - rValue) / 2), (int) ((255 - gValue) / 2), (int) ((255 - bValue) / 2)));
		setScanColor(new Color((int) ((255 - (rValue + 2)) / 2), (int) ((255 - (gValue + 2)) / 2),
				(int) ((255 - (bValue + 2)) / 2)));
		setBulletColor(new Color(rValue - 1, gValue - 1, bValue - 1));
	}
}
