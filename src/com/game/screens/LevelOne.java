 package com.game.screens;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.game.main.Audio;
import com.game.main.Monster;
import com.game.main.Player;
import com.game.main.ReadAndWrite;
import com.game.main.Stats;

/**
 * @author Eyas Valdez
 * @version 01/18/2018
 *
 */

public class LevelOne extends Screen implements Runnable, KeyListener, ReadAndWrite{
	
	private static final long serialVersionUID = 6659391085186518355L;
	private int score;
	private boolean paused = false;
	private boolean audioLoop = true;
	public HashMap<String, Audio> sfx;
	
	private String pausedStatus = new String();
	private JFrame frame;
	private Dimension size;
	private Thread thread;
	private Graphics2D g;
	private BufferStrategy buffer;
	
	private Stats s;
	private Player p;
	private Rectangle playerHitBox = new Rectangle(0, 0, 52, 53);;
	private Rectangle bullet = new Rectangle(0, 0, 8, 8);
	private Image image;
	private Image background;
	
	private Image plantIdleOne;
	private Image plantIdleTwo;
	
	private Image plantAttackTwo;
	
	private Pause pause = new Pause();
	
	private boolean taunt = false;
	
	private BufferedImage cursor;
	private Image icon;
	
	//Hitboxes for enimies	
	private Rectangle flowerOne;
	private Rectangle flowerTwo;
	private Rectangle flowerThree;
	
	//Monsters
	
	private Monster flowOne;
	private Monster flowTwo;
	private Monster flowThree;
	
	//will be used for the hitboxes on the ground
	private Rectangle floorOne = new Rectangle(0, 611, 1286, 700);
	private Rectangle obsticleOne = new Rectangle(528, 590, 80, 20);
	private Rectangle floorTwo = new Rectangle(1450 ,541, 885, 174);
	private Rectangle floorThree = new Rectangle(2591, 685, 204, 32);
	private Rectangle floorFour = new Rectangle (2915, 647, 203, 72);
	private Rectangle floorFive = new Rectangle(3254, 599, 203, 118);
	private Rectangle floorSix = new Rectangle(3643, 535, 204, 181);
	private Rectangle floorSeven = new Rectangle(4010, 535, 1120, 181);
	
	public  int monstersKilled = 0;
	private Rectangle healthBar;
	
	private String lastDirection = "RIGHT";
	private boolean shootBullet;
	private boolean running;
	private boolean falling = true;
	private boolean canJump;
	private boolean bulletSpawned = false;
	private boolean moving = false;
	
	private boolean plantAttack = false;
	
//	private int tempMouse = 1;
//	private int tempmX;
//	private int tempmY;
	
	private int screenH = 720;
	private int screenW = 1280;
	private int aniState = 1;
	private int plantState = 1;
	private int aniPlant = 1;
	private int seconds = 0;
	private int milliseconds = 0;
	private int levelX = 0;
	private int levelY = -700;
	private int aniTime = 1;
	private int gravity = 1;
	private int fallSpeed = 0 ;
	private int jumpPower = -15;
	private int runningSpeed = 1;
	private int[] time = new int[2];
	
	/**
	 * This is a constructor that sets dimension of screen
	 */
	public LevelOne() {
		
		size = new Dimension(screenW,screenH);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		
	}//LevelOne
	
	/**
	 * This method initializes the class and begins the game
	 * @return void
	 * 
	 */
	public synchronized void start() {
		audioLoop = true;
		if(running) {
			return;
		}else {
		thread = new Thread(this);
		running = true;
		thread.start();
		}
		
	}//start
	
	/**
	 * This method disposed of the class and ends the game
	 * @return void
	 * 
	 */
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
	 * This method initializes properties of the class canvas and object information
	 * @return void
	 */
	public void init() {
		
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.addKeyListener(this);
		
		sfx = new HashMap<String, Audio>();
		sfx.put("shoot", new Audio( "/sound/shoot.wav"));
		sfx.put("background", new Audio("/sound/level_one.wav"));
		sfx.put("death", new Audio("/sound/death.wav"));
		sfx.put("damage", new Audio("/sound/take_damage.wav"));
		sfx.get("background").play();
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
		
		renderImages();
		initMonster();
		
		Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "My Cursor");
		
		frame.setCursor(cur);
		frame.setIconImage(icon);
		animateFlower();
		animateP();
		
		
		healthBar = new Rectangle(150, 20, 250,35);
		s  = new Stats(1, 0.1, playerHitBox);
		p = new Player(image, 60, 60, s);

		
	}//init
	
	
	/**
	 * This method will be called by the Runnable implementation
	 * This method will keep track of time, call the tick, update, and render methods
	 * while keeping the game at 60 frames per second
	 * @return void
	 */
	public void run() { 
		
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
				tick();
				update();
				delta--;
			}
			render();
			if(System.currentTimeMillis() - timerS > 1000) {
				timerS += 1000;
				if(paused == false) {
					seconds ++;
					animateFlower();
				}
			}
			
			if(System.currentTimeMillis() - timerM > 100) {
				timerM += 100;
				if(paused == false) {
					milliseconds ++;
				}
			}
			
			
		}
			
		time[0] = seconds;
		time[1] = milliseconds;
	}//run
	
	/**
	 * This method will update background information, such as object location
	 * This method will call the Player.tick method used to update location to achieve movement
	 * @return void
	 */
	public void tick() {
		if(sfx.get("background").getClip().isRunning() == false && audioLoop == true) {
			sfx.get("background").play();
		}
		readFrom("paused.txt");
		playerHitBox.setLocation(p.getX(), p.getY());
		floorOne.setLocation(levelX, 611);
		obsticleOne.setLocation(levelX + 528, 590);
		floorTwo.setLocation(levelX + 1450, 541);
		floorThree.setLocation(levelX + 2591, 685);
		floorFour.setLocation(levelX + 2915, 647);
		floorFive.setLocation(levelX + 3254, 599);
		floorSix.setLocation(levelX + 3643, 535);
		floorSeven.setLocation(levelX + 4010, 535);	
		healthBar.setBounds(150, 20, (int)(250 * s.getHealth()), 35);
		
		flowerOne.setLocation(levelX + 1173, 554);
		flowerTwo.setLocation(levelX + 2691, 632);
		flowerThree.setLocation(levelX + 4210, 486);
		
		p.tick();
	}//tick
	
	/**
	 * This method draw all graphics onto the canvas and will keep track of
	 * what animation should be drawn
	 * @return void
	 */
	public void render() {
		
		//sets up graphics tool
		g = (Graphics2D) buffer.getDrawGraphics();
				
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int min = seconds / 60;
		int sec = seconds - (min*60);
		int milli = milliseconds - (seconds * 10);
		
		//rendering background
		g.drawImage(background, levelX, levelY, null);
		g.setColor(Color.WHITE);
		g.fillRect(150, 20, 250, 35);
		Font font = new Font("My font", Font.PLAIN, 20);
		g.setFont(font);
		g.drawString("Current Health: ", 10, 40);
		g.drawString(min + ":" + sec + ":" + milli, 1000,40);
		if(healthBar.width < 50) {
			g.setColor(Color.RED);
		}else {
		g.setColor(Color.GREEN);
		}
		g.fill(healthBar);
		g.setColor(Color.BLACK);
		g.drawRect(150, 20, 250, 35);
		if(flowOne.getHealth() > 0) {
			g.drawImage(flowOne.getImage(), flowerOne.x,flowerOne.y,null);
		}else {
			flowOne.setDamage(0);
		}
		if(flowTwo.getHealth() > 0) {
			g.drawImage(flowTwo.getImage(), flowerTwo.x,flowerTwo.y,null);
		}else {
			flowTwo.setDamage(0);
		}
		if(flowThree.getHealth() > 0) {
			g.drawImage(flowThree.getImage(), flowerThree.x,flowerThree.y,null);
		}else {
			flowThree.setDamage(0);
		}
		
		
		//renders bullet if shot
		
		if(shootBullet) {
			g.setColor(Color.RED);
			g.fillOval(bullet.x, bullet.y, bullet.width,bullet.height);
			
					
		}
				
		//animates character every second
		aniPlant ++;
		if(moving) {
			if(milliseconds == aniTime) {
				animateP();
				aniTime  ++;
			}
		}else {
			if(milliseconds == aniTime) {
				if(lastDirection.equals("RIGHT")) {
				try {
					image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningRThree.png"));
				} catch (IOException ex) {
					
					ex.printStackTrace();
				}
				}else {
					try {
						image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningLThree.png"));
					} catch (IOException ex) {
						
						ex.printStackTrace();
					}
				}
				aniTime  ++;
			}
		}
		
				
		//draws player	
		g.drawImage(image, p.getX(), p.getY(), null);	
		
		if(taunt) {
			
			g.setColor(Color.YELLOW);
			font = new Font("My font", Font.PLAIN, 15);
			g.setFont(font);
			g.drawString("At least we arent playing a tower defence!", playerHitBox.x, playerHitBox.y);
		}
		//clears screen
		g.dispose();
		buffer.show();
				
		
	}//render
	
	
	/**
	 * This method updates system events such as; object collision, player shooting etc.
	 * This method will call the tick method again to update location after an event has taken place
	 * @return void
	 */
	public void update() {
		if(pause.getAction() == "Quit") {
			audioLoop = false;
			 sfx.get("background").stop();
		}
		if(pausedStatus == null && pause.getAction() == "Resume") {
			
			paused = false;
		}
		
		if(shootBullet) {
			bullet = new Rectangle(p.getX() + 10, p.getY()+20, 8, 8);
			shoot(60);
			shootBullet = false;
			bulletSpawned = false;
			
		}
		
		if(levelX < 0  && p.getX() != 0) {
			if(p.getX() < (350)){
				if(levelX != 0) {
					levelX += 350;
					p.setX(p.getX() + 350);
				}
			}
		}
		if(levelX != -3850) {
			if(p.getX() > (getWidth() - 350)) {
			    levelX -= 350;
			    p.setX(p.getX() - 350);
				    
			}
		}
		if(playerHitBox.y > 1000 || s.getHealth() <= 0) {
			DScreen ds = new DScreen();
			score = ((monstersKilled * 200) - (milliseconds) / 50);
			sfx.get("death").play();
			audioLoop = false;
			sfx.get("background").stop();
			String scoreString = new String();
			scoreString += score;
			writeTo("score.txt", scoreString);
			ds.start();
			this.stop();
		}
		if(p.getX() >= getWidth() && levelX == -3850) {
			WScreen ws = new WScreen();
			score = ((monstersKilled * 200) - (milliseconds) / 50);
			sfx.get("background").stop();
			audioLoop = false;
			String scoreString = new String();
			scoreString += score;
			writeTo("score.txt", scoreString);
			ws.start();
			this.stop();
		}
		
		checkCollision();
		
		tick();
		
	}//update
	
	/**
	 * This method takes in a key stroke being pressed and will
	 * handle the system operation appropriate with the key stroke
	 * @param e - KeyEvent
	 * @return void
	 */
	public void keyPressed(KeyEvent e) {
		

		
		int keyCode = e.getKeyCode();

		if(keyCode == KeyEvent.VK_S) {
	
			lastDirection = "DOWN";
			//Tool used for dev
//			if(tempMouse == 1) {
//				tempmX = (int) MouseInfo.getPointerInfo().getLocation().getX();
//				tempmY = (int) MouseInfo.getPointerInfo().getLocation().getY();
//			}else if(tempMouse == 2) {
//				int mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
//				int mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
//				tempMouse = 0;
//				System.out.println("distance x: " + (mouseX - tempmX) + " distance y: " + (mouseY - tempmY));
//			}
			
		}

		if(keyCode == KeyEvent.VK_W){
    
			lastDirection = "UP";
		}

		if(keyCode == KeyEvent.VK_A){
	
	
			moving = true;
			lastDirection = "LEFT";
			
			if(p.getX() != 0) {
				p.setVelX(runningSpeed * -1);
			}
			
				
		}
			
			
		if(keyCode == KeyEvent.VK_D){
			moving = true;

			lastDirection = "RIGHT";
			
			if(p.getX() != 5130 - playerHitBox.width) {
				p.setVelX(runningSpeed);
			}
						
		}
			
			if(keyCode == KeyEvent.VK_SPACE ){
				if(canJump) {
					jump();
					
				}else {
					return;
				}
			}
			if(keyCode == KeyEvent.VK_SHIFT) {
				runningSpeed = 3;
			}
			
			if(keyCode == KeyEvent.VK_I ){
				
				sfx.get("shoot").play();
				shootBullet = true;
				
			}
		
		if(keyCode == KeyEvent.VK_ESCAPE){
			pauseButton();
			
		    pause.start();		    
		}
		
		if(keyCode == KeyEvent.VK_M){
			taunt = true;		    
		}
		

		
	}//keyPressed
	
	/**
	 * This method takes in a key stroke being released and will
	 * handle the system operation appropriate with the key stroke
	 * @param e - KeyEvent
	 * @return void
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
		if(keyCode == KeyEvent.VK_S) {
		
			//tempMouse++;
		}
		
		if(keyCode == KeyEvent.VK_W){
		    
		}
		
		if(keyCode == KeyEvent.VK_A){
			
			moving = false;
		    p.setVelX(0);
		}
		
		if(keyCode == KeyEvent.VK_D){
			
			moving = false;
		    p.setVelX(0);
		}
		if(keyCode == KeyEvent.VK_SHIFT) {
			runningSpeed = 1;
		}
		
		if(keyCode == KeyEvent.VK_M) {
			taunt = false;
		}
	}//keyReleased
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}//keyTyped
	
	/**
	 * This method will set parameters to make the fall method make the player move upwards
	 * and then move down after the jump has reached a limit
	 * @return void
	 */
	public void jump() {
		canJump = false;
		fallSpeed = jumpPower;
		fall();
	}//jump

	/**
	 * This method uses the fall speed to make the players Y-Value to decrease if the
	 * the player is not on the ground
	 * @return void
	 */
	public void fall() {
		
		if(falling) {
			fallSpeed += gravity;	
			
			if(floorOne.intersects(playerHitBox) == false) {
				p.setY(p.getY() + fallSpeed);
			}else {
				p.setY(floorOne.y - 53);
			}
		}
		
	}//fall
	
	/**
	 * This method will create a rectangle object associated with the bullets
	 * hitbox and will use its power to determine how far it should move
	 * @param power - int used to determine how far the bullet should be shot
	 * @return void
	 */
	public void shoot(int power) {
		bulletSpawned = true;
		int xLoc = bullet.x;
		int yLoc = bullet.y;
		bulletHit();
		if(bulletSpawned == false) {
			return;
		}
		if(power > 0) {
			
			countTo(2);
			
			if(lastDirection.equals("RIGHT")) {
				xLoc += 5;
			}else if(lastDirection.equals("LEFT")) {
				xLoc -= 5;
			}else if(lastDirection.equals("UP")) {
				yLoc -= 5;
			}else {
				yLoc += 5;
			}
			bullet.setLocation(xLoc, yLoc);
			
			render();
			shoot(power - 1);
			
		}else {	
			return;	
		}
	}//shoot
	
	/**
	 * This method will animate the player by changing the picture rendered
	 * based on the direction the player is facing
	 * @return void
	 */
	public void animateP() {
	
	if(lastDirection.equals("RIGHT")) {
			playerAniRight();
			
	}else if(lastDirection.equals("LEFT")){
		playerAniLeft();
	}
		
	}
	public void bulletHit() {
		if(bulletSpawned) {
			if(bullet.intersects(flowerOne)) {
				flowOne.setHealth(flowOne.getHealth() - s.getDamage());
				bulletSpawned = false;
				if(flowOne.getHealth() < 1) {
					monstersKilled ++;
				}
			}
			
			if(bullet.intersects(flowerTwo)) {
				flowTwo.setHealth(flowTwo.getHealth() - s.getDamage());
				bulletSpawned = false;
				if(flowTwo.getHealth() < 1) {
					monstersKilled ++;
				}
			}
			
			if(bullet.intersects(flowerThree)) {
				flowThree.setHealth(flowThree.getHealth() - s.getDamage());
				bulletSpawned = false;
				if(flowThree.getHealth() < 1) {
					monstersKilled ++;
				}
			}
		}
	}//animateP
	
	/**
	 * This method checks for all game collisions and performs actions based on those collisions
	 * such as a bullet collision with an enemy
	 * @return void
	 */
	public void checkCollision() {
		if(milliseconds == aniPlant) {
			
			aniPlant++;
		}
		if(flowOne.getHealth() > 0) {
		if(flowerOne.intersects(playerHitBox)){
			s.setHealth(s.getHealth() - flowOne.getDamage());
			if(lastDirection == "RIGHT") {
				p.setY(p.getY() - 20);
				p.setX(p.getX()- 30);
			}else {
				p.setY(p.getY() - 20);
				p.setX(p.getX() + 30);
			}
				plantAttack = true;
				sfx.get("damage").play();
				animateFlower();
			}else {
				plantAttack = false;
			}
		}
		if(flowTwo.getHealth() > 0) {
		if(flowerTwo.intersects(playerHitBox))  {
			s.setHealth(s.getHealth() - flowTwo.getDamage());
			if(lastDirection == "RIGHT") {
				p.setY(p.getY() - 20);
				p.setX(p.getX()- 30);
			}else {
				p.setY(p.getY() - 20);
				p.setX(p.getX() + 30);
			}
			plantAttack = true;
			sfx.get("damage").play();
			animateFlower();
			}else {
				plantAttack = false;
			}
		}
		if(flowThree.getHealth() > 0) {
		if(flowerThree.intersects(playerHitBox)) {
			s.setHealth(s.getHealth() - flowThree.getDamage());
			if(lastDirection == "RIGHT") {
				p.setY(p.getY() - 20);
				p.setX(p.getX()- 30);
			}else {
				p.setY(p.getY() - 20);
				p.setX(p.getX() + 30);
			}
			plantAttack = true;
			sfx.get("damage").play();
			animateFlower();
		}else {
			plantAttack = false;
		}
		}
		
		
		if(obsticleOne.intersects(playerHitBox)) {
			p.setVelX(0);
			sfx.get("damage").play();
			s.setHealth(s.getHealth() - 0.01);
		}
		if(obsticleOne.intersects(playerHitBox) && fallSpeed > 0 && playerHitBox.y < 548) {
			canJump = true;
			sfx.get("damage").play();
			fallSpeed = 0;
			p.setY(obsticleOne.y - 53);
		}
		
		if(floorTwo.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 541) {
			canJump = true;
			fallSpeed = 0;
			p.setY(floorTwo.y - 53);
			
		}else if(floorSeven.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 535) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorSeven.y - 53);
			
			
		}else if(floorSix.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 535) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorSix.y - 53);
			
			
		}else if(floorFive.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 599) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorFive.y - 53);
			
			
		}else if(floorFour.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 647) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorFour.y - 53);
			
			
		}else if(floorThree.intersects(playerHitBox) == true && fallSpeed > 0 && p.getY() < 685) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorThree.y - 53);
			
			
		}else if(floorOne.intersects(playerHitBox) == true && fallSpeed > 0) {
			//System.out.println(fallSpeed);
				
			canJump = true;
			fallSpeed = 0;
			falling = false;
			p.setY(floorOne.y - 53);
			
			
		}else if(paused) {
			return;
		}else {
			falling = true;
			fall();
		}
		
		
	}//checkCollision
	
	/**
	 * This method initialized enemy information such as; health, location,
	 * damage
	 * @return void
	 */
	public void initMonster() {
		flowOne = new Monster(plantIdleOne, 0.5, 0.06, 500,500);
		flowerOne = new Rectangle(500,500,53,52);
		flowTwo = new Monster(plantIdleOne, 0.5, 0.06, 500,500);
		flowerTwo = new Rectangle(500,500,53,52);
		flowThree = new Monster(plantIdleOne, 0.5, 0.06, 500,500);
		flowerThree = new Rectangle(500,500,53,52);
		
	}//initMonser
	
	/**
	 * This method will count to a specified number in milliseconds
	 * @param millisecs - int that indicates how long the method should count to, in milliseconds 
	 * @return void
	 */
	public void countTo(int millisecs){
		long timeM = System.currentTimeMillis();
		int milli = 0;
		while(milli < millisecs) {
			if(System.currentTimeMillis() - timeM > 100) {
			
				timeM += 100;
				milli++;
			}
			return;
		}
	}//countTo
	
	/**
	 * This method initializes all images that should be rendered onto the screen
	 * @return void
	 */
	public void renderImages() {
		try {
			background = ImageIO.read(getClass().getResourceAsStream("/terrain/backGround_one.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningROne.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}

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
		
		try {
			plantIdleOne = ImageIO.read(getClass().getResourceAsStream("/sprites/Monsters/plant_idleOne.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		try {
			plantIdleTwo = ImageIO.read(getClass().getResourceAsStream("/sprites/Monsters/plant_idleTwo.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
		try {
			plantAttackTwo = ImageIO.read(getClass().getResourceAsStream("/sprites/Monsters/plant_attackTwo.png"));
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
	}//renderImages
	
	/**
	 * This method will set the player animation to be set east if the player is looking right
	 * @return void
	 */
	public void playerAniRight() {
		if(aniState == 1) {
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningRTwo.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
			
		}else if(aniState == 2){
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningRThree.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		}else if(aniState == 3) {
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningRTwo.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		}else if(aniState == 4) {
			
			aniState = 1;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningROne.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		}
	}//playerAniRight
	
	/**
	 * This method will set the player animation to be set west if the player is looking left
	 * @return void
	 */
	public void playerAniLeft() {
		if(aniState == 1) {
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningLTwo.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
			
		}else if(aniState == 2){
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningLThree.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		}else if(aniState == 3) {
			
			aniState ++;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningLTwo.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		}else if(aniState == 4) {
			
			aniState = 1;
			
			
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/sprites/player_runningLOne.png"));
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
			return;
		
		}

	}//playerAniLeft
	
	/**
	 * This method will animate the attack animation if the flower is attacking
	 * @return void
	 */
	public void animateFlower() {
		if(plantAttack) {
			if(plantState == 1) {
				
				plantState ++;
			
			
				flowOne.setImage(plantAttackTwo);

				flowTwo.setImage(plantAttackTwo);

				flowThree.setImage(plantAttackTwo);
			
				return;
			
			}else if(plantState == 2){
			
				plantState = 1;
			
			

				flowOne.setImage(plantAttackTwo);
			
				flowTwo.setImage(plantAttackTwo);

				flowThree.setImage(plantAttackTwo);
			
				return;
			}
		}else {

			if(plantState == 1) {
				
				plantState ++;
			
			
				flowOne.setImage(plantIdleOne);

				flowTwo.setImage(plantIdleOne);

				flowThree.setImage(plantIdleOne);
			
				return;
			
			}else if(plantState == 2){
			
				plantState = 1;
			
			

				flowOne.setImage(plantIdleTwo);
			
				flowTwo.setImage(plantIdleTwo);

				flowThree.setImage(plantIdleTwo);
			
				return;
			}

			
		}
	}//animateFlower
	public void pauseButton() {
		paused = true;
		writeTo("paused.txt", "paused");
		
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
			
			pausedStatus = br.readLine();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void bg(Image im, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}//LevelOne

