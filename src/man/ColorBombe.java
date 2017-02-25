package man;

import java.awt.Color;
import java.util.Random;
import robocode.*;
import robocode.util.Utils;

/**
 * 
 * @author Ana Germano up201105083
 *
 */

public class ColorBombe extends AdvancedRobot {

	// Distância ao inimigo (relativamente à largura do campo)
	// a que dispara com o máximo de potência
	private final int MAX_FIRE_DIST = 30;
	// Distância ao inimigo (relativamente à largura do campo)
	// a que dispara com o mínimo de potência definida.
	private final int MIN_FIRE_DIST = 70;
	// Potência máxima de disparo
	private final int MAX_FIRE = 3;
	// Potência mínima de disparo
	private final int MIN_FIRE = 2;
	//Distância que o robô percorre de cada vez
	private final int MOVE_DIST = 100;

	private double lastTurnEnergy = 100;
	private int moveDirection = 1;

	public void run() {
		setRandomColors();
		setTurnRadarRight(Integer.MAX_VALUE);
	}

	/**
	 * Quando encontra um robot vira até ao inimigo para ficar na horizontal e arma frente a
	 * frente. Verifica se o inimigo disparou usando a energia para escapar do
	 * tiro. Apontar a arma, é feito depois do desvio, pois o robot pode ficar
	 * numa posicao diferente E actualiza a energia
	 * 
	 * @param enemy é o evento ao encontrar um robô inimigo.
	 */

	public void onScannedRobot(ScannedRobotEvent enemy) {

		//Verifica se o enimigo disparou
		double energy = lastTurnEnergy - enemy.getEnergy();
		if (energy <= 3 && energy > 0) {
			//movimento zig zag
			moveDirection = -moveDirection;
			// 90 graus e angulo de 30
			setTurnRight(enemy.getBearing() + 90 - 30 * moveDirection);
			setAhead(MOVE_DIST * moveDirection);
		} else{
			//Se o inimigo não disparou, move-se em direção a ele 
			setTurnRight(enemy.getBearing());
			setAhead(MOVE_DIST);
		}

		//Aponta para o inimigo e dispara
		setTurnGunRight(aimGun(enemy));
		fire(calcFireRate(enemy));

		lastTurnEnergy = enemy.getEnergy();
	}
	/**
	 * Calcula a rotação necessária para que a arma aponte para um robô alvo.
	 * @param enemy é o robô ao qual se quer apontar a arma
	 * @return movimento para a direita, em graus, necessário para apontar a arma ao inimigo
	 */
	
	private double aimGun(ScannedRobotEvent enemy){
		
		double absEnemyAngle=enemy.getBearing()+getHeading();//ângulo entre o inimigo e o referêncial
		double gunTurnDegrees = Utils.normalRelativeAngleDegrees(absEnemyAngle- getGunHeading())
				+ enemy.getVelocity() * Math.sin(enemy.getHeading());//inclui ajuste devido ao movimento esperado do inimigo
		
		return gunTurnDegrees;
	}

	/**
	 * Calcula a intensidade do tiro de acordo com a distancia em
	 * que o robot inimigo se encontra, ou seja, quanto menor a
	 * distancia, mais intensidade é do tiro
	 * 
	 * @param enemy é o robô inimigo
	 * @return potência do tiro
	 */

	private double calcFireRate(ScannedRobotEvent enemy) {

		if (enemy.getDistance() < getBattleFieldWidth() * MAX_FIRE_DIST / 100)
			return MAX_FIRE;
		if (enemy.getDistance() > getBattleFieldWidth() * MIN_FIRE_DIST / 100)
			return 0;
		return MIN_FIRE;
	}

	
	/**
	 * Muda aleatóriamente as cores das componentes do robô
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
		setBulletColor(new Color(rValue - randomGenerator.nextInt(rValue), 
				gValue - randomGenerator.nextInt(gValue), 
				bValue - randomGenerator.nextInt(bValue)));
	}
}