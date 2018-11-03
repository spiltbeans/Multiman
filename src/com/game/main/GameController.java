package com.game.main;
/**
 * @author Eyas Valdez
 * @version 01/18/2018
 *
 */
public class GameController {
	
	int up;
	int down;
	int left;
	int right;
	int jump;
	int shoot;
	int sprint;
	
	public GameController(int up, int down, int left, int right, int jump, int shoot, int sprint){
		this.up = up;
		this.down = down;
		this.right = right;
		this.left = left;
		this.jump = jump;
		this.shoot = shoot;
		this.sprint = sprint;
		
	}

	/**
	 * @return the up
	 */
	public int getUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
	public void setUp(int up) {
		this.up = up;
	}

	/**
	 * @return the down
	 */
	public int getDown() {
		return down;
	}

	/**
	 * @param down the down to set
	 */
	public void setDown(int down) {
		this.down = down;
	}

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public int getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * @return the jump
	 */
	public int getJump() {
		return jump;
	}

	/**
	 * @param jump the jump to set
	 */
	public void setJump(int jump) {
		this.jump = jump;
	}

	/**
	 * @return the shoot
	 */
	public int getShoot() {
		return shoot;
	}

	/**
	 * @param shoot the shoot to set
	 */
	public void setShoot(int shoot) {
		this.shoot = shoot;
	}

	/**
	 * @return the sprint
	 */
	public int getSprint() {
		return sprint;
	}

	/**
	 * @param sprint the sprint to set
	 */
	public void setSprint(int sprint) {
		this.sprint = sprint;
	}
}
