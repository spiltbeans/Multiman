package com.game.screens;

/**
 * @author Jaivir Panesar
 * @version 01/18/2018
 *
 */

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Screen extends Canvas implements Runnable, ActionListener {
	
	private static final long serialVersionUID = 6999864503032630389L;
	
	private Dimension size;
	private int screenH;
	private int screenW;

	/**
	 * this constructor creates a basic 1208x720 screen
	 *
	 */
	public Screen() {
		
		screenH = 720;
		screenW = 1280;
		size = new Dimension(screenW,screenH);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
	}
	
	public abstract void start();//start
	
	
	public abstract void stop();//stop
	
	public abstract void init();//init
	
	public abstract void run();//run
	
	public abstract void render();//render
	
	public abstract void bg(Image im, int x, int y);
	
	public abstract void update();//update

	
	public abstract void actionPerformed(ActionEvent e);//actionPerformed
	
	public abstract String getAction();//getAction
}
