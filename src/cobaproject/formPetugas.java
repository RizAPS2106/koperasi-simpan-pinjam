/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static javax.swing.UIManager.getString;
/**
 *
 * @author rizky
 */
public class formPetugas extends javax.swing.JFrame {
    
    String gender,tgl,akses;
    int panjangpass;
    boolean usernameada,passwordvalid;
    
    public formPetugas() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        autokode();
        lblid.setVisible(false);
        idPetugas.setVisible(false);
        ubah.setEnabled(false);
        hapus.setEnabled(false);
        namaPetugas.requestFocus();
        load_table();
        
    }
    
    public void setId(String ida){
        id.setText(ida);
        akses();
        
        if(akses.equals("PETUGAS")){
            tampilkan();
            jScrollPane2.setVisible(false);
            simpanPetugas.setVisible(false);
            hapus.setVisible(false);
            ubah.setEnabled(true);
        }else if(akses.equals("ADMIN")){
            jScrollPane2.setVisible(true);
            simpanPetugas.setVisible(true);
            hapus.setVisible(true);
            ubah.setEnabled(false);
        }
    }
    
    //menampilkan data dari database
    public void tampilkan(){
        try{
            String sql = "SELECT * FROM petugas WHERE id = '"+id.getText()+"' ";
            java.sql.Connection con = (Connection)config.configDB();
            java.sql.Statement stm = con.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            while(res.next()){
                idPetugas.setText(res.getString("id"));
                namaPetugas.setText(res.getString("nama"));
                usernamePet.setText(res.getString("username"));
                passPet.setText(res.getString("password"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //menentukan akses
    public void akses(){
        try{
            String sql = "SELECT * FROM admin WHERE id = '"+id.getText()+"' ";
            java.sql.Connection con = (Connection)config.configDB();
            java.sql.Statement stm = con.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            if(res.next()){
                if(id.getText().equals(res.getString("id"))){
                    akses = "ADMIN";
                }
            }else{
                String sql1 = "SELECT * FROM petugas WHERE id = '"+id.getText()+"'";
                java.sql.Connection con1 = (Connection)config.configDB();
                java.sql.Statement stm1 = con1.createStatement();
                java.sql.ResultSet res1 = stm1.executeQuery(sql1);
                
                if(res1.next()){
                    if(id.getText().equals(res1.getString("id"))){
                        akses = "PETUGAS";
                    }
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void load_table(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Nama");
        model.addColumn("Username");
        model.addColumn("Password");
        
        //menampilkan data database ke dalam tabel
        try{
            int no=1;
            String sql = "select * from petugas";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
                while(res.next()){
                    model.addRow(new Object[]{res.getString(1),res.getString(2),res.getString(3),res.getString(4)});
                }
            tabelPetugas.setModel(model);
        }catch(Exception e){
            
        }
    }
    
    //validasi username sudah digunakan/belum
    public void validasiUsername(){
        try{
            String sql = "SELECT * FROM petugas WHERE username = '"+usernamePet.getText()+"' AND id != '"+idPetugas.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
                if(rs.next()){
                    if(usernamePet.getText().equals(rs.getString("username"))){
                        usernameada = true;
                    }
                }else{
                    usernameada = false;
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //validasi panjang password
    public void validasiPassword(){
        panjangpass = passPet.getText().length();
        if(panjangpass < 4 || panjangpass > 16){
            passwordvalid = false;
        }else{
            passwordvalid = true;
        }
    }
    
    public void simpanData(){
        String nama,username,pass;
        nama = namaPetugas.getText();
        username = usernamePet.getText();
        pass = passPet.getText();
        
        if(namaPetugas.equals("") || username.equals("") || pass.equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }else{
            simpan();
        }
    }
    
    public void simpan(){
        //fungsi
        try{
            String sql = "INSERT INTO petugas VALUES('"+idPetugas.getText()+"','"+namaPetugas.getText()+"','"+usernamePet.getText()+"','"+passPet.getText()+"')";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            bersihkan();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
    }
    
    public void ubahData(){
        String nama,username,pass;
        nama = namaPetugas.getText();
        username = usernamePet.getText();
        pass = passPet.getText();
        
        if(nama.equals("") || username.equals("") || pass.equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }else{
            ubah();
        }
    }
    
    private void ubah(){
        //fungsi
        try{
            String sql = "UPDATE petugas SET nama = '"+namaPetugas.getText()+"', username = '"+usernamePet.getText()+"', password = '"+passPet.getText()+"' WHERE id = '"+idPetugas.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "Data berhasil di edit");
            bersihkan();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        load_table();
    }
    
    private void hapusData(){
        // fungsi hapus data
        try {
            String sql ="delete from petugas where id='"+idPetugas.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "berhasil di hapus");
            bersihkan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
    }
    
    private void bersihkan(){
        akses();
        
        if(akses.equals("PETUGAS")){
            namaPetugas.setText("");
            usernamePet.setText("");
            passPet.setText("");
            ubah.setEnabled(true);
        }else if(akses.equals("ADMIN")){
            autokode();
            namaPetugas.setText("");
            usernamePet.setText("");
            passPet.setText("");
            simpanPetugas.setEnabled(true);
            ubah.setEnabled(false);
            hapus.setEnabled(false);
        }
    }
    
    private void autokode(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM petugas";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            
            while(rs.next()){
                if(rs.first()==false){
                    idPetugas.setText("P001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idPetugas.setText("P" + nomor);
                }
            }
        }catch(Exception e){}
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genderPetugas = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        simpanPetugas = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        usernamePet = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        passPet = new javax.swing.JPasswordField();
        back = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelPetugas = new javax.swing.JTable();
        ubah = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        lblid = new javax.swing.JLabel();
        idPetugas = new javax.swing.JTextField();
        showpass = new javax.swing.JCheckBox();
        id = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        namaPetugas = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("FORM PETUGAS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        simpanPetugas.setText("TAMBAH DATA");
        simpanPetugas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpanPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanPetugasActionPerformed(evt);
            }
        });
        simpanPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                simpanPetugasKeyPressed(evt);
            }
        });

        clear.setText("BERSIHKAN FORM");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        clear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clearKeyPressed(evt);
            }
        });

        jLabel11.setText("USERNAME       ");

        usernamePet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usernamePetKeyPressed(evt);
            }
        });

        jLabel12.setText("PASSWORD      ");

        passPet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passPetActionPerformed(evt);
            }
        });
        passPet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passPetKeyPressed(evt);
            }
        });

        back.setText("Kembali");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        tabelPetugas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tabelPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPetugasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelPetugas);

        ubah.setText("UBAH DATA");
        ubah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahActionPerformed(evt);
            }
        });
        ubah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ubahKeyPressed(evt);
            }
        });

        hapus.setText("HAPUS DATA");
        hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });
        hapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hapusKeyPressed(evt);
            }
        });

        lblid.setText("ID                       ");

        showpass.setText("Tampilkan Password");
        showpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpassActionPerformed(evt);
            }
        });
        showpass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                showpassKeyPressed(evt);
            }
        });

        id.setText("YOU ARE...");

        jLabel2.setText("NAMA");

        namaPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaPetugasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(simpanPetugas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ubah)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hapus))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblid, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(idPetugas, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(usernamePet, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(passPet, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(showpass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(namaPetugas))))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(id)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(back)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblid)
                            .addComponent(idPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(namaPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(usernamePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(passPet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showpass)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(simpanPetugas)
                            .addComponent(ubah)
                            .addComponent(hapus))
                        .addGap(18, 18, 18)
                        .addComponent(clear))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(id))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpanPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanPetugasActionPerformed
        validasiUsername();
        validasiPassword();
        
        if(usernameada == false){
            if(passwordvalid == true){
                simpan();
            }else{
                JOptionPane.showMessageDialog(null, "password minimal 4 karakter dan maksimal 16 karakter");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Username sudah digunakan");
        }
    }//GEN-LAST:event_simpanPetugasActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        
        bersihkan();
        
    }//GEN-LAST:event_clearActionPerformed

    private void usernamePetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernamePetKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            passPet.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            namaPetugas.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            passPet.requestFocus();
        }
        
    }//GEN-LAST:event_usernamePetKeyPressed

    private void passPetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passPetKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            simpanPetugas.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            usernamePet.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            showpass.requestFocus();
        }
        
    }//GEN-LAST:event_passPetKeyPressed

    private void simpanPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_simpanPetugasKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            validasiUsername();
            validasiPassword();
        
            if(usernameada == false){
                if(passwordvalid == true){
                    simpan();
                }else{
                    JOptionPane.showMessageDialog(null, "password minimal 4 karakter dan maksimal 16 karakter");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Username sudah digunakan");
            }
        }   
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            showpass.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            clear.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            ubah.requestFocus();
        }
        
    }//GEN-LAST:event_simpanPetugasKeyPressed

    private void clearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clearKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            bersihkan();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            simpanPetugas.requestFocus();
        }
        
    }//GEN-LAST:event_clearKeyPressed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(id.getText());
        bf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void tabelPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPetugasMouseClicked
        
        // menampilkan data kedalam form
        int baris = tabelPetugas.rowAtPoint(evt.getPoint());
        String id =tabelPetugas.getValueAt(baris, 0).toString();
        idPetugas.setText(id);
        
        try{
            String sql = "SELECT * FROM petugas WHERE id = '"+id+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            
            while(rs.next()){
                namaPetugas.setText(rs.getString("nama"));
                usernamePet.setText(rs.getString("username"));
                passPet.setText(rs.getString("password"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        simpanPetugas.setEnabled(false);
        ubah.setEnabled(true);
        hapus.setEnabled(true);
        
    }//GEN-LAST:event_tabelPetugasMouseClicked

    private void ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahActionPerformed
        validasiUsername();
        validasiPassword();
        
        if(usernameada == false){
            if(passwordvalid == true){
                ubahData();
            }else{
                JOptionPane.showMessageDialog(null, "password minimal 4 karakter dan maksimal 16 karakter");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Username sudah digunakan");
        }
    }//GEN-LAST:event_ubahActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        hapusData();
    }//GEN-LAST:event_hapusActionPerformed

    private void showpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpassActionPerformed
        if(showpass.isSelected()){
            passPet.setEchoChar((char)0);
        }else{
            passPet.setEchoChar('*');
        }
    }//GEN-LAST:event_showpassActionPerformed

    private void passPetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passPetActionPerformed
        
    }//GEN-LAST:event_passPetActionPerformed

    private void showpassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_showpassKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(showpass.isSelected()){
                showpass.setSelected(false);
                passPet.setEchoChar('*');
                simpanPetugas.requestFocus();
            }else{
                showpass.setSelected(true);
                passPet.setEchoChar((char)0);
                simpanPetugas.requestFocus();
            }
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            simpanPetugas.requestFocus();
        }
    }//GEN-LAST:event_showpassKeyPressed

    private void ubahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ubahKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            validasiUsername();
            validasiPassword();
        
            if(usernameada == false){
                if(passwordvalid == true){
                    ubahData();
                }else{
                    JOptionPane.showMessageDialog(null, "password minimal 4 karakter dan maksimal 16 karakter");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Username sudah digunakan");
            }
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            showpass.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            clear.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            hapus.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            simpanPetugas.requestFocus();
        }
    }//GEN-LAST:event_ubahKeyPressed

    private void hapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hapusKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            hapusData();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            showpass.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            clear.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            ubah.requestFocus();
        }
    }//GEN-LAST:event_hapusKeyPressed

    private void namaPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaPetugasKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            usernamePet.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            usernamePet.requestFocus();
        }
    }//GEN-LAST:event_namaPetugasKeyPressed
    
    public void hanyaangka(KeyEvent a){
        if(Character.isAlphabetic(a.getKeyChar())){
            a.consume();
            JOptionPane.showMessageDialog(null,"hanya angka",null,JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void hanyahuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
            JOptionPane.showMessageDialog(null,"hanya huruf",null,JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(formPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formPetugas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton clear;
    private javax.swing.ButtonGroup genderPetugas;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idPetugas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblid;
    private javax.swing.JTextField namaPetugas;
    private javax.swing.JPasswordField passPet;
    private javax.swing.JCheckBox showpass;
    private javax.swing.JButton simpanPetugas;
    private javax.swing.JTable tabelPetugas;
    private javax.swing.JButton ubah;
    private javax.swing.JTextField usernamePet;
    // End of variables declaration//GEN-END:variables
}
