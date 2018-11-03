package com.game.main;

import java.awt.Image;

import com.game.main.Stats;

/**
 * @author Eyas Valdez
 * @version 01/18/2018
 *
 */
public interface Entity {
	
	
	/**
	 * Ticks any updates
	 * @return void
	 */
	public void tick();
	
	/**
	 * Returns entitys image
	 * @return image - Image of player
	 */
	public Image getImage();
	/**
	 * Sets image of entity
	 * @param image - Image used for entity
	 * @return void
	 */
	public void setImage(Image image);
	
	/**
	 * Sets the Stats object to hold entities status
	 * @param s - Stats used for entity
	 * @return void
	 */
	public void setStats(Stats s);
	/**
	 * Returns the Stats object used to hold entity status
	 * @return
	 */
	public Stats getStats();
	
	
	/**
	 * This returns the integer X-Value location
	 * @return int - X-Value
	 */
	public int getX();
	/**
	 * This returns the integer Y-Value location
	 * @return int - Y-Value
	 */
	public int getY();
	
	/**
	 * This sets the integer Y-Value location
	 * @param y - int used for Y-Value
	 * @return void
	 */
	public void setY(int y);
	/**
	 * This sets the integer X-Value location
	 * @param x - int used for X-Value
	 * @return void
	 */
	public void setX(int x);

}//Entity
