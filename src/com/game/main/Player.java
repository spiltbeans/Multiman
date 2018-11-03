package com.game.main;

import java.awt.Image;

/**
 * @author Eyas Valdez
 * @version 01/17/2018
 *
 */
public class Player implements Entity{
	
	private Image image;
	private int x;
	private int y;
	private Stats s;
	
	private int velX = 0;
	private int velY = 0;
	


	
	/**
	 * This is the constructor for the player object
	 * @param image - Image used for player
	 * @param x - int X-Value used for player location
	 * @param y - int Y-Value used for player location
	 * @param s	- Stats object used to hold player information
	 **/
	public Player(Image image, int x, int y, Stats s) {
		
		
		this.image = image;
		this.x = x;
		this.y = y;
		this.s = s;
		
		
	}
	
	/**
	 * Returns the Stats object of the player used to hold status information
	 * @return Stats
	 */
	public Stats getStats() {
		return s;
	}

	/**
	 * Sets the X-Value used for player location
	 * @param x - int used for X-Value
	 * @return void
	 * 
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Sets the Y-Value used for the player location
	 * @param y - int used for Y-Value
	 */
	public void setY(int y) {
		this.y = y;
	}


	/**
	 * Gets the velocity player is traveling in the X-Axis
	 * @return velX - int velocity in X-Axis
	 */
	public int getVelX() {
		return velX;
	}



	/**
	 * Sets the velocity of the player in the X-Axis
	 * @param velX - int velocity to make the player move in the X-Axis
	 */
	public void setVelX(int velX) {
		this.velX = velX;
	}


	/**
	 * Gets the velocity player is traveling in the Y-Axis
	 * @return velY - int velocity in Y-Axis
	 */
	public int getVelY() {
		return velY;
	}


	/**
	 * Sets the velocity of the player in the Y-Axis
	 * @param velY - int velocity to make the player move in the Y-Axis
	 */
	public void setVelY(int velY) {
		this.velY = velY;
	}

	/**
	 * Returns the current X-Value the player is located
	 * @return int - X-Value location
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the current Y-Value the player is located
	 * @return int - Y-Value location
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Ticks updating players location
	 * @return void
	 */
	public void tick() {
		x += velX; 
		y += velY;
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
		this.s = s;
		
	}

	
}
