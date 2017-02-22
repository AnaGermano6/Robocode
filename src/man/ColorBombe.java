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
	private int graus = 360;
	
	/**
	 * Este metodo faz com que o robot se mova
	 * e tem as suas configurações iniciais
	 */
	
	public void run() {
		setRandomColors();
		setTurnGunRight(graus);
	}
	
	/**
	 * Este metedo é usado quando um robot é detetado no radar.
	 * Quando encontra um robot vira até ao inimigo para ficar frente a frente.
	 * Verifica se o inimigo disparou usando a energia para escapar do tiro.
	 * Apontar a arma, é feito depois do desvio, pois o robot pode ficar numa posicao diferente
	 * E actualiza a energia
	 * 
	 * @param e é um evento para quando um robot é detetado no radar
	 */

	public void onScannedRobot(ScannedRobotEvent e) {
		//setRandomColors();

		// 90 graus e angulo de 30
		setTurnRight(e.getBearing()+ 90 - 30 * moveDirection);

		
		double energy = lastTurnEnergy - e.getEnergy();
		if (energy <= 3 && energy > 0) {
			// desviar de um tiro
			moveDirection = -moveDirection;
			setAhead((e.getDistance() / 4 + 25) * moveDirection);
		}

		moveDirection=-moveDirection;
		setTurnGunRight(graus*moveDirection);

		// calcula a intensidade e dispara
		calcFireRate(e);

		lastTurnEnergy = e.getEnergy();
	}
	
	/**
	 * Este metodo calcula a intensidade do tiro de acordo 
	 * com a distancia em que o robot inimigo se encontra
	 * 
	 * Quanto maior a distancia, menos intensidade do tiro
	 * Quanto menor a distancia, mais intensidade é do tiro 
	 * 
	 * @param e é um evento para quando um robot é detetado no radar
	 */

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
	
	/**
	 * Quando o robot bate na parede e poder sair
	 */
	public void onHitWall(HitWallEvent e) {
		setTurnRight(100);
	}
	
	
	/**
	 * Quando o robot bate noutro robot 
	 */
	public void onHitRobot(HitRobotEvent e) {
		setTurnRight(100);
	}

	
	/**
	 * Quando o robo leva um tiro
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		setTurnRight(100);
	}
	
	
	/**
	 * Este metodo muda de cor aleatoriamente do robot
	 * usando numero aleatorios entre 0 e 254
	 * 
	 * criando cores RGB 
	 * 
	 */
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
