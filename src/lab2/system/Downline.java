/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.system;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.elements.FileSink;

/**
 * Represents the pipeline for receiving audio conference streams
 *
 * @author adekola
 */
public class Downline {

    Element audiosrc = ElementFactory.make("autoaudiosrc", "mic");
    Element audioconvert = ElementFactory.make("audioconvert", "converter");
    Element encoder = ElementFactory.make("vorbisenc", "encoder");
    Element mux = ElementFactory.make("oggmux", "mux");
    FileSink filesink = (FileSink) ElementFactory.make("filesink", "filesink");
}
