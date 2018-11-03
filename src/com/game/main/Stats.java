package com.game.main;
/**
 * @author Eyas Valdez
 * @version 01/17/2018
 *
 */
import java.awt.Rectangle;

/**
 * @author Eyas Valdez
 * @version 01/06/2018
 *
 */
public class Stats {
	
	double health;
	double damage;
	Rectangle hitbox;
	public Stats(double health, double damage, Rectangle hitbox) {
		
		this.health = health;
		this.damage = damage;
		this.hitbox = hitbox;
	}

	/**
	 * @return the hitbox
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * @param hitbox the hitbox to set
	 */
	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getHealth() {
		return health;
	}

	public double getDamage() {
		return damage;
	}
	
	

}
