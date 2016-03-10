/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javax.swing.JOptionPane;
import models.Setting;

/**
 *
 * @author jaspertomas
 */
public class ServerSettingFrame extends javax.swing.JFrame {
    static ServerSettingFrame instance;

    public static ServerSettingFrame getInstance() {
        return instance;
    }
    
    /**
     * Creates new form ServerSettingFrame
     */
    public ServerSettingFrame() {
        initComponents();
        instance=this;
        
        txtServerAddress.setText(Constants.getServerUrl());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
        txtServerAddress = new javax.swing.JTextField();
        btnSetServerAddress = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Set Server Address Please");

        label.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("Tradewind Program");

        txtServerAddress.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N

        btnSetServerAddress.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        btnSetServerAddress.setText("Save");
        btnSetServerAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetServerAddressActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSetServerAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtServerAddress)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtServerAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSetServerAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSetServerAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetServerAddressActionPerformed
        String address=txtServerAddress.getText();
        
        if(address.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please enter the server address", "Error", JOptionPane.ERROR_MESSAGE, null);
            return;
        }
                
        //fetch setting from database
        Setting serverIp=Setting.getByName("server_ip");
        if(serverIp!=null)
        {
            serverIp.setValue(address);
            serverIp.save();
        }
        //if it doesn't exist, create it
        else
        {
            serverIp=new Setting();
            serverIp.setName("server_ip");
            serverIp.setValue(address);
            serverIp.setPriority(0);
            //serverIp.setId(1);
            serverIp.save();
        }
        //open login window, close this one
        WindowManager.open(WindowManager.LOGINFRAME);
    }//GEN-LAST:event_btnSetServerAddressActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSetServerAddress;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label;
    private javax.swing.JTextField txtServerAddress;
    // End of variables declaration//GEN-END:variables
}