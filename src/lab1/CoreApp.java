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
 * The flow goes soemthing like this: - User starts recording, he sees the
 * progress of the recording as it happens, when he's done, stops recording and
 * then is able to playback before deciding to save it to file, for which he has
 * a quick option to rename the file
 */
public class CoreApp {

    private static final String TEMP_FILE_NAME = "temp";
    private AudioRecorder recorder;
    private PlayBin2 player;
    private int recordingsCount = 0;

    String recordFileName;
    private boolean isPlaying;
    private boolean isRecording;
    private boolean autoPlayBack = false;
    private Settings settings;
    
    public boolean isAutoPlayBack() {
        return autoPlayBack;
    }

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

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

    String recordingsFolderPath;

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

    void logMessagesToConsole(int code, String msg) {
        System.out.println(String.format("Received an error code: %s with message %s", code, msg));
    }

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

    //expects a string with just filename, it then sets up the path and extensions appropriately
    public void startPlayback(String file) {
        if(autoPlayBack)
            autoPlayBack = false;
        String filename = createAbsoluteFileName(file);
        player.setInputFile(new File(filename));
        player.play();
        isPlaying = true;
    }

    public void pausePlayback() {
        player.pause();
    }

    public void stopPlayback() {
        player.stop();
        isPlaying = false;
    }

    public void disposeLastRecording() {
        //dispose the just temp record file
        new File(createAbsoluteFileName(TEMP_FILE_NAME)).delete();
    }

    public String createAbsoluteFileName(String filename) {
        return recordingsFolderPath + File.separator + filename + ".ogg";
    }

    //renames the temp record
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

    String createRecordsFolder() {
        String path = "";
        File dirpath = new File("Recordings");
        if (dirpath.mkdir() | dirpath.exists()) {
            path = dirpath.getAbsolutePath();
        }

        return path;
    }
}
