package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener,MouseListener,MouseMotionListener  {
		
	private final int gamePanelHeight=20;
	private final int gamePanelWidth=10;
	private int[][]gamePanel=new int [gamePanelHeight][gamePanelWidth];
	private Block[]shape=new Block[7];
	private BufferedImage blocks;
	private final int blockSize=30;
	private int x;
	private int y;
	private boolean leftButtonPressed=false;
	private boolean pausedGame=false;
	private boolean endedGame=false;
	private Rectangle musicLine;
	private Rectangle pauseLine;
	private Rectangle restartLine;
	private BufferedImage pause;
	private BufferedImage restart;
	private BufferedImage backgroundMusic;
	private BufferedImage backgroundImg;
	private BufferedImage pauseImage;
	private int points=0;
	private Clip track1;
	private Clip track2;
	private Clip track3;
	private Clip blockSound;
	private int trackIndex = 2;
	private Timer pressedButton=new Timer(800, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {	
			pressedButton.stop();
		}
	});
	
	private Timer loop;
	private int FPS=60;
	private int delay=1000/FPS;	
	private static Block currentBlock;
	private static Block nextBlock;		
	
	public GamePanel() 
	{
		blocks = FilesLoader.loadPicture("/klocki.png");
		backgroundImg = FilesLoader.loadPicture("/background.png");
		pause = FilesLoader.loadPicture("/pause.png");
		restart = FilesLoader.loadPicture("/restart.png");
		backgroundMusic = FilesLoader.loadPicture("/music.png");
		blockSound = FilesLoader.loadSound("/przycisk.wav");
		track1 = FilesLoader.loadSound("/music1.wav");
		track2 = FilesLoader.loadSound("/music2.wav");
		track3 = FilesLoader.loadSound("/music3.wav");
		track2.loop(Clip.LOOP_CONTINUOUSLY);
		
		shape[0]=new Block(new int[][]{
				{1,1,1,1}
		},blocks.getSubimage(0, 0, blockSize, blockSize),this,1);
		
		shape[1]=new Block(new int[][]{
			{1,1,1},
			{0,1,0},			
		},blocks.getSubimage(blockSize, 0, blockSize, blockSize),this,2);
		
		shape[2]=new Block(new int[][]{
			{1,1,1},
			{1,0,0},			
		},blocks.getSubimage(blockSize*2, 0, blockSize, blockSize),this,3);
		
		shape[3]=new Block(new int[][]{
			{1,1,1},
			{0,0,1},			
		},blocks.getSubimage(blockSize*3, 0, blockSize, blockSize),this,4);
		
		shape[4]=new Block(new int[][]{
			{0,1,1},
			{1,1,0},			
		},blocks.getSubimage(blockSize*4, 0, blockSize, blockSize),this,5);
		
		shape[5]=new Block(new int[][]{
			{1,1,0},
			{0,1,1},			
		},blocks.getSubimage(blockSize*5, 0, blockSize, blockSize),this,6);
		
		shape[6]=new Block(new int[][]{
			{1,1},
			{1,1},			
		},blocks.getSubimage(blockSize*6, 0, blockSize, blockSize),this,7);
	
	x=0;
	y=0;
	musicLine = new Rectangle(334, 400, 91, 30);
	restartLine = new Rectangle(334, 450, 91, 30);
	pauseLine = new Rectangle(334, 500, 91, 30);
	loop = new Timer(delay, new GameLooper());
	
	}	
	private void update()
	{		
		if(musicLine.contains(x-9, y-30) && leftButtonPressed  && !pressedButton.isRunning() && !endedGame)		// dolozyc obsluge przycisku MUSIC
		{
			pressedButton.start();

			if(trackIndex == 0)
			{
				track1.start();
				track1.loop(Clip.LOOP_CONTINUOUSLY);
				trackIndex++;
			}

			else if(trackIndex == 1)
			{
				track1.stop();
				track2.start();
				track2.loop(Clip.LOOP_CONTINUOUSLY);
				trackIndex++;
			}

			else if(trackIndex == 2)
			{
				track2.stop();
				track3.start();
				track3.loop(Clip.LOOP_CONTINUOUSLY);
				trackIndex++;
			}	

			else if(trackIndex == 3)
			{
				track3.stop();																						
				trackIndex = 0;
			}		
		}
		
		if(restartLine.contains(x-9, y-33)&& !pressedButton.isRunning()&&leftButtonPressed)
		 {
			pressedButton.start();
			startGame();
		 }
		
		if(pauseLine.contains(x-9, y-30)&& leftButtonPressed&&!pressedButton.isRunning()
				&&!endedGame)
		{
			pressedButton.start();
			pausedGame=!pausedGame;
		}		
		
		if(pausedGame||endedGame)
		{
			return;	
		}
			
		currentBlock.update();
	}
		
	public void paintComponent(Graphics g){
		super.paintComponent(g);		
		g.drawImage(backgroundImg, 0, 0, null);			
		for(int row = 0; row < gamePanel.length; row++)
		{
			for(int col = 0; col < gamePanel[row].length; col ++)
			{				
				if(gamePanel[row][col] != 0)
				{			
					g.drawImage(blocks.getSubimage((gamePanel[row][col] - 1)*blockSize,
						0, blockSize, blockSize), col*blockSize, row*blockSize, null);
				}									
			}
		}
		for(int row = 0; row < nextBlock.getCoords().length; row ++)
		{
			for(int col = 0; col < nextBlock.getCoords()[0].length; col ++)
			{
				if(nextBlock.getCoords()[row][col] != 0)
				{
					g.drawImage(nextBlock.getKostka(), col*30 + 320, row*30 + 50, null);	
				}
			}		
		}
		currentBlock.render(g);
				
		if(musicLine.contains(x-8, y-30))
			g.drawImage(backgroundMusic.getScaledInstance(backgroundMusic.getWidth() + 1, backgroundMusic.getHeight() + 5, BufferedImage.SCALE_DEFAULT)
					, musicLine.x - 1, musicLine.y - 1, null);
		else
			g.drawImage(backgroundMusic, musicLine.x, musicLine.y, null);
		
		if(restartLine.contains(x-8, y-33))
			g.drawImage(restart.getScaledInstance(restart.getWidth() + 1, restart.getHeight() + 5,
					BufferedImage.SCALE_DEFAULT), restartLine.x - 1, restartLine.y - 1, null);
		else
			g.drawImage(restart, restartLine.x, restartLine.y, null);
		
		if(pauseLine.contains(x-8, y-30))
			g.drawImage(pause.getScaledInstance(pause.getWidth() + 1, pause.getHeight() + 5, BufferedImage.SCALE_DEFAULT)
					, pauseLine.x - 1, pauseLine.y - 1, null);
		else
			g.drawImage(pause, pauseLine.x, pauseLine.y, null);	
		
		if(pausedGame)
		{
			String gamePausedString = "PAUZA";
			g.setColor(Color.WHITE);
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.drawString(gamePausedString, 35, GameWindow.HEIGHT/2);	
			pauseImage = FilesLoader.loadPicture("/backgroundP.png");
			g.drawImage(pauseImage, 0, 0, null);			
		}
		if(endedGame)
		{
			String gameOverString = "KONIEC";
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 34));
			g.drawString(gameOverString, 50, GameWindow.HEIGHT/2);
		}	
		g.setColor(Color.WHITE);		
		g.setFont(new Font("Georgia", Font.BOLD, 20));		
		g.drawString("Punkty", GameWindow.WIDTH - 125, GameWindow.HEIGHT/2);
		g.drawString(points+"", GameWindow.WIDTH - 125, GameWindow.HEIGHT/2 + 30);				// todo rysuje kratke odtad
		
		Graphics2D g2d = (Graphics2D)g;		
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(new Color(0, 0, 0, 100));  //todo 0 0 0 100
		
		for(int i = 0; i <= gamePanelHeight; i++)
		{
			g2d.drawLine(0, i*blockSize, gamePanelWidth*blockSize, i*blockSize);
		}
		for(int j = 0; j <= gamePanelWidth; j++)
		{
			g2d.drawLine(j*blockSize, 0, j*blockSize, gamePanelHeight*30);				// todo rysuje kratke dotad
		}
	}
	public void setNextBlock() 
	{
		int index=(int)(Math.random()*shape.length);
		nextBlock=new Block(shape[index].getCoords(), shape[index].getKostka(),
				this, shape[index].getColour());		
	}
	
	public void setCurrentBlock()
	{
		currentBlock=nextBlock;
		setNextBlock();
		for(int row = 0; row < currentBlock.getCoords().length; row ++)
		{
			for(int col = 0; col < currentBlock.getCoords()[0].length; col ++)
			{
				if(currentBlock.getCoords()[row][col] != 0)
				{
					if(gamePanel[currentBlock.getY() + row][currentBlock.getX() + col] != 0)
						endedGame = true;
				}
			}		
		}
	}
	
	public int [][] getGamePanel(){
		return gamePanel;
	}
	
	public void stopGame(){
		points = 0;
		Block.normal = 250;
		
		for(int row = 0; row < gamePanel.length; row++)
		{
			for(int col = 0; col < gamePanel[row].length; col ++)
			{
				gamePanel[row][col] = 0;
			}
		}
		loop.stop();
	}
	
	public void addScore() {
		points++;
		Block.normal = 250 - points;			//TODO przyspieszanie gry, 300 wystarczy
	}
	
	public void startGame()
	{
		stopGame();
		setNextBlock();
		setCurrentBlock();
		endedGame=false;
		loop.start();
	}
	
	class GameLooper implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			update();
			repaint();			
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();	
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftButtonPressed = true;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftButtonPressed = false;	
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_P || e.getKeyChar() == 112 )
			pausedGame = !pausedGame;

		if(e.getKeyCode() == KeyEvent.VK_UP) {
			stuk();
			currentBlock.rotateShape();}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			stuk();
			currentBlock.setPatternX(1);}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			stuk();
			currentBlock.setPatternX(-1);}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			stuk();
			currentBlock.speedUp();}
	}

	private void stuk() 
	{
		blockSound.loop(1);
	}
			
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			currentBlock.speedDown();		
	}
}
