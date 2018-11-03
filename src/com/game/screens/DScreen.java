package com.game.screens;

/**
 * @author Jaivir Panesar
 * @version 01/18/2018
 *
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.game.main.ReadAndWrite;

public class DScreen extends Screen implements ReadAndWrite{
	
	
	private static final long serialVersionUID = 6999864503032630389L;

	private static String action = new String();
	
	private JFrame frame;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	
	private boolean running;
	private Image i;
	
	private String scString = new String();
	
	public DScreen() {
		super();
	}
	
	/**
	 * starts the current death screen screen
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
	 * stops the current meny screen
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
		
		readFrom("score.txt");
		
		button = new JButton("Restart");
		button.setBorder(null);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
		button.setActionCommand("Restart");
		button.setBounds(100, 75, 400, 100);
		frame.add(button);
		
		button = new JButton("Quit to Menu");
		button.setBorder(null);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
		button.setActionCommand("qtm");
		button.setBounds(780, 75, 400, 100);
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
	
	/** runs the death screen screen
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
	 * renders pictures in the death screen screen
	 * @see com.game.screens.Screen#render()
	 */
	public void render() {
		
		try {
			i = ImageIO.read(getClass().getResourceAsStream("/assets/game_over_screen.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
		g = (Graphics2D) buffer.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g.drawImage(i, 0, 0, null);
			
		g.setColor(Color.WHITE);
		Font font = new Font("My font", Font.PLAIN, 20);
		g.setFont(font);
		g.drawString("Your Score: " + scString, getWidth() / 2 - 80, 500);
		
		g.dispose();
		buffer.show();
		
	}//render
	
	/**
	 * updates the action for a button
	 * @see com.game.screens.Screen#update()
	 */
	public void update() {
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
	    if(actionCommand == "Restart"){
	    	action = "Restart";
	    	this.stop();
	    	LevelOne lo = new LevelOne();
	    	lo.start();
	    }
	    
	    if(actionCommand == "qtm") {
	    	action = "Quit to Menu";
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFrom(String file) {

	File f = new File(file);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			scString = br.readLine();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}//Pause