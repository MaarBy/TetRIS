package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import javafx.scene.shape.Circle;



public class StartPanel extends JPanel implements MouseListener,MouseMotionListener {
	
	private int mouseXcoordinates;
	private int mouseYcoordinates;
	private boolean mouseLeftButtonPressed = false;
	private BufferedImage logo;
	private BufferedImage manual;
	private BufferedImage start;
	private GameWindow window;
	private BufferedImage[] gameStartButton = new BufferedImage[2];
	private Timer timer;
	private Circle coordinates;
	
	public StartPanel(GameWindow window) {
		try {
			logo = ImageIO.read(GamePanel.class.getResource("/tetrisGora.png"));
			manual = ImageIO.read(GamePanel.class.getResource("/sterowanie.png"));
			start = ImageIO.read(GamePanel.class.getResource("/start.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timer=new Timer(1000/60, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			repaint();				
			}
		});
		
		timer.start();
		mouseXcoordinates = 0;
		mouseYcoordinates = 0;		
		gameStartButton[0] = start.getSubimage(0, 0, 100, 80);
		gameStartButton[1] = start.getSubimage(100, 0, 100, 80);		
		coordinates = new Circle(GameWindow.WIDTH/2 + 7, GameWindow.HEIGHT/2 - 30, 41);
		this.window = window;		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);		
		if(mouseLeftButtonPressed && coordinates.contains(mouseXcoordinates, mouseYcoordinates))
			window.startTetris();			
		g.setColor(Color.BLACK);		
		g.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);		
		g.drawImage(logo, GameWindow.WIDTH/2 - logo.getWidth()/2, GameWindow.HEIGHT/2 - logo.getHeight()/2 - 200, null);
		g.drawImage(manual, GameWindow.WIDTH/2 - manual.getWidth()/2,
				GameWindow.HEIGHT/2 - manual.getHeight()/2 + 150, null);
		
		if(coordinates.contains(mouseXcoordinates, mouseYcoordinates))
			g.drawImage(gameStartButton[0], GameWindow.WIDTH/2 - 50, GameWindow.HEIGHT/2 - 100, null);
		else
			g.drawImage(gameStartButton[1], GameWindow.WIDTH/2 - 50, GameWindow.HEIGHT/2 - 100, null);		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseXcoordinates = e.getX();
		mouseYcoordinates = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseXcoordinates = e.getX();
		mouseYcoordinates = e.getY();		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			mouseLeftButtonPressed = true;	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			mouseLeftButtonPressed = false;	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
}
