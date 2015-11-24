/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.gstreamer.Bus;
import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.elements.PlayBin2;

/**
 *
 * @author adekola This class is responsible for coordinating the
 * functionalities in Recorder and Playbin2 to deliver a smooth recording and
 * playback experience
 *
 * The flow goes something like this: - User starts recording, he sees the
 * progress of the recording as it happens, when he's done, stops recording and
 * then playback is automatically initiated, after which he can decide to save 
 * it to file,with an option to specify the file name
 */
public class CoreApp {

    //temporary file name to hold the clip before it's finally committed to disk
    private static final String TEMP_FILE_NAME = "temp";
    
    //variables specific to g-streamer  
    private final AudioRecorder recorder;
    private final PlayBin2 player;
    
    
    //holds the name with which to store the file as it's being recorded or saved to disk after confirmation by the user
    String recordFileName;
    
    //boolean fields for tracking the state of the app, playing or recording
    private boolean isPlaying;
    private boolean isRecording;
    private boolean autoPlayBack = false;
    
    //holds a reference to an instance of settings with which user preferences will be persisted
    private final Settings settings;
    
    //a date formatter to use as part of the default filename 
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    //holds the folder path on disk where recordings will be saved
    String recordingsFolderPath;
    
    public boolean isAutoPlayBack() {
        return autoPlayBack;
    }

   
    //getters and setters...nothing special
    public AudioRecorder getRecorder() {
        return recorder;
    }

    public PlayBin2 getPlayer() {
        return player;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isRecording() {
        return isRecording;
    }

    

    public String getRecordingsFolderPath() {
        return recordingsFolderPath;
    }

    public CoreApp() {
        recorder = new AudioRecorder();
        player = new PlayBin2("player");
        recordingsFolderPath = createRecordsFolder();
        isPlaying = isRecording = false;
        settings = new Settings();

        //arange to receive notifications from the message bus
        player.getBus().connect(new Bus.ERROR() {
            @Override
            public void errorMessage(GstObject source, int code, String message) {
                logMessagesToConsole(code, message);
            }
        });
        player.getBus().connect(new Bus.WARNING() {
            @Override
            public void warningMessage(GstObject source, int code,
                    String message) {
                logMessagesToConsole(code, message);
            }
        });
        recorder.getBus().connect(new Bus.ERROR() {
            @Override
            public void errorMessage(GstObject go, int i, String string) {
                logMessagesToConsole(i, string);
            }
        });
        recorder.getBus().connect(new Bus.WARNING() {

            @Override
            public void warningMessage(GstObject go, int i, String string) {
                logMessagesToConsole(i, string);
            }
        });
    }

    public Settings getSettings() {
        return settings;
    }

    /** Simply prints messages to the standard console
     * 
     * @param code - error code number from the gstreamer system
     * @param msg - error message from the gstreamer system
     */
    void logMessagesToConsole(int code, String msg) {
        System.out.println(String.format("Received an error code: %s with message %s", code, msg));
    }

    /**
     * Starts off the recording process
     * 
     * Sets the file location and quality of the recorder pipeline to use
     */
    public void startRecording() {
        if (isRecording) {
            return;
        }

        recordFileName = createAbsoluteFileName(TEMP_FILE_NAME);
        recorder.setFileLocation(recordFileName);
        recorder.setQuality(settings.getQuality());
        recorder.play();
        isRecording = true;
    }

    /**
     * Stops the recording process and initiates the auto-playback
     */
    public void stopRecording() {
        if (!isRecording) {
            return;
        }

        recorder.stop();
        //now do a auto playback so user can hear what was recorded
        //before confirming the save to disk!
        startPlayback(TEMP_FILE_NAME);
        isRecording = false;
        autoPlayBack = true;
    }

    //expects a string with just filename, it then sets up the full path and extensions appropriately
    public void startPlayback(String file) {
        //checks if this playback is triggered from the recording process or initiated by the user selecting a clip to play
        if(autoPlayBack)
            autoPlayBack = false;
        String filename = createAbsoluteFileName(file);
        player.setInputFile(new File(filename));
        player.play();
        isPlaying = true;
    }

    /**
     * Pauses the playback
     */
    public void pausePlayback() {
        player.pause();
    }

    /**
     * Stops the playback completely
     */
    public void stopPlayback() {
        player.stop();
        isPlaying = false;
    }
    

    /**
     * Deletes the temp file which holds the clip temporarily...this is done if the user decides not to keep the recording
     * 
     */
    public void disposeLastRecording() {
        //dispose the just temp record file
        new File(createAbsoluteFileName(TEMP_FILE_NAME)).delete();
    }

    /**
     * 
     * @param filename - a filename intended to be created in the Recordings folder
     * @return a fully qualified file name (including extensions and folder path)
     */
    public String createAbsoluteFileName(String filename) {
        return recordingsFolderPath + File.separator + filename + ".ogg";
    }

    /**renames the temp record to the name entered by the user
     * 
     * @param filename - the file name supplied by the user, as a string, no file extensions. 
     * That's taken care of by createAbsoluteFileName
     * @return a true/false depending on whether it was successfully able to rename the temp file
     */
    public boolean renameLastRecord(String filename) {
        filename = createAbsoluteFileName(filename);

        new File(createAbsoluteFileName(TEMP_FILE_NAME)).renameTo(new File(filename));
         
        //check the file
        File file =  new File(filename);
        
        return file.exists();
    }

    //generates a default file name to show in the dialog for saving recorded files - without extension
    public String generateDefaultFileName() {
        return String.format("%s-Recording_%d", dateFormatter.format(new Date()), new Random().nextInt(1000));
    }

    /** Simply creates a folder in the file system into which the clips will be saved
     * 
     * @return the path of the recordings folder 
     */
    String createRecordsFolder() {
        String path = "";
        File dirpath = new File("Recordings");
        if (dirpath.mkdir() | dirpath.exists()) {
            path = dirpath.getAbsolutePath();
        }

        return path;
    }
}
