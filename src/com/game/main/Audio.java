package com.game.main;

import java.io.IOException;

import javax.sound.sampled.*;


public class Audio {
	private Clip clip;
	
	public Audio(String song) {
		
		
		try {
			
			AudioInputStream aInStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(song));
			
			AudioFormat baseFormat = aInStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getFrameRate(), false);
			
			AudioInputStream dias = AudioSystem.getAudioInputStream(decodeFormat, aInStream);
			clip = AudioSystem.getClip();
			clip.open(dias);
			
		} catch (UnsupportedAudioFileException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void play() {
		if(clip == null) {
			return;
		}else {
			stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void stop() {
		if(clip.isRunning()) {
			clip.stop();
		}
	}
	
	public Clip getClip() {
		return clip;
	}
	
	public void close() {
		stop();
		clip.close();
		
	}
}
