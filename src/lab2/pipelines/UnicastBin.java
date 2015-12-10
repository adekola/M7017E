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

/**
 * @author Kola
 * @reviewer 
 */
public class UnicastBin extends Bin {
    
    //declaring the elements for the pipeline.
    private final Element udpSource;
    private final Element rtpBin;

    //Variables declaration corresponding to pads of this bin.
    private Pad source;

    
    public UnicastBin(int port, final Element sink) {
        // call parent constructor of Pipeline.
        super("UnicastReceiver_" + port);

        //Manufacture of elements.
        udpSource = ElementFactory.make("udpsrc", null);
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

        rtpBin.connect(new Element.PAD_ADDED() {
            @Override
            public void padAdded(Element element, Pad pad) {
                if (pad.getName().startsWith("recv_rtp_src")) {
   
                    DecodingBin decoder = new DecodingBin(false);
                    UnicastBin.this.add(decoder);
                    decoder.syncStateWithParent();
                    pad.link(decoder.getStaticPad("sink"));
                    source = new GhostPad("src" + String.valueOf(System.nanoTime()), decoder.getStaticPad("src"));
                    source.setActive(true);
                    addPad(source);
                    Element.linkMany(UnicastBin.this, sink);
                }
            }
        });

        addMany(udpSource, rtpBin);

        Pad pad = rtpBin.getRequestPad("recv_rtp_sink_0");
        udpSource.getStaticPad("src").link(pad);
        
        //Pause the Bin.
        pause();
    }

    //tears down the bin
    public void dropIt() {
      try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TransmitPipeline.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pad downstreamPeer = null;
        if (source != null) {
            downstreamPeer = source.getPeer();
        }
        this.setState(State.NULL);
        ((Bin) this.getParent()).remove(this);
        if (downstreamPeer != null) {
            downstreamPeer.getParentElement().releaseRequestPad(downstreamPeer);
        }
    }
}
