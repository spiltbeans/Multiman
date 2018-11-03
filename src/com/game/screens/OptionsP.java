package com.game.screens;

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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Jaivir Panesar
 * @version 01/17/2018
 *
 */
public class OptionsP extends Screen{
	
	
	private static final long serialVersionUID = 6999864503032630389L;

	private static String action = new String();
	
	private JFrame frame;
	private Dimension size;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	private Image i;
	
	private boolean running;
	private int screenH = 720;
	private int screenW = 1280;

	
	public OptionsP() {
		
		size = new Dimension(screenW,screenH);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
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
		
		ImageIcon bButton = new ImageIcon("res/assets/buttons/back_button.png");
		button = new JButton(bButton);
		button.addActionListener(this);
		button.setActionCommand("Back");
		button.setBounds(65, 600, 100, 65);
		frame.add(button);
		
		button = new JButton("Sound");
		button.addActionListener(this);
		button.setActionCommand("Sound");
		button.setBounds(screenW / 2 - 200, 250, 400, 100);
		frame.add(button);
		
		button = new JButton("Controls");
		button.addActionListener(this);
		button.setActionCommand("Controls");
		button.setBounds(screenW / 2 - 200, 400, 400, 100);
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
			i = ImageIO.read(getClass().getResourceAsStream("/assets/options_screen.png"));
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
	
	public void update() {
		if(action != null) {
			System.out.println(action);
			action = null;
		}else {
			return;
		}
		
		
	}//update

	private int n = 1;
	public void actionPerformed(ActionEvent e){
	    String actionCommand = ((JButton) e.getSource()).getActionCommand();
	    if(actionCommand == "Sound"){
	    	action = "Sound";
	    	if(n % 2 == 0) {
	    		System.out.println("Sount Toggle: Off");
	    	}else {
	    		System.out.println("Sound Toggle: On");
	    	}
	    	n++;
	    }
	    
	    if(actionCommand == "Controls") {
	    	action = "Controls";
	    	
	    }
	    
	    if(actionCommand == "Back") {
	    	action = "Back";
	    	Pause p = new Pause();
	    	p.start();
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