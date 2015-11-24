/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.util.prefs.*;

/**
 *
 * @author adekola
 */
public class Settings {
    private Preferences preferences;
    
    public Settings(){
        preferences = Preferences.userNodeForPackage(this.getClass());
    }
    
    public float getQuality(){
        return preferences.getFloat("quality", 0.6f);
    }
    
    public void setQuality(float quality){
        preferences.putFloat("quality", quality);
    }
    
    public String getPreferredEncoding(){
        return preferences.get("encoding", "Ogg");
    }
    
    public void setPreferredEncoding(String encoding){
        preferences.put("encoding", encoding);
    }
    
    public void clear(){
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            System.err.println("Encountered an error while trying to clear preferences");
        }
    }
    
    public void flush(){
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            System.err.println("Encountered an error while trying to flush the preferences to store");
        }
    }
}
