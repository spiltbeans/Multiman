package com.game.screens;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.game.main.ReadAndWrite;


/**
 * @author Eyas Valdez
 * @version 01/06/2018
 *
 */
public class WScreen extends Screen implements ReadAndWrite{
	
	
	private static final long serialVersionUID = 6999864503032630389L;

	private static String action = new String();
	
	private JFrame frame;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	
	private String scString = new String();
	
	private boolean running;
	private Image i;

	
	public WScreen() {
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
		
		button = new JButton("Restart");
		button.setBorder(null);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
		button.setActionCommand("Restart");
		button.setBounds(100, 475, 400, 100);
		frame.add(button);
		
		button = new JButton("Quit to Menu");
		button.setBorder(null);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
		button.setActionCommand("qtm");
		button.setBounds(780, 475, 400, 100);
		frame.add(button);
		
		readFrom("score.txt");
		
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
	
	public void render() {
		
		try {
			i = ImageIO.read(getClass().getResourceAsStream("/assets/title_screens/you_win_screen.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
		g = (Graphics2D) buffer.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(i, 0, 0, null);
		Font font = new Font("My font", Font.PLAIN, 20);
		g.setFont(font);
		g.setColor(Color.RED);
		g.drawString("High Score: " + scString, getWidth() / 2 - 80, 50);
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