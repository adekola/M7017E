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
import org.gstreamer.PadDirection;

/**
 * @author Kola
 * @reviewer 
 */
public class DecodingBin extends Bin {
    
    //elements declaration
    private final Element decoder;
    private final Element convert;
    private final Element rtpPcMuDepay;

    //variables related to pads
    private final Pad source;
    private final Pad sink;

    
    public DecodingBin(boolean cleanlyRelease) {
        // call parent constructor of Pipeline.
        super();
        
        //constructing pipeline elements
        rtpPcMuDepay = ElementFactory.make("rtppcmudepay", null);
        decoder = ElementFactory.make("mulawdec", null);
        convert = ElementFactory.make("audioconvert", null);
        
        
        this.addMany(rtpPcMuDepay, decoder, convert);
       
        
        Bin.linkMany(rtpPcMuDepay, decoder, convert);
        

        //Ghost pads which link the bin pads to to the elements' pads
        sink = new GhostPad("sink", rtpPcMuDepay.getStaticPad("sink"));
        source = new GhostPad("src", convert.getStaticPad("src"));
        
        //add the ghost pads to the bin
        this.addPad(sink);
        this.addPad(source);
        
        //Unlink listener flag in a multicast scenario.
        if(cleanlyRelease) {
            sink.connect(new GhostPad.UNLINKED() {
                @Override
                public void unlinked(Pad complainer, Pad gonePad) {
                    if (gonePad.getDirection().equals(PadDirection.SRC)) {
                        if(DecodingBin.this != null)
                            DecodingBin.this.dropIt();
                    }
                }
            });
        }
    }

    public void dropIt() {
        Pad sourcePad = source.getPeer();
        sourcePad.getParentElement().releaseRequestPad(sourcePad);
        ((Bin) this.getParent()).remove(this);
    }

}
