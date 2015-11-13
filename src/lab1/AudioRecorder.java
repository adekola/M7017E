/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.io.File;
import java.net.URI;
import java.util.EventObject;
import java.util.Random;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Event;
import org.gstreamer.Gst;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.event.EOSEvent;
import org.gstreamer.media.PipelineMediaPlayer;

/**
 *
 * @author adekola
 *
 * So, basically this class will be responsible for the tasks associated with
 * Recording an Audio clip and playing it back
 *
 * - setup g-streamer pipeline and call the appropriate methods as required -
 * generate unique file names and save the recorded audio to disk
 */
public class AudioRecorder{
    
    String lastRecordedFile;

    
    Random random ;
    Pipeline pipe;
    String folderPath = makeRecordsFolder();
    Element audiosrc = ElementFactory.make("autoaudiosrc", "mic");
    Element audioconvert = ElementFactory.make("audioconvert", "converter");
    Element encoder = ElementFactory.make("vorbisenc", "encoder");
    Element mux = ElementFactory.make("oggmux", "mux");
    Element filesink = ElementFactory.make("filesink", "filesink");


    public AudioRecorder() {
        pipe =  new Pipeline("AudioPipeline");
        lastRecordedFile = "";
        random = new Random();
    }

    public void Record() {
        if (pipe != null){
            String fName = generateFileName();
            filesink.set("location", fName);
            pipe.addMany(audiosrc, audioconvert, encoder, mux, filesink);
            Pipeline.linkMany(audiosrc, audioconvert, encoder, mux, filesink);
            pipe.setState(State.PAUSED);
            pipe.setState(State.PLAYING);
        }
        else{
            pipe = new Pipeline("AudioPipeline");
            Record();
        }
            
    }

    public void Stop() {
        if(pipe != null & pipe.getState() == State.PLAYING){
            pipe.setState(State.NULL);
            pipe.dispose();
        }
    }

    public boolean isPlaying(){
        return pipe.isPlaying();
    }
    public String getLastRecordedFile() {
        return lastRecordedFile;
    }

    public void setLastRecordedFile(String lastRecordedFile) {
        this.lastRecordedFile = lastRecordedFile;
    }
    
    String generateFileName() {
        int num = random.nextInt(10000);
        lastRecordedFile = String.format("%s/Recording-%d.ogg",folderPath, num);
        return lastRecordedFile;
    }

    String makeRecordsFolder() {
        String path = "";
        File dirpath = new File("Recordings");
        if (dirpath.mkdir() | dirpath.exists()) {
            path = dirpath.getAbsolutePath();
        }
       
        return path;
    }

}
