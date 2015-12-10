/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.pipelines;

import java.util.Iterator;
import java.util.List;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;
import lab2.config.Constants;

/**
 * @author Kola
 * @reviewer
 */
public class TransmitPipeline extends Pipeline {

    //construct elements of the sending pipeline
    final Element source = ElementFactory.make("alsasrc", "alsasrc");
    final Element tee = ElementFactory.make("tee", "tee");

    //Variables declaration corresponding to sub-bins of this bin.
    private TransmitterBin multicastBin = null;
    private TransmitterBin unicastBin = null;

    public TransmitPipeline() {
        //obligatory call to Transmitting parent constructor.
        super("TransmittingPipeline");

        addMany(source, tee);

        Pipeline.linkMany(source, tee);
    }

    /**
     * Plugs a multi-cast bin to the sending pipeline.
     *
     * @param ip is the IP address of the server.
     * @return the SSRC (or -1 if error)
     */
    public long startStreamingToMulticast(String ip) {
        if (multicastBin == null) {

            //Manufacture of elements.
            multicastBin = new TransmitterBin(ip, Constants.MULTICAST_PORT, true, "MULTICAST_TB");

            //Adding elements together.
            add(multicastBin);

            //States Synchronization with the parent bin.
            multicastBin.syncStateWithParent();

            //Linkaging elements together.
            tee.getRequestPad("src%d").link(multicastBin.getStaticPad("sink"));

            //Play the pipeline.
            play();

            //Return the SSRC to differentiate different multicast transmission.
            return multicastBin.getSSRC();
        }
        return -1;
    }

    /**
     * Remove the multi-cast bin to the sending pipeline.
     */
    public void stopStreamingToMulticast() {
        if (multicastBin != null) {
            multicastBin.dropIt();
            multicastBin = null;
        }
    }

    /**
     * Injects a uni-cast bin into the sending pipeline.
     *
     * @param ip is the IP address of the server.
     */
    public void startStreamingToUnicast(String ip) {
        if (unicastBin == null) {

            //Manufacture of elements.
            unicastBin = new TransmitterBin(ip, Constants.UNICAST_PORT, false, "UNICAST_TB");

            //Adding elements together.
            add(unicastBin);

            //States Synchronization with the parent bin.
            unicastBin.syncStateWithParent();

            //Linkaging elements together.
            tee.getRequestPad("src%d").link(unicastBin.getStaticPad("sink"));

            //Play the pipeline
            play();
        }
    }

    /**
     * Removes the uni-cast bin to the sending pipeline.
     */
    public void stopStreamingToUnicast() {
        if (unicastBin != null) {
            unicastBin.dropIt();
            unicastBin = null;
        }
    }

    public boolean isStreamingToMulticast() {
        return (multicastBin != null);
    }

    public boolean isStreamingToUnicast() {
        return (unicastBin != null);
    }

    public void mutemicro() {
        source.set("volume", 0.0);
    }

    public void unmutemicro() {
        source.set("volume", 1.0);
    }

    /*
    public void printPipeline() {

        List<Element> elements = this.getElements();

        if (elements.size() > 0) {
            Iterator<Element> elemiter = elements.iterator();
            Element e = null;
            while (elemiter.hasNext()) {
                e = (Element) elemiter.next();

                List<Pad> pads = e.getPads();

                if (pads.size() > 0) {
                    Iterator<Pad> paditer = pads.iterator();
                    Pad pad = null;
                    while (paditer.hasNext()) {
                        pad = (Pad) paditer.next();
                        System.out.print(e + " " + pad.getDirection());
                        System.out.println("\t" + pad.getCaps());
                    }
                }
            }
        }
    } */
}
