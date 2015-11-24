/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gstreamer.Bus;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.Pipeline;
import org.gstreamer.swing.PipelinePositionModel;

/**
 *
 * @author adekola
 */
public class AudioCapture extends javax.swing.JFrame {

    CoreApp app;
    DefaultListModel recordingsListModel;
    String recordToPlay;

    PipelinePositionModel recorderPosition;
    PipelinePositionModel playerPosition;

    /**
     * Creates new form AudioCap
     */
    public AudioCapture() {
        Gst.init(); //should this really be here or could it be somewhere else instead?
        recordingsListModel = new DefaultListModel();
        app = new CoreApp();
        initComponents();
        this.setLocationRelativeTo(null);
        getExistingRecordings();
        recorderPosition = new PipelinePositionModel(app.getRecorder());
        playerPosition = new PipelinePositionModel(app.getPlayer());

        setupSliderListener();

        app.getPlayer().getBus().connect(new Bus.EOS() {
            public void endOfStream(GstObject source) {
                resetElements();
                if (app.isAutoPlayBack()) {
                    initiateSave();
                }
            }
        });

    }


    private void resetElements() {
        //reset the slider position and time label
        positionSlider.setModel(new DefaultBoundedRangeModel());
        lblTimeLapse.setText("0:00:00");
    }

    private void setupSliderListener() {
        this.positionSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateTimeLabel();
            }
        });
    }

    void updateTimeLabel() {
        Pipeline activePipeline = new Pipeline();

        if (app.isPlaying()) {
            activePipeline = app.getPlayer();
        } else if (app.isRecording()) {
            activePipeline = app.getRecorder();
        }

        long currPosition = activePipeline.queryPosition(Format.TIME);

        currPosition = currPosition / 1000000000L;
        lblTimeLapse.setText(String.format("%d:%02d:%02d", currPosition / 3600, (currPosition % 3600) / 60, (currPosition % 3600)));

    }

    void getExistingRecordings() {

        String recordsFolder = app.getRecordingsFolderPath();

        File folder = new File(recordsFolder);

        File[] recordings = folder.listFiles(new OggFilter()); //this will be picked from the preferences feature 

        for (File file : recordings) {
            String fileName = file.getName();
            if (fileName.contains("temp")) {
                continue;
            }
            recordingsListModel.addElement(fileName.substring(0, fileName.length() - 4));//knocks off the extension..or maybe it's fine to have it 
        }

        listRecords.setModel(recordingsListModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listRecords = new javax.swing.JList();
        positionSlider = new javax.swing.JSlider();
        btnRecord = new javax.swing.JToggleButton();
        btnStop = new javax.swing.JToggleButton();
        lblTimeLapse = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Audio Recorder 1.0");

        listRecords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listRecordsMouseClicked(evt);
            }
        });
        listRecords.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listRecordsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listRecords);

        positionSlider.setValue(0);
        positionSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                positionSliderStateChanged(evt);
            }
        });

        btnRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/record_icon.gif"))); // NOI18N
        btnRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordActionPerformed(evt);
            }
        });

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/stop.png"))); // NOI18N
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        lblTimeLapse.setText("HH:mm:ss");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/jokolaudio.png"))); // NOI18N

        jMenu1.setText("Preferences");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        aboutMenuItem.setText("About");
        aboutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aboutMenuItemMouseClicked(evt);
            }
        });
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenuBar1.add(aboutMenuItem);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(positionSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                                .addComponent(jLabel1))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTimeLapse)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTimeLapse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(positionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // TODO add your handling code here:
        btnStop.setSelected(true);
        btnRecord.setSelected(false);
        this.positionSlider.setModel(playerPosition);
        app.stopRecording();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordActionPerformed
        // TODO add your handling code here:
        btnRecord.setSelected(true);
        btnStop.setSelected(false);
        app.startRecording();
        this.positionSlider.setModel(recorderPosition);
    }//GEN-LAST:event_btnRecordActionPerformed

    private void initiateSave() {
        // TODO add your handling code here:
        String defaultFileName = app.generateDefaultFileName();
        String fileName = JOptionPane.showInputDialog(this, "Please enter a file name", defaultFileName);

        if (fileName == null) { //cancel button was pressed
            app.disposeLastRecording();
            resetElements();
        } else if (fileName.isEmpty()) { //user cleared the content of the filenam textbox
            fileName = defaultFileName;
            app.renameLastRecord(fileName);
            //update the list of files showing in the list
            recordingsListModel.addElement(fileName);
            resetElements();
        } else {
            app.renameLastRecord(fileName);
            //update the list of files showing in the list
            recordingsListModel.addElement(fileName);
            resetElements();
        }
    }


    private void listRecordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listRecordsMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) { // a user clicks twice = Double click
            app.stopPlayback(); //first stop the currently playing process

            recordToPlay = (String) listRecords.getSelectedValue();
            this.positionSlider.setModel(playerPosition);
            app.startPlayback(recordToPlay);
        }
    }//GEN-LAST:event_listRecordsMouseClicked

    private void listRecordsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listRecordsValueChanged
        // TODO add your handling code here:
        if (!evt.getValueIsAdjusting()) { //checks that the value changing event is the last in the chain in this case, the mouse up during a click action
            recordToPlay = (String) listRecords.getSelectedValue();
        }
    }//GEN-LAST:event_listRecordsValueChanged

    private void positionSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_positionSliderStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_positionSliderStateChanged

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // TODO add your handling code here:
        //Show the settings dialog
        SettingsDialog dialog  = new SettingsDialog(this, true, app.getSettings());
       
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenu1MouseClicked

    private void aboutMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuItemMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_aboutMenuItemMouseClicked

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(AudioCapture.this,"This AudioRecorder was gladly developed for \n your pleasurable use by  \n\n\n"+
                        "- Kola Adebayo and " +
                        "- Jonathan Pucher \n\n\n" +
                        "  For the Multimedia Course - M7017E, \n at the Lulea Uni. of Technology, Fall 2015", null,
                        JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AudioCapture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AudioCapture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AudioCapture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AudioCapture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioCapture().setVisible(true);
            }
        });
    }

    class OggFilter implements FilenameFilter {

        @Override
        public boolean accept(File file, String fileName) {
            return fileName.endsWith(".ogg");
        }
    }

    class Mp3Filter implements FilenameFilter {

        @Override
        public boolean accept(File file, String fileName) {
            return fileName.endsWith(".mp3");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenuItem;
    private javax.swing.JToggleButton btnRecord;
    private javax.swing.JToggleButton btnStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTimeLapse;
    private javax.swing.JList listRecords;
    private javax.swing.JSlider positionSlider;
    // End of variables declaration//GEN-END:variables
}
