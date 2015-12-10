/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lab2.pipelines;

import org.gstreamer.Bin;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pad;

/**
 * @author Kola
 * @reviewer 
 */
public class EncodingBin extends Bin {
    
    private final Element queue;
    private final Element mulawEncoder;
    private final Element rtpPcMuPay;
    
    //Pad related variables.
    private final Pad source;
    private final Pad sink;

    
    public EncodingBin() {
        // parent constructor
        super();
        
        //setting up elements
        rtpPcMuPay = ElementFactory.make("rtppcmupay", null);
        mulawEncoder =  ElementFactory.make("mulawenc", "mulawenc");
        queue    = ElementFactory.make("queue", null);
        
        this.addMany(queue, mulawEncoder, rtpPcMuPay);
        
        Bin.linkMany(queue, mulawEncoder, rtpPcMuPay);
        
        //Ghost pads which link the bin pads to to the elements' pads
        source = new GhostPad("src", rtpPcMuPay.getStaticPad("src"));
        sink = new GhostPad("sink", queue.getStaticPad("sink"));
        
        //Activate the ghost pads
        source.setActive(true); 
        sink.setActive(true); 
        
        //Then add to the bin
        this.addPad(sink);
        this.addPad(source);   
    }
}
