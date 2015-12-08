/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.ArrayList;
import lab2.config.Network;
import lab2.config.Network.*;
import lab2.config.Constants;

/**
 * @author Kola
 * @reviewer 
 */
public class ClientConnect {
    //-------------------------------------------------------------------------+
    //|************************    VARIABLES     ******************************|
    //+------------------------------------------------------------------------+
    private com.esotericsoftware.kryonet.Client client;
    private final String myName;
    private final String serverIP;
    EventListener listener = null;

    //+------------------------------------------------------------------------+
    //|**********************      CONSTRUCTOR     ****************************|
    //+------------------------------------------------------------------------+
    /**
     * Constructor creating a client for audioConference.
     * 
     * @param serverIP is the IP address of the server.
     * @param name is the name of the client.
     */
    public ClientConnect(String serverIP, String name) {
        this.myName = name;
        this.serverIP = serverIP;
        //initialize client class with buffer enough parameters
        client = new com.esotericsoftware.kryonet.Client(8192, 25000);
        new Thread(client).start();

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(client);

        //add listeners for connection
        client.addListener(new Listener() {

            //whene connected rigester me with name
            @Override
            public void connected(Connection connection) {
                RegisterName registerName = new RegisterName();
                registerName.name = myName;
                client.sendTCP(registerName);
            }

            //when recived object
            @Override
            public void received(Connection connection, Object object) {
                //received new client list
                if (object instanceof ArrayList) {
                    sendToListener(Constants.EVT_CLIENT_LIST_RECEIVED, object);
                    return;
                } //received new room array
                else if (object instanceof Room[]) {
     
                    sendToListener(Constants.EVT_ROOM_ARRAY_RECEIVED, object);
                   
                } //received new message from another client
                else if (object instanceof Message) {
                    Message message = ((Message) object);
                    if (message.recipient != null && message.sender != null) {
                        Client tmp = new Client(myName, null, connection.getID());
                        if (tmp.equals(message.recipient)) {
                            sendToListener(Constants.EVT_MESSAGE_FROM_ANOTHER_CLIENT, object);
                        }

                    }

                }//received new multicast address from server
                else if (object instanceof String) {
                    sendToListener(Constants.EVT_ROOM__MULTICAST_RECIEVED, object);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("disconnected");
            }
        });

    }

    //+------------------------------------------------------------------------+
    //|**********************     METHODS     *********************************|
    //+------------------------------------------------------------------------+
    /**
     * Connection to the server within a new thread
     */
    public void connect() {

        new Thread("Connect to server") {
            @Override
            public void run() {
                try {
                    client.connect(5000, serverIP, Constants.MAIN_SERVER_PORT);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();

    }

    /**
     * Set the listener of incoming informations from server
     *
     * @param lst is the event listener to be set.
     */
    public void setListener(EventListener lst) {
        this.listener = lst;
    }

    /**
     * Send to the listener.
     *
     * @param message is the code of the message sent.
     * @param o is the object sent in the message.
     */
    public void sendToListener(int message, Object o) {
        if (listener != null) {
            listener.fireEvent(message, o);
        }

    }

    /**
     * Send a request to refresh clients and rooms information.
     */
    public void sendRefreshLists() {
        if (client != null) {
            client.sendTCP(new RefreshLists());
        }
    }

    /**
     * Send request to join a specific room.
     *
     * @param room is the index of the room.
     */
    public void sendJoinRoom(int room) {
        if (client != null) {
            client.sendTCP(new JoinRoom(room));
        }
    }

    /**
     * Send request to leave the room.
     */
    public void sendLeaveRoom() {
        if (client != null) {
            client.sendTCP(new LeaveRoom());
        }
    }

    /**
     * Send message to another Client
     *
     * @param recipient is the recipient of the message.
     * @param text is the content of the message.
     */
    public void sendMessage(Client recipient, String text) {
        if (client != null) {
            System.out.println("SENT MESSAGE" + recipient + text);
            Message message = new Message(recipient, text);
            
            client.sendTCP(message);
        }
    }
    
    /**
     * Get the ID of the client.
     * @return the client ID
     */
    public int getID(){
        return client.getID();
    } 
    //***************************   end   **************************************
}
