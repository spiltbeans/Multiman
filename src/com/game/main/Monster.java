package com.game.main;

import java.awt.Image;
/**
 * @author Eyas Valdez
 * @version 01/18/2018
 *
 */
public class Monster implements Entity{
	
	private Image image;
	private double health;
	private double damage;
	private int x;
	private int y;
	
	/**
	 * This is the constructor for the player object
	 * @param image - Image used for player
	 * @param health
	 * @param damage
	 * @param x - int X-Value used for player location
	 * @param y - int Y-Value used for player location
	 */
	public Monster(Image image, double health, double damage, int x, int y) {
		this.image = image;
		this.health = health;
		this.damage = damage;
		this.x = y;
		this.y = y;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public void setStats(Stats s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Stats getStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * This method returns the value of the health
	 * @return double value of health
	 */
	public double getHealth() {
		return health;
	}
	/**
	 * This method sets the value of the health
	 * @param health - double used to set the health
	 * @return void
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	/**
	 * This method returns the value of the health
	 * @return double value of health
	 */
	public double getDamage() {
		return damage;
	}
	/**
	 * This method sets the value of the damage
	 * @param damage - double used to set the damage
	 * @return void
	 */
	public void setDamage(double damage) {
		this.damage = damage;
	}

}
