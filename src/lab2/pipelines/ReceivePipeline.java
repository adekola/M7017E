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
public class ReceivePipeline extends Pipeline {

    //constructing the pipepline elements
    private final Element adder = ElementFactory.make("liveadder", "liveadder");
    private final Element sink = ElementFactory.make("alsasink", "alsasink");

    //vars related to the uni and multi cast bins which will be injected to the receving pipeline at runtime
    private MulticastBin multicast = null;
    private UnicastBin unicast = null;

    public ReceivePipeline() {
        // obligatory call to the parent Pipeline class
        super("ReceivingPipeline");

        addMany(adder, sink);

        linkMany(adder, sink);

        play();
    }

    //initiates the flow to receive from multicast
    public void startReceivingFromMulticast(String ip, long ssrcIgnore) {
        if (multicast == null) {
            //Manufacture of elements.
            multicast = new MulticastBin(ip, Constants.MULTICAST_PORT, ssrcIgnore);

            //Adding elements together.
            add(multicast);

            //States Synchronization with the parent bin.
            multicast.syncStateWithParent();

            //Linking elements together.
            multicast.link(adder);
        }
    }

    // terminates the flow for receiving from multicast
    public void stopReceivingFromMulticast() {
        if (multicast != null) {
            multicast.dropIt();
            multicast = null;
        }
    }

    //initiates the flow to receive from unicast
    public void startReceivingFromUnicast() {
        if (unicast == null) {
            //Manufacture of elements.
            unicast = new UnicastBin(Constants.UNICAST_PORT, adder);

            //Adding elements together.
            add(unicast);

            //States Synchronization with the parent bin.
            unicast.syncStateWithParent();
        }
    }

    // terminates the flow for receiving from unicast
    public void stopReceivingFromUnicast() {
        if (unicast != null) {
            unicast.dropIt();
            unicast = null;
        }
    }

    //checks which flow the pipeline is receiving from
    public boolean isReceivingFromMulticast() {
        return (multicast != null);
    }

    /**
     * Test is you are receiving from Uni-cast
     *
     * @return true or false
     */
    public boolean isReceivingFromUnicast() {
        return (unicast != null);
    }

    /**
     * Mute the sound of the speakers.
     */
    public void muteSound() {
        sink.set("volume", 0.0);
    }

    /**
     * Un-mute the sound of the speakers.
     */
    public void unmuteSound() {
        sink.set("volume", 1.0);
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
    }
    */
   
}
