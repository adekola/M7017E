/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.pipelines;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gstreamer.Bin;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pad;
import org.gstreamer.State;
import org.gstreamer.elements.Tee;
import org.gstreamer.elements.good.RTPBin;

/**
 * @author Kola
 * @reviewer 
 */
public class TransmitterBin extends Bin {
    
    //bin related variables.
    private final EncodingBin encoder = new EncodingBin();
    private final RTPBin rtpBin = new RTPBin("RTPBin");
    
    //UDP sink to terminate the sending pipeline
    private final Element udpSink;
    
    //Pad variable for the sink.
    private final Pad sink;

   
    public TransmitterBin(String ip, int port, boolean isMulticast, String name) {
        // call parent constructor of Pipeline.
        super(name);
        
        //synchronize with the parent bin.
        encoder.syncStateWithParent();
        
        
        Pad rtpSink = rtpBin.getRequestPad("send_rtp_sink_0");
        
        //Construct elements.
        udpSink = ElementFactory.make("udpsink", null);
        udpSink.set("host", ip);
        udpSink.set("port", port);
        if (isMulticast) {
            udpSink.set("auto-multicast", true);
        }
        
        addMany(encoder, rtpBin, udpSink);
        
        sink = new GhostPad("sink", encoder.getStaticPad("sink"));
        
        //activate the ghost pad
        sink.setActive(true);

        addPad(sink);
        
        encoder.getStaticPad("src").link(rtpSink);
        rtpBin.getStaticPad("send_rtp_src_0").link(udpSink.getStaticPad("sink"));

    }

    //get the unique SSRC id for the current node
    public long getSSRC() {
        String test = null;
        for (Element element : rtpBin.getElements()) {
            if (element.getName().startsWith("rtpsession")) {
                try {
                    test = element.getSinkPads().get(0).getCaps().toString();
                } catch (Exception e) {
                }
            }
        }
        if (test != null) {
            Pattern pattern = Pattern.compile("ssrc=(.*?)([0-9]+);");
            Matcher matcher = pattern.matcher(test);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(2));
            }
        }
        return -1;
    }

    //tear down the bin
    public void dropIt() {
        //wait for 1,5sec to stabilize states
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TransmitPipeline.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Pad sourcePad = sink.getPeer();

        Tee teeElement = ((Tee) sink.getPeer().getParent());     
        
        //gets the transmitting bin element
        Bin parentBin = ((Bin) this.getParent()); 
        
        //upstreamPeer.setActive(false);
        sourcePad.setBlocked(true);
        ((Bin) this.getParent()).unlink(this);

        this.setState(State.NULL);
        ((Bin) this.getParent()).remove(this);
        
        if (teeElement.getSrcPads().size() == 1) {
            parentBin.setState(State.NULL);
        }
 
        teeElement.releaseRequestPad(sourcePad);
    }
}
