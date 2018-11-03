package com.game.main;

import com.game.screens.Menu;

/**
 * @author Eyas Valdez
 * @version 01/18/2018
 *
 */
public class Game {
	
	
	private static Menu m = new Menu();
	
	/**
	 * Main method that starts the game
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		m.start();
	}//main
	
	/**
	 * This method updates the screen based on what buttons are clicked
	 * @return void
	 */
	public void updateScreen() {
		
		if(m.getAction() == "play") {
			m.stop();
		}else if(m.getAction() == "Option") {
			m.stop();
		}else if(m.getAction() == "Quit"){
			m.stop();
		}
	}//updateScreen

}//Game
