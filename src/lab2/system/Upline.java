/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.system;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pipeline;
import org.gstreamer.elements.FileSink;
import org.gstreamer.elements.good.RTPBin;

/**
 * Represents the pipeline for sending audio conference streams
 *
 * @author adekola
 */
public class Upline {

    Pipeline pipe;

    Element audiosrc = ElementFactory.make("autoaudiosrc", "mic");
    Element audioconvert = ElementFactory.make("audioconvert", "converter");
    Element encoder = ElementFactory.make("vorbisenc", "encoder");
    Element mux = ElementFactory.make("oggmux", "mux");
    RTPBin rtpbin =  new RTPBin("sending_rtp_bin");
    FileSink filesink = (FileSink) ElementFactory.make("filesink", "filesink");

    public Upline() {
        pipe = new Pipeline();
    }

}
