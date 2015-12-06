/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.entities;

/** This class represents a computer node participating in the audio conference
 *
 * @author adekola
 */
public class Peer {
    String IpAddress;
    int port;
    int Id;
    static int PeerCount = 0;

    public Peer(String IpAddress, int port) {
        this.IpAddress = IpAddress;
        this.port = port;
        this.Id = PeerCount++;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getId() {
        return Id;
    }
    
    @Override
    public String toString(){
        return String.format("Peer%s:%s", Id, IpAddress);
    }
}
