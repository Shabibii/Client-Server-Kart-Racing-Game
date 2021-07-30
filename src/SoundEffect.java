/**
 * This piece of code is retrieved from: https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 * The code is slightly adjusted to fit the RaceTrack project.
 * @author https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 */

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum SoundEffect
{
    START("Sounds\\trumpet.wav"),
    CRASH("Sounds\\explosion.wav"),
    GRASS("Sounds\\floop.wav"),
    WIN("Sounds\\cheering.wav");

    public static enum Volume 
    {
        MUTE, LOW, MEDIUM, HIGH
    }
 
    public static Volume volume = Volume.LOW;
    
    // Declare clip to store each sound effect 
    private Clip clip;
    
    // Constructor for obtaining corresponding audio file for enum types.
    SoundEffect(String wavFile) 
    {
        try 
        {        
        URL url = this.getClass().getClassLoader().getResource(wavFile); // use URL (instead of File)

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url); // Configure audio input stream from sound file

        clip = AudioSystem.getClip(); // get a clip resource
        clip.open(audioInputStream); // Load clip with audio input stream
        } 
        catch (UnsupportedAudioFileException  e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        catch (LineUnavailableException e) 
        {
            e.printStackTrace();
        }
    }
 
    // Play sound & restart sound
    public void play() {
        if (volume != Volume.MUTE) 
        {
            if (clip.isRunning())
                clip.stop();   // stop running clip
                clip.setFramePosition(0); // set frameposition to zero, restart audio
                clip.start();     // start playing again
        }
    }
    
    // Method to pre-load audio files
    static void init() 
    {
        values(); // gets elements from constructor
    }
} 

