package com.game.screens;

/**
 * @author Jaivir Panesar
 * @version 01/18/2018
 *
 */

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.game.main.ReadAndWrite;

public class Pause extends Screen implements ReadAndWrite{
	
	
	private static final long serialVersionUID = 6999864503032630389L;

	private static String action = new String();
	
	
	private JFrame frame;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	private Image i;
	private Dimension size;
	
	private boolean running;
	private int screenH = 500;
	private int screenW = 720;

	
	public Pause() {

		size = new Dimension(screenW,screenH);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
	}
	
	/**
	 * starts the current pause screen
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
	 * stops the current pause screen
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
	
//public void keyPressed(KeyEvent e) {
//		
//		int keyCode = e.getKeyCode();
//
//		if(keyCode == KeyEvent.VK_ESCAPE){
//			Lev
//			this.stop();
//		}
//		
//	}//keyPressed
	
	/**
	 * initializez all the buttons, images and adds the to the frame
	 * @see com.game.screens.Screen#init()
	 **/
	public void init() {
		frame = new JFrame();
		frame.setUndecorated(true);
		JButton button;
		
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
		
		ImageIcon pButton = new ImageIcon("res/assets/buttons/resume_button.png");
		button = new JButton(pButton);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(this);
		button.setActionCommand("Resume");
		button.setBounds(screenW / 2 - 125, 200, 256, 56);
		frame.add(button);
		
//	    ImageIcon qButton = new ImageIcon("res/assets/buttons/quit_game_button.png");
//		button = new JButton(qButton);
//		button = new JButton("Options");
//		button.setOpaque(false);
//		button.setContentAreaFilled(false);
//		button.setBorderPainted(false);
//		button.addActionListener(this);
//		button.setActionCommand("Options");
//		button.setBounds(((screenW / 2) - 125), 275, 256, 56);
//		frame.add(button);
//		
//	    ImageIcon qButton = new ImageIcon("res/assets/buttons/quit_game_button.png");
//		button = new JButton(qButton);
//		button = new JButton("Save and Quit");
//		button.setOpaque(false);
//		button.setContentAreaFilled(false);
//		button.setBorderPainted(false);
//		button.addActionListener(this);
//		button.setActionCommand("snq");
//		button.setBounds(((screenW / 2) - 125), 350, 256, 56);
		frame.add(button);
		
	    ImageIcon qButton = new ImageIcon("res/assets/buttons/quit_game_button.png");
		button = new JButton(qButton);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(true);
		button.addActionListener(this);
		button.setActionCommand("Quit");
		button.setBounds(((screenW / 2) - 125), 275, 256, 56);
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
	
	/** runs the pause screen
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
	 * renders pictures in the pause screen
	 * @see com.game.screens.Screen#render()
	 */
	public void render() {
		
		try {
			i = ImageIO.read(getClass().getResourceAsStream("/assets/pause_screen.png"));
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
		if(action != null) {
			System.out.println(action);
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
	    if(actionCommand == "Resume"){
	    	action = "Resume";
	    	writeTo("paused.txt", "");
	    	this.stop();
	    }	
	    
	    if(actionCommand == "Options"){
	    	action = "Options";
	    	OptionsP op = new OptionsP();
	    	op.start();
	    	this.stop();
	    }
	    
	    if(actionCommand == "snq"){
	    	action = "Save and Quit";
	    	//LevelOne.save("save.txt");
	    	this.stop();
	    }	    
	    
	    if(actionCommand == "Quit") {
	    	action = "Quit";
	    	this.stop();
	    	Menu m = new Menu();
	    	m.start();
	    }	    	    
	    
	}//actionPerformed
	
	/**
	 * returns what button is pressed
	 * @see com.game.screens.Screen#getAction()
	 */
	public String getAction() {
		
		return action;
		
	}//getAction

	@Override
	public void bg(Image im, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTo(String file, String text) {
		File f = new File(file);
		BufferedWriter bw;
		
		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(text);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void readFrom(String file) {
		// TODO Auto-generated method stub
		
	}
}//Pause