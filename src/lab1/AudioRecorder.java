/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.io.File;
import java.net.URI;
import java.util.Random;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
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
public class AudioRecorder extends Pipeline {

    Random random ;
    String folderPath = makeRecordsFolder();
    Element audiosrc = ElementFactory.make("autoaudiosrc", "mic");
    Element audioconvert = ElementFactory.make("audioconvert", "converter");
    Element encoder = ElementFactory.make("vorbisenc", "encoder");
    Element mux = ElementFactory.make("oggmux", "mux");
    Element filesink = ElementFactory.make("filesink", "filesink");


    public AudioRecorder() {
        super("AudioPipeline");
        random = new Random();
        //initialize pipeline...
        //Gst.init("AudioPipeline", null);
        //set source, sink, filter etc.
        
        addMany(audiosrc, audioconvert, encoder, mux, filesink);
    }

    public boolean Record() {
        String fName = generateFileName();
        filesink.set("location", fName);
        linkMany(audiosrc, audioconvert, encoder, mux, filesink);
        setState(State.PLAYING);
        return false;
    }

    public boolean Stop() {
        setState(State.NULL);
        //filesink.dispose();
        return false;
    }

    String generateFileName() {
        int num = random.nextInt(10000);
        return String.format("%s/Recording-%d.ogg",folderPath, num);
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
