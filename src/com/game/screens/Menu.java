package com.game.screens;

/**
 * @author Jaivir Panesar
 * @version 01/17/2018
 */


import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.game.main.Audio;
import com.game.main.Game;

public class Menu extends Screen{
	
	private static final long serialVersionUID = 8703899486894107165L;
	
	private Game game = new Game();
	private static String action = new String();
	
	private JFrame frame;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	private Image i;
	
	private Audio audio;
	private boolean running;
	

	
	public Menu() {
		super();
	}
	
	/**
	 * starts the current menu screen
	 * @see com.game.screens.Screen#start()
	 **/
	public synchronized void start(){
		if(running) {
			return;
		}else {
		thread = new Thread(this);
		running = true;
		thread.start();
		}
		
	}//start
	
	
	/** 
	 * stops the current menu screen
	 * @see com.game.screens.Screen#stop()
	 **/
	public synchronized void stop() {
		if(running == false) {
			return;	
		}else {
			running = false;
			thread.interrupt();
			frame.dispose();
		}
		
		
	}//stop
	
	/**
	 * initializez all the buttons, images and adds the to the frame
	 * @see com.game.screens.Screen#init()
	 **/
	public void init() {
		frame = new JFrame();
		frame.setUndecorated(true);
		JButton button;
		
		audio = new Audio("/sound/main_screen.wav");
		
		audio.play();
		Image icon = null;
		BufferedImage cursor = null;
		try {
			
			
			icon = ImageIO.read(getClass().getResourceAsStream("/assets/icon.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		try {
			cursor = ImageIO.read(getClass().getResourceAsStream("/assets/cursor.gif"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
		Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "My Cursor");
		
		frame.setCursor(cur);
		frame.setIconImage(icon);
		
		ImageIcon pButton = new ImageIcon("res/assets/buttons/play_game.png");
		button = new JButton(pButton);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.addActionListener(this);
		button.setActionCommand("Play Game!");
		button.setBounds((getWidth() / 2) - 250, 485, 500, 100);
		frame.add(button);
		
//		ImageIcon qButton = new ImageIcon("res/assets/buttons/quit_game_button.png");
//		button = new JButton(qButton);
//		button = new JButton("Options");
//		button.setOpaque(false);
//		button.setContentAreaFilled(false);
//		button.addActionListener(this);
//		button.setActionCommand("Options");
//		button.setBounds(((getWidth() / 2) - 125), 595, 250, 50);
//		frame.add(button);
//		
	    ImageIcon qButton = new ImageIcon("res/assets/buttons/quit_game_button.png");
		button = new JButton(qButton);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.addActionListener(this);
		button.setActionCommand("Quit Game");
		button.setBounds(((getWidth() / 2) - 125), 595, 250, 50);
		frame.add(button);
		
		
		frame.add(this); 
		frame.setResizable(false);
		frame.pack();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stop();
			}
		});
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		createBufferStrategy(3); 
		buffer = getBufferStrategy();
		
		
	}//init
	
	/** 
	 * changes the button
	 * @see com.game.screens.Screen#bg(java.awt.Image, int, int)
	 **/
	public void bg(Image im, int x, int y) {
		g = (Graphics2D) buffer.getDrawGraphics();		
		
		g.drawImage(im, x, y, null);	
				
		g.dispose();
		buffer.show();
	}
	
	/** runs the menu screen
	 * @see com.game.screens.Screen#run()
	 **/
	public void run() {
		new Menu().setVisible(true);

		init();
		
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		
		long timerS = System.currentTimeMillis();
		long timerM = System.currentTimeMillis();
		
		
	while(running) {
			
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			
			if(delta >= 1) {
				update();
				delta--;
			}
			render();
			if(System.currentTimeMillis() - timerS > 1000) {
				timerS += 1000;
			}
			
			if(System.currentTimeMillis() - timerM > 100) {
				timerM += 100;
				
			}
			
			
		}
		
	}//run
	
	/**
	 * renders pictures in the menu screen
	 * @see com.game.screens.Screen#render()
	 */
	public void render() {
		try {
			i = ImageIO.read(getClass().getResourceAsStream("/assets/title_screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g = (Graphics2D) buffer.getDrawGraphics();		
		
		g.drawImage(i, 0, 0, null);	
				
		g.dispose();
		buffer.show();
		
	}//render
	
	/**
	 * updates the action for a button
	 * @see com.game.screens.Screen#update()
	 */
	public void update() {
		
		if(audio.getClip().isRunning() == false) {
			audio.play();
		}
		if(action != null) {
			action = null;
		}else {
			return;
		}
		
		
	}//update

	
	/**
	 * action listener for clicks
	 * @see com.game.screens.Screen#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e){
	    String actionCommand = ((JButton) e.getSource()).getActionCommand();
	    if(actionCommand == "Play Game!"){
	    	action = "play";
	    	audio.stop();
	    	Play p = new Play();
	    	p.start();
	    	this.stop();
	    }	
	    
//	    if(actionCommand == "Options") {
//	    	action = "Options";
//	    	Options o = new Options();
//	    	o.start();
//	    	this.stop();
//	    }
	    
	    if(actionCommand == "Quit Game") {
	    	action = "quit";
	    	this.stop();
	    	System.exit(0);
	    }
	    
	    game.updateScreen();
	}//actionPerformed
	
	/**
	 * returns what button is pressed
	 * @see com.game.screens.Screen#getAction()
	 */
	public String getAction() {
		
		return action;
		
	}//getAction
}//Menu