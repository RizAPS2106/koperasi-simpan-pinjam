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
public class formPetugasUser extends javax.swing.JFrame {
    
    String gender,tgl;
    boolean sudahada,aktif;
    
    public formPetugasUser() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        autokode();
        lblid.setVisible(false);
        idPetugas.setVisible(false);
        ubah.setEnabled(false);
        hapus.setEnabled(false);
        idang.setEditable(false);
        namaPetugas.setEditable(false);
        load_table();
        
    }
    
    public void setAkses(String aks){
        akses.setText(aks);
    }
    
    private void load_table(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Id anggota");
        model.addColumn("Nama");
        model.addColumn("Akses");
        
        //menampilkan data database ke dalam tabel
        try{
            int no=1;
            String sql = "select * from petugas";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
                while(res.next()){
                    String sql1 = "select * from anggota where id = '"+res.getString("idanggota")+"'";
                    java.sql.Connection conn1=(Connection)config.configDB();
                    java.sql.Statement stm1=conn1.createStatement();
                    java.sql.ResultSet res1=stm1.executeQuery(sql1);
                        while(res1.next()){
                            model.addRow(new Object[]{res.getString(1),res.getString(2),res1.getString("nama"),res.getString(5)});
                        }
                }
            tabelPetugas.setModel(model);
        }catch(Exception e){
            
        }
    }
    
    public void simpanData(){
        String idanggota,nama,alamat,tlah,tempatlah,jenkel,notlp,username,pass;
        idanggota = idang.getText();
        nama = namaPetugas.getText();
        username = usernamePet.getText();
        pass = passPet.getText();
        
        if(idanggota.equals("") || username.equals("") || pass.equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }else{
            simpan();
        }
    }
    
    public void simpan(){
        //fungsi
        validasiPetugas();
        validasiAktif();
        
        if(sudahada == true){
            JOptionPane.showMessageDialog(null, "Anggota ini sudah menjadi petugas");
            bersihkan();
        }else if(aktif == false){
            JOptionPane.showMessageDialog(null, "Anggota ini belum aktif");
            bersihkan();
        }else{
            try{
                String sql = "SELECT * FROM petugas WHERE username = '"+usernamePet.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                pst.execute();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql);
            
                //jika username/pass sudah ada
                if(rs.next()){
                    if(usernamePet.getText().equals(rs.getString("username"))){
                        JOptionPane.showMessageDialog(null, "Username sudah digunakan");
                    }
            
                //jika username/pass belum ada    
                }else{
                    String sql1 = "INSERT INTO petugas VALUES('"+idPetugas.getText()+"','"+idang.getText()+"','"+usernamePet.getText()+"','"+passPet.getText()+"','"+cmbakses.getSelectedItem()+"')";
                    pst.executeUpdate(sql1);
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
                    bersihkan();
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
        
        load_table();
    }
    
    public void ubahData(){
        String idanggota,nama,alamat,tlah,tempatlah,jenkel,notlp,username,pass;
        idanggota = idang.getText();
        nama = namaPetugas.getText();
        username = usernamePet.getText();
        pass = passPet.getText();
        
        if(idanggota.equals("") || username.equals("") || pass.equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }else{
            ubah();
        }
    }
    
    private void ubah(){
        //fungsi
        
        try{
            String sql = "UPDATE petugas SET username = '"+usernamePet.getText()+"', password = '"+passPet.getText()+"', akses = '"+cmbakses.getSelectedItem()+"' WHERE id = '"+idPetugas.getText()+"' ";
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
        bersihkan();
    }
    
    private void bersihkan(){
        autokode();
        
        idang.setText("");
        namaPetugas.setText("");
        usernamePet.setText("");
        passPet.setText("");
        
        simpanPetugas.setEnabled(true);
        ubah.setEnabled(false);
        hapus.setEnabled(false);
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
    
    //menampilkan nama di tabel dari cari
    private void load_table_cariang(){
        
        //membuat tampilan model tabel
        DefaultTableModel model1 = new DefaultTableModel();
        model1.addColumn("id");
        model1.addColumn("nama");
        
        //menampilkan database, tabel anggota, kolom id dan nama ke dalam jtable
        try{
            int no=1;
            String sql = "select * from anggota where nama like '%"+carinama.getText()+"%'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
                while(res.next()){
                    if(!(carinama.getText().equals(""))){
                        model1.addRow(new Object[]{res.getString("id"),res.getString("nama")});
                    }
                }
            tblcari.setModel(model1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    public void validasiPetugas(){
        try{
            String sql = "select * from petugas where idanggota = '"+idang.getText()+"'";
            java.sql.Connection conn = config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            if(res.next()){
                if(idang.getText().equals(res.getString("idanggota"))){
                    sudahada = true;
                }
            }else{
                sudahada = false;
            }
        }catch(Exception e){}
    }
    
    public void validasiAktif(){
        
        try{
            String sql = "select * from anggota where id = '"+idang.getText()+"'";
            java.sql.Connection conn = config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            if(res.next()){
                if(res.getString("keterangan").equals("AKTIF")){
                    aktif = true;
                }
            }else{
                aktif = false;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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
        jLabel4 = new javax.swing.JLabel();
        simpanPetugas = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        namaPetugas = new javax.swing.JTextField();
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
        jLabel2 = new javax.swing.JLabel();
        cmbakses = new javax.swing.JComboBox<String>();
        showpass = new javax.swing.JCheckBox();
        akses = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        idang = new javax.swing.JTextField();
        carinama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblcari = new javax.swing.JTable();

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

        jLabel4.setText("NAMA                 ");

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

        namaPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaPetugasActionPerformed(evt);
            }
        });
        namaPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaPetugasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                namaPetugasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                namaPetugasKeyTyped(evt);
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

        hapus.setText("HAPUS DATA");
        hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        lblid.setText("ID                       ");

        jLabel2.setText("AKSES              ");

        cmbakses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PETUGAS", "ADMIN" }));

        showpass.setText("Tampilkan Password");
        showpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpassActionPerformed(evt);
            }
        });

        akses.setText("YOU ARE...");

        jLabel3.setText("ID ANGGOTA      ");

        idang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idangActionPerformed(evt);
            }
        });

        carinama.setText("cari nama...");
        carinama.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carinamaMouseClicked(evt);
            }
        });
        carinama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                carinamaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                carinamaKeyTyped(evt);
            }
        });

        tblcari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "id", "nama"
            }
        ));
        tblcari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcariMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblcari);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(simpanPetugas)
                                .addGap(33, 33, 33)
                                .addComponent(ubah)
                                .addGap(37, 37, 37)
                                .addComponent(hapus))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(namaPetugas)
                                    .addComponent(carinama)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(idang)
                                    .addComponent(idPetugas)
                                    .addComponent(usernamePet)
                                    .addComponent(passPet)
                                    .addComponent(showpass)
                                    .addComponent(cmbakses, 0, 258, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(akses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(back)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblid)
                            .addComponent(idPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(namaPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addComponent(carinama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(usernamePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(passPet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(showpass)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbakses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(simpanPetugas)
                            .addComponent(ubah)
                            .addComponent(hapus))
                        .addGap(18, 18, 18)
                        .addComponent(clear))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(akses))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpanPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanPetugasActionPerformed
        
        simpan();
        
    }//GEN-LAST:event_simpanPetugasActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        
        bersihkan();
        
    }//GEN-LAST:event_clearActionPerformed

    private void namaPetugasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaPetugasKeyTyped
        
        hanyahuruf(evt);
        
    }//GEN-LAST:event_namaPetugasKeyTyped

    private void namaPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaPetugasKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            carinama.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            carinama.requestFocus();
        }
        
    }//GEN-LAST:event_namaPetugasKeyPressed

    private void usernamePetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernamePetKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            passPet.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            carinama.requestFocus();
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
            simpanPetugas.requestFocus();
        }
        
    }//GEN-LAST:event_passPetKeyPressed

    private void simpanPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_simpanPetugasKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            simpanData();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            passPet.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            clear.requestFocus();
        }
        
    }//GEN-LAST:event_simpanPetugasKeyPressed

    private void clearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clearKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            new dataPetugas().setVisible(true);
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            simpanPetugas.requestFocus();
        }
        
    }//GEN-LAST:event_clearKeyPressed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(akses.getText());
        bf.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_backActionPerformed

    private void tabelPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPetugasMouseClicked
        
        // menampilkan data kedalam form pengisian:
        int baris = tabelPetugas.rowAtPoint(evt.getPoint());
        String id =tabelPetugas.getValueAt(baris, 0).toString();
        idPetugas.setText(id);
        String idanggota = tabelPetugas.getValueAt(baris, 1).toString();
        idang.setText(idanggota);
        String nama = tabelPetugas.getValueAt(baris,2).toString();
        namaPetugas.setText(nama);
        String aks = tabelPetugas.getValueAt(baris,3).toString();
        cmbakses.setSelectedItem(aks);
        
        try{
            String sql = "SELECT * FROM petugas WHERE id = '"+id+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            
            while(rs.next()){
                usernamePet.setText(rs.getString("username"));
                passPet.setText(rs.getString("password"));
            }
        }catch(Exception e){
        }
        
        simpanPetugas.setEnabled(false);
        ubah.setEnabled(true);
        hapus.setEnabled(true);
        
    }//GEN-LAST:event_tabelPetugasMouseClicked

    private void ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahActionPerformed
        
        ubahData();
        
    }//GEN-LAST:event_ubahActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        hapusData();
    }//GEN-LAST:event_hapusActionPerformed

    private void namaPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaPetugasActionPerformed
        
    }//GEN-LAST:event_namaPetugasActionPerformed

    private void showpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpassActionPerformed
        if(showpass.isSelected()){
            passPet.setEchoChar((char)0);
        }else{
            passPet.setEchoChar('*');
        }
    }//GEN-LAST:event_showpassActionPerformed

    private void carinamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carinamaMouseClicked
        carinama.setText("");
    }//GEN-LAST:event_carinamaMouseClicked

    private void carinamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyReleased
        load_table_cariang();
    }//GEN-LAST:event_carinamaKeyReleased

    private void carinamaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyTyped
        hanyahuruf(evt);
    }//GEN-LAST:event_carinamaKeyTyped

    private void tblcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcariMouseClicked

        // menampilkan data dari tabel ke form
        int baris = tblcari.rowAtPoint(evt.getPoint());
        String idanggota = tblcari.getValueAt(baris, 0).toString();
        String nama = tblcari.getValueAt(baris,1).toString();
        idang.setText(idanggota);
        namaPetugas.setText(nama);
        carinama.setText("");
        load_table_cariang();
    }//GEN-LAST:event_tblcariMouseClicked

    private void idangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idangActionPerformed

    private void passPetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passPetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passPetActionPerformed

    private void namaPetugasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaPetugasKeyReleased
        
    }//GEN-LAST:event_namaPetugasKeyReleased
    
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
            java.util.logging.Logger.getLogger(formPetugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formPetugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formPetugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formPetugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formPetugasUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel akses;
    private javax.swing.JButton back;
    private javax.swing.JTextField carinama;
    private javax.swing.JButton clear;
    private javax.swing.JComboBox<String> cmbakses;
    private javax.swing.ButtonGroup genderPetugas;
    private javax.swing.JButton hapus;
    private javax.swing.JTextField idPetugas;
    private javax.swing.JTextField idang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblid;
    private javax.swing.JTextField namaPetugas;
    private javax.swing.JPasswordField passPet;
    private javax.swing.JCheckBox showpass;
    private javax.swing.JButton simpanPetugas;
    private javax.swing.JTable tabelPetugas;
    private javax.swing.JTable tblcari;
    private javax.swing.JButton ubah;
    private javax.swing.JTextField usernamePet;
    // End of variables declaration//GEN-END:variables
}
