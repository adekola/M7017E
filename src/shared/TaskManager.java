/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import lab1.*;
import lab1.AudioRecorder;

/**
 *
 * @author adekola
 */
public class TaskManager {

    AudioRecorder recorder;

    public TaskManager() {
        recorder = new AudioRecorder();
    }

    public void Record() {
        recorder.Record();
    }

    public void StopRecord() {
        recorder.Stop();
    }

}
