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
import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Event;
import org.gstreamer.Gst;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.elements.AppSink;
import org.gstreamer.elements.FileSink;
import org.gstreamer.elements.Queue;
import org.gstreamer.elements.Tee;
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
public class AudioRecorder extends Pipeline{

    Element audiosrc = ElementFactory.make("autoaudiosrc", "mic");
    Element audioconvert = ElementFactory.make("audioconvert", "converter");
    Element encoder = ElementFactory.make("vorbisenc", "encoder");
    Element mux = ElementFactory.make("oggmux", "mux");
    FileSink filesink = (FileSink) ElementFactory.make("filesink", "filesink");
    
    public AudioRecorder() {
        super("AudioPipeline");
        addMany(audiosrc, audioconvert, encoder, mux, filesink, null);
        linkMany(audiosrc, audioconvert, encoder, mux,filesink, null);
    }
    
    public void setFileLocation(String filename){
        filesink.setLocation(filename);
    }
    
    public void setQuality(float quality){
        encoder.set("quality", quality);
    }
}
