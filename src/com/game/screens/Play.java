package com.game.screens;

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

/**
 * @author Jaivir Panesar
 * @version 01/17/2018
 *
 */
public class Play extends Screen{
	
	
	private static final long serialVersionUID = 6999864503032630389L;

	private static String action = new String();
	
	private JFrame frame;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	private Image i;
	
	private boolean running;

	private Audio audio;
	
	public Play() {
		super();
	}
	
	public synchronized void start(){
		if(running) {
			return;
		}else {
		thread = new Thread(this);
		running = true;
		thread.start();
		}
		
	}//start
	
	
	public synchronized void stop() {
		if(running == false) {
			return;	
		}else {
			running = false;
			thread.interrupt();
			frame.dispose();
		}
		
		
	}//stop
	
	public void init() {
		frame = new JFrame();
		frame.setUndecorated(true);
		JButton button;
		
		audio = new Audio("/sound/play_screen.wav");
		
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
		
//		button = new JButton("New Game");
//		button.setBackground(Color.RED);
//		button.addActionListener(this);
//		button.setActionCommand("New");
//		button.setBounds(getWidth() / 2 - 300, 475, 600, 100);
//		frame.add(button);
		
//		button = new JButton("Load Game");
//		button.setBackground(Color.RED);
//		button.addActionListener(this);
//		button.setActionCommand("Load");
//		button.setBounds(getWidth() / 2 - 300, 585, 600, 100);
//		frame.add(button);
		
		ImageIcon bButton = new ImageIcon("res/assets/buttons/back_button.png");
		button = new JButton(bButton);
		button.addActionListener(this);
		button.setActionCommand("Back");
		button.setBounds(65, 600, 100, 65);
		frame.add(button);
		
		ImageIcon pButton = new ImageIcon("res/assets/buttons/play_button.png");
		button = new JButton(pButton);
		button.addActionListener(this);
		button.setActionCommand("cont");
		button.setBounds(1115, 600, 100, 65);
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
	
	public void run() {
		new Play().setVisible(true);
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
	
	public void render() {
		try {
			i = ImageIO.read(getClass().getResourceAsStream("/assets/controls_screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g = (Graphics2D) buffer.getDrawGraphics();
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
		
		g.drawImage(i, 0, 0, null);	
				
		g.dispose();
		buffer.show();
		
	}//render
	
	public void bg(Image im) {
		g = (Graphics2D) buffer.getDrawGraphics();
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
		
		g.drawImage(im, 0, 0, null);	
				
		g.dispose();
		buffer.show();
	}
	
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

	
	public void actionPerformed(ActionEvent e){
	    String actionCommand = ((JButton) e.getSource()).getActionCommand();
	    if(actionCommand == "cont"){
	    	action = "Continue";
	    	LevelOne lo = new LevelOne();
	    	lo.start();
	    	audio.stop();
	    	this.stop();
	    }
	    
//	    if(actionCommand == "load") {
//	    	action = "Load";
//	    }	
	    
	    if(actionCommand == "Back") {
	    	action = "Back";
	    	audio.stop();
	    	Menu m = new Menu();
	    	m.start();
	    	this.stop();
	    }
	    
	    
	}//actionPerformed
	
	public String getAction() {
		
		return action;
		
	}//getAction

	@Override
	public void bg(Image im, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	


}//Play