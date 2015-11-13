/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.io.File;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;

/**
 *
 * @author adekola
 */
public class AudioPlayer {

    PlayBin2 player;
    public AudioPlayer() {
    }
    
    public void play(String fileName){
        player =  new PlayBin2("Player");
        player.setInputFile(new File(fileName));
        player.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        player.setState(State.PLAYING);
    }
    
    //be able to update the user interface with the progress of the playback
    
}
