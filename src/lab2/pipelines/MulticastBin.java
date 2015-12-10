/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.pipelines;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.gstreamer.Bin;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pad;
import org.gstreamer.State;
import org.gstreamer.elements.FakeSink;

/**
 * @author Kola
 * @reviewer 
 */
public class MulticastBin extends Bin {
    
    //Element related variables
    private final Element udpSource;
    private final Element rtpBin;
    private final Element adder;
    
    //pad related variables
    private final Pad source;

    /**
     * Constructor creating a MulticastReceivingBin
     * 
     * @param ip is the multi-cast IP address.
     * @param port is the multi-cast port number on the server.
     * @param ssrcIgnore is a variable to manage echo.
     */
    public MulticastBin(String ip, int port, final long ssrcIgnore) {
        //obligatory call to parent constructor.
        super("Room_" + ip);

        //construct elements.
        udpSource = ElementFactory.make("udpsrc", null);
        udpSource.set("multicast-group", ip);
        udpSource.set("auto-multicast", true);
        udpSource.set("port", port);

        udpSource.getStaticPad("src").setCaps(
                Caps.fromString("application/x-rtp,"
                        + "media=(string)audio,"
                        + "clock-rate=(int)8000,"
                        + "encoding-name=(string)PCMU, "
                        + "payload=(int)0, "
                        + "ssrc=(guint)1350777638, "
                        + "clock-base=(guint)2942119800, "
                        + "seqnum-base=(guint)47141"
                ));

        rtpBin = ElementFactory.make("gstrtpbin", null);
        adder = ElementFactory.make("liveadder", null);

        rtpBin.connect(new Element.PAD_ADDED() {
            @Override
            public synchronized void padAdded(Element element, Pad pad) {

                if (pad.getName().startsWith("recv_rtp_src")) {

                    if (pad.getName().contains(String.valueOf(ssrcIgnore))) {
                        //if ssrcignore flag is set, kill echo by directing it to a fake sink
                        Element fakesink = new FakeSink((String) null);
                        MulticastBin.this.add(fakesink);
                        fakesink.syncStateWithParent();
                        pad.link(fakesink.getStaticPad("sink"));
                    }
                    else {
                        //connect a decoder to the new incoming client flow.
                        DecodingBin decoder = new DecodingBin(true);
                        MulticastBin.this.add(decoder);
                        decoder.syncStateWithParent();
                        pad.link(decoder.getStaticPad("sink"));
                        Pad adderPad = adder.getRequestPad("sink%d");
                        decoder.getStaticPad("src").link(adderPad);
                    }
                }
            }
        });

        addMany(udpSource, rtpBin, adder);

        source = new GhostPad("src", adder.getStaticPad("src"));
        
        addPad(source);

        Pad pad = rtpBin.getRequestPad("recv_rtp_sink_0");
        udpSource.getStaticPad("src").link(pad);

        //Pause the bin.
        pause();
    }

    //breaks down the pipeline
    public void dropIt() {
      try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TransmitPipeline.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pad downstreamPeer = source.getPeer();
        this.setState(State.NULL);
        ((Bin) this.getParent()).remove(this);
        downstreamPeer.getParentElement().releaseRequestPad(downstreamPeer);
    }
}
