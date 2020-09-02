package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class FilesLoader { 
	public static BufferedImage loadPicture(String picturePath) {
		try {
			return ImageIO.read(FilesLoader.class.getResource(picturePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return null;	
	}
	public static Clip loadSound(String musicFile){
		try{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(FilesLoader.class.getResource(musicFile)));
			return clip;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
