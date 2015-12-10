/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Timer;

/**
 *
 * @author Kola
 */
public class Calling extends javax.swing.JPanel {

    /**
     * Creates new form Calling   */
    private final SimpleDateFormat date = new SimpleDateFormat("HH.mm.ss");
    private long startTime;
    private final ClockListener clock = new ClockListener();
    public final Timer timer = new Timer(53, (ActionListener) clock);
    public Calling (){      
        initComponents();
        startTime = System.currentTimeMillis();
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblDuration = new javax.swing.JLabel();
        cmdMute2 = new javax.swing.JToggleButton();

        lblStatus.setText("Calling ...");

        jLabel1.setText("Duration:");

        lblUser.setText("User");

        lblDuration.setText("time");

        cmdMute2.setText("Mute");
        cmdMute2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdMute2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cmdMute2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lblDuration))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUser))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDuration)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmdMute2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
   private void myInitComponents(){
        cmdMute2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cmdMute2.setBackground(Color.BLACK);
        cmdMute2.setContentAreaFilled(false);
        cmdMute2.setFocusPainted(false);
   }
    private void cmdMute2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdMute2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdMute2ActionPerformed

 private class ClockListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateClock();
        }
    }

    private void updateClock() {
        Date elapsed = new Date(System.currentTimeMillis() - startTime - 3600000);
        lblDuration.setText(date.format(elapsed));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JToggleButton cmdMute2;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel lblDuration;
    public static javax.swing.JLabel lblStatus;
    public static javax.swing.JLabel lblUser;
    // End of variables declaration//GEN-END:variables
}