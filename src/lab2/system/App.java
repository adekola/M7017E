/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.system;

/** This class will be responsible for coordinating the operations of the app by using corresponding 
 * pipeline elements
 *
 * @author adekola
 */
public class App {
    
    /** Checks that host is available before adding to list or subsequently when making a call
     * 
     * @param address - the address of the host to connect to
     * @return 
     */
    public boolean isHostAvailable(String address){
        return false;
    }
    
    /** Initiates a unicast conference with a specific host
     * 
     * @param address -  address of host to conference with
     */
    public void startUnicastConference(String address){
        
    }
    
    /** Initiates a multicast conference with the hosts in the participant list
     * 
     * @param address 
     */
    public void startMultiCastConference(String[] address){
        
    }
    
    /** Terminates any currently active conference session
     * 
     */
    public void endConference(){
        
    }
    
    public void muteAudio(){
        
    }
    
}
