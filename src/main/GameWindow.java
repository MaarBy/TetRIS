package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JFrame;

public class GameWindow {
	
	static final int WIDTH=470;
	static final int HEIGHT=639;
	
	private JFrame window;
	private GamePanel gamePanel;
	private StartPanel startPanel;
		
	public GameWindow() 
	{
		window=new JFrame("Tetris");
		Image icon = Toolkit.getDefaultToolkit().getImage("C:/temper/icon2.png");
        window.setIconImage(icon);
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		gamePanel=new GamePanel();
		startPanel=new StartPanel(this);
		window.add(startPanel);
		window.addKeyListener(gamePanel);
		window.addMouseMotionListener(startPanel);
		window.addMouseListener(startPanel);
		window.setVisible(true);	
	}
	
	public void startTetris() {
		window.remove(startPanel);
		window.addMouseMotionListener(gamePanel);
		window.addMouseListener(gamePanel);
		window.add(gamePanel);
		gamePanel.startGame();
		window.revalidate();
	}	
	
	
	
	public static void main(String[] args)  {
		new GameWindow();		
	}		
}



