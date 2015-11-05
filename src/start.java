
import lab1.AudioCapture;
import org.gstreamer.Gst;
import shared.TaskManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adekola
 */
public class start {
    
    public static void main(String[] args){
        Gst.init("AudioRecord", args);
        final TaskManager mgr = new TaskManager();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioCapture(mgr).setVisible(true);
            }
        });
    }
}
