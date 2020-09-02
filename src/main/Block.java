package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Block {
	
	private int [][] coordinates; 
	private int [][] reference;
	private BufferedImage square;
	private GamePanel gamePanel;
	private int colour;
	private int axisX=0;
	private int axisY =0;
	private int patternX;
	private int delay;
	private long time;
	private long lastTime;
	static int normal =250;
	private int fast=30;
	
	private boolean crash=false;
	private boolean axisXmovement=false;
	
	public Block(int[][] coordinates, BufferedImage square, GamePanel gamePanel, int colour) {
		
		this.coordinates = coordinates;
		this.square = square;
		this.gamePanel = gamePanel;
		this.colour = colour;
		patternX=0;
		axisX=4;
		axisY=0;
		delay=normal;
		time=0;
		lastTime=System.currentTimeMillis();		
		reference=new int[coordinates.length][coordinates[0].length];
		System.arraycopy(coordinates, 0, reference,0, coordinates.length);
		
	}
	public void update(){
		axisXmovement = true;
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		
		if(crash)
		{
			for(int row = 0; row < coordinates.length; row ++)
			{
				for(int col = 0; col < coordinates[0].length; col ++)
				{
					if(coordinates[row][col] != 0)
						gamePanel.getGamePanel()[axisY + row][axisX + col] = colour;
				}
			}
			checkLine();
			gamePanel.addScore();
			gamePanel.setCurrentBlock();;
		}
		
		if(!(axisX + patternX + coordinates[0].length > 10) && !(axisX + patternX < 0))
		{		
			for(int row = 0; row < coordinates.length; row++)
			{
				for(int col = 0; col < coordinates[row].length; col ++)
				{
					if(coordinates[row][col] != 0)
					{
						if(gamePanel.getGamePanel()[axisY + row][axisX + patternX + col] != 0)
						{
							axisXmovement = false;
						}						
					}
				}
			}
			
			if(axisXmovement)
				axisX += patternX;
			
		}
		
		if(!(axisY + 1 + coordinates.length > 20))
		{			
			for(int row = 0; row < coordinates.length; row++)
			{
				for(int col = 0; col < coordinates[row].length; col ++)
				{
					if(coordinates[row][col] != 0)
					{						
						if(gamePanel.getGamePanel()[axisY + 1 + row][axisX +  col] != 0)
						{
							crash = true;
						}
					}
				}
			}
			if(time > delay)
				{
					axisY++;
					time = 0;
				}
		}else{
			crash = true;
		}		
		patternX = 0;
	}
	
public void render(Graphics g){
		
		for(int row = 0; row < coordinates.length; row ++)
		{
			for(int col = 0; col < coordinates[0].length; col ++)
			{
				if(coordinates[row][col] != 0)
				{
					g.drawImage(square, col*30 + axisX*30, row*30 + axisY*30, null);	
				}
			}		
		}
		
		for(int row = 0; row < reference.length; row ++)
		{
			for(int col = 0; col < reference[0].length; col ++)
			{
				if(reference[row][col] != 0)
				{
					g.drawImage(square, col*30 + 320, row*30 + 160, null);	
				}					
			}				
		}
	}

public void rotateShape()	{	
	int[][] rotatedBlock = null;
	
	rotatedBlock = transposePattern(coordinates);	
	rotatedBlock = reverseRows(rotatedBlock);
	
	if((axisX + rotatedBlock[0].length > 10) || (axisY + rotatedBlock.length > 20))
	{
		return;
	}
	
	for(int row = 0; row < rotatedBlock.length; row++)
	{
		for(int col = 0; col < rotatedBlock[row].length; col ++)
		{
			if(rotatedBlock[row][col] != 0)
			{
				if(gamePanel.getGamePanel()[axisY + row][axisX + col] != 0)
				{
					return;
				}
			}
		}
	}
	coordinates = rotatedBlock;
}

private int[][] transposePattern(int[][] pattern){
    int[][] temp = new int[pattern[0].length][pattern.length];
    for (int i = 0; i < pattern.length; i++)
        for (int j = 0; j < pattern[0].length; j++)
            temp[j][i] = pattern[i][j];
    return temp;
}
private int[][] reverseRows(int[][] pattern){
	
	int middle = pattern.length/2;
		
	for(int i = 0; i < middle; i++)
	{
		int[] temp = pattern[i];		
		pattern[i] = pattern[pattern.length - i - 1];
		pattern[pattern.length - i - 1] = temp;
	}	
	return pattern;	
}
	
	private void checkLine(){
		int size = gamePanel.getGamePanel().length - 1;
		
		for(int i = gamePanel.getGamePanel().length - 1; i > 0; i--)
		{
			int count = 0;
			for(int j = 0; j < gamePanel.getGamePanel()[0].length; j++)
			{
				if(gamePanel.getGamePanel()[i][j] != 0)
					count++;	
				gamePanel.getGamePanel()[size][j] = gamePanel.getGamePanel()[i][j];
			}
			if(count < gamePanel.getGamePanel()[0].length)
				size --;
		}
	}
	
	public int[][] getCoords() {
		return coordinates;
	}
	public void setCoords(int[][] coords) {
		this.coordinates = coords;
	}
	public BufferedImage getKostka() {
		return square;
	}
	public void setBlock(BufferedImage block) {
		this.square = block;
	}
	
	public int getX() {
		return axisX;
	}
	public void setX(int x) {
		this.axisX = x;
	}
	public int getY() {
		return axisY;
	}
	public void setY(int y) {
		this.axisY = y;
	}
	public int getColour() {
		return colour;
	}
	public void setColour(int colour) {
		this.colour = colour;
	}
	public int getPatternX() {
		return patternX;
	}
	public void setPatternX(int patternX) {
		this.patternX = patternX;
	}
	public void speedUp(){
		delay = fast;
	}
	public void speedDown(){
		delay = normal;
	}
}
