/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.util.prefs.*;

/**
 *
 * @author jonathan
 */
public class Settings {
    //We're using the Java Preferences API to persist application settings as selected by the User
    
    private Preferences preferences;
    
    //constructor for the Settings class which simply indicates to the Preferences API to use a node for this current class
    public Settings(){
        preferences = Preferences.userNodeForPackage(this.getClass());
    }
    
    /**gets the quality from the preferences store - between 0.1 and 1 with 1 being the highest quality
     * 
     * @return the quality from the preferences store or the default one - 0.6f
     */
    public float getQuality(){
        return preferences.getFloat("quality", 0.6f);
    }
    
    /**
     * 
     * @param quality the quality value to be set
     */
    public void setQuality(float quality){
        preferences.putFloat("quality", quality);
    }
    
    /** Gets the preferred encoding to use for the audio clip (between mp3 and ogg)
     * 
     * @return The pre-set encoding value, defaults to ogg
     */
    public String getPreferredEncoding(){
        return preferences.get("encoding", "Ogg");
    }
    
    /**
     * 
     * @param encoding -  the encoding selected by the user, passed a string value
     */
    public void setPreferredEncoding(String encoding){
        preferences.put("encoding", encoding);
    }
    
    /**
     *  Clears the Preferences store of all entries
     */
    public void clear(){
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            System.err.println("Encountered an error while trying to clear preferences");
        }
    }
    
    /**
     * Persists the values that are stored in the preferences
     */
    public void flush(){
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            System.err.println("Encountered an error while trying to flush the preferences to store");
        }
    }
}
