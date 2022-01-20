/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import view.menuSimpananFrame;

/**
 *
 * @author rizky
 */
public class berandaFrameMaster extends javax.swing.JFrame {
    String akses;
    boolean tanggal15,pokokada,wajibada;
    /**
     * Creates new form berandaFrame
     */
    public berandaFrameMaster() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //set aktif
        keaktifan();
    }
    
    //menentukan id anggota
    public void setId(String ida){
        id.setText(ida);
        akses();
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
    
    //mengecek tanggal sudah tanggal 15/belum
    public void cekTanggal(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
        int tgl = Integer.parseInt(simpledate0.format(tglrealtime));
        int bulan = Integer.parseInt(simpledate1.format(tglrealtime));
        int tahun = Integer.parseInt(simpledate2.format(tglrealtime));
        
        if(tgl > 15){
            tanggal15 = false;
        }else if(tgl <= 15){
            tanggal15 = true;
        }
    }
    
    public void keaktifan(){
        try{
            String sql = "SELECT * FROM anggota";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
                
            if(rs.next()){
                java.util.Date tanggalreal = new Date();
                String tglgabung = rs.getString("tglgabung");
                java.util.Date tanggalgabung = new Date(tglgabung);
                    
                SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpledate1 = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
                    
                int tglreal = Integer.parseInt(simpledate0.format(tanggalreal));
                int bulanreal = Integer.parseInt(simpledate1.format(tanggalreal));
                int tahunreal = Integer.parseInt(simpledate2.format(tanggalreal));
                    
                int tglgab = Integer.parseInt(simpledate0.format(tanggalgabung));
                int bulangab = Integer.parseInt(simpledate1.format(tanggalgabung));
                int tahungab = Integer.parseInt(simpledate2.format(tanggalgabung));
                            
                if(bulangab == bulanreal && tahungab == tahunreal){
                    cekTanggal();
                        
                    if(tanggal15 == true){
                        String sql1 = "SELECT * FROM simpan WHERE jenis = 'POKOK'";
                        java.sql.Connection conn1=(Connection)config.configDB();
                        java.sql.Statement stmt1 = conn1.createStatement();
                        java.sql.ResultSet rs1 = stmt.executeQuery(sql1);
                            
                        int baris = 0;
                        for(baris = 0 ; baris <= rs1.getRow(); baris++){
                            if(rs1.next()){
                                String sql3 = "UPDATE anggota SET keterangan = 'AKTIF' WHERE id = '"+rs1.getString("idanggota")+"'";
                                java.sql.Connection conn3=(Connection)config.configDB();
                                java.sql.PreparedStatement pst3 = conn3.prepareStatement(sql3);
                                pst3.execute();
                            }
                        }
                    }else if(tanggal15 == false){
                        String sql1 = "SELECT * FROM simpan WHERE jenis = 'WAJIB'";
                        java.sql.Connection conn1=(Connection)config.configDB();
                        java.sql.Statement stmt1 = conn1.createStatement();
                        java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
                            
                        int baris = 0;
                        for(baris = 0 ; baris <= rs1.getRow(); baris++){
                            if(rs1.next()){
                                java.util.Date tglrealtime = new java.util.Date();
                                String tanggalsimpan = rs1.getString("tanggal");
                                java.util.Date tglsimpan = new Date(tanggalsimpan);
                            
                                //tgl realtime
                                int tglrealw = Integer.parseInt(simpledate0.format(tglrealtime));
                                int bulanrealw = Integer.parseInt(simpledate1.format(tglrealtime));
                                int tahunrealw = Integer.parseInt(simpledate2.format(tglrealtime));
                
                                //tgl simpan
                                int tglsimpw = Integer.parseInt(simpledate0.format(tglsimpan));
                                int bulansimpw = Integer.parseInt(simpledate1.format(tglsimpan));
                                int tahunsimpw = Integer.parseInt(simpledate2.format(tglsimpan));
                                
                                
                                if(bulansimpw >= bulanreal && tahunsimpw >= tahunreal){
                                    String sql2 = "UPDATE anggota SET keterangan = 'AKTIF' WHERE id = '"+rs1.getString("idanggota")+"'";
                                    java.sql.Connection conn2=(Connection)config.configDB();
                                    java.sql.PreparedStatement pst2 = conn.prepareStatement(sql2);
                                    pst2.execute();
                                }else{
                                    String sql2 = "UPDATE anggota SET keterangan = 'TIDAK AKTIF' WHERE id = '"+rs1.getString("idanggota")+"'";
                                    java.sql.Connection conn2=(Connection)config.configDB();
                                    java.sql.PreparedStatement pst2 = conn.prepareStatement(sql2);
                                    pst2.execute();
                                }
                            }
                        }
                    }
                }else{
                    
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
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

        about = new javax.swing.JDialog();
        tentang1 = new gambarimport.tentang();
        help = new javax.swing.JDialog();
        bantuan1 = new gambarimport.bantuan();
        panelsamping = new javax.swing.JPanel();
        txtAnggota = new javax.swing.JLabel();
        txtSimpan = new javax.swing.JLabel();
        txtPinjam = new javax.swing.JLabel();
        txtAngsuran = new javax.swing.JLabel();
        txtPetugas = new javax.swing.JLabel();
        tabungan = new javax.swing.JLabel();
        panelpojok = new javax.swing.JPanel();
        id = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        tentang = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        keanggotaan1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        panelatas = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        namaaplikasi = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lainnya = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        tentang1.setPreferredSize(new java.awt.Dimension(504, 360));

        javax.swing.GroupLayout tentang1Layout = new javax.swing.GroupLayout(tentang1);
        tentang1.setLayout(tentang1Layout);
        tentang1Layout.setHorizontalGroup(
            tentang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );
        tentang1Layout.setVerticalGroup(
            tentang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout aboutLayout = new javax.swing.GroupLayout(about.getContentPane());
        about.getContentPane().setLayout(aboutLayout);
        aboutLayout.setHorizontalGroup(
            aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tentang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        aboutLayout.setVerticalGroup(
            aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tentang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        bantuan1.setPreferredSize(new java.awt.Dimension(832, 507));

        javax.swing.GroupLayout bantuan1Layout = new javax.swing.GroupLayout(bantuan1);
        bantuan1.setLayout(bantuan1Layout);
        bantuan1Layout.setHorizontalGroup(
            bantuan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 832, Short.MAX_VALUE)
        );
        bantuan1Layout.setVerticalGroup(
            bantuan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 507, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout helpLayout = new javax.swing.GroupLayout(help.getContentPane());
        help.getContentPane().setLayout(helpLayout);
        helpLayout.setHorizontalGroup(
            helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpLayout.createSequentialGroup()
                .addComponent(bantuan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        helpLayout.setVerticalGroup(
            helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpLayout.createSequentialGroup()
                .addComponent(bantuan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelsamping.setBackground(new java.awt.Color(0, 204, 0));
        panelsamping.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelsampingMouseClicked(evt);
            }
        });

        txtAnggota.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        txtAnggota.setForeground(new java.awt.Color(255, 255, 255));
        txtAnggota.setText("ANGGOTA");
        txtAnggota.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtAnggota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtAnggotaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtAnggotaMouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAnggotaMouseClicked(evt);
            }
        });

        txtSimpan.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        txtSimpan.setForeground(new java.awt.Color(255, 255, 255));
        txtSimpan.setText("SIMPAN");
        txtSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSimpanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtSimpanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtSimpanMouseExited(evt);
            }
        });

        txtPinjam.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        txtPinjam.setForeground(new java.awt.Color(255, 255, 255));
        txtPinjam.setText("PINJAM");
        txtPinjam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtPinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPinjamMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPinjamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtPinjamMouseExited(evt);
            }
        });

        txtAngsuran.setFont(new java.awt.Font("Yu Gothic", 1, 10)); // NOI18N
        txtAngsuran.setForeground(new java.awt.Color(255, 255, 255));
        txtAngsuran.setText("ANGSURAN");
        txtAngsuran.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtAngsuran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtAngsuranMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtAngsuranMouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAngsuranMouseClicked(evt);
            }
        });

        txtPetugas.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        txtPetugas.setForeground(new java.awt.Color(255, 255, 255));
        txtPetugas.setText("PETUGAS");
        txtPetugas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPetugasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtPetugasMouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPetugasMouseClicked(evt);
            }
        });

        tabungan.setFont(new java.awt.Font("Yu Gothic", 1, 11)); // NOI18N
        tabungan.setForeground(new java.awt.Color(255, 255, 255));
        tabungan.setText("TABUNGAN");
        tabungan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabungan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabunganMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tabunganMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tabunganMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelsampingLayout = new javax.swing.GroupLayout(panelsamping);
        panelsamping.setLayout(panelsampingLayout);
        panelsampingLayout.setHorizontalGroup(
            panelsampingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelsampingLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelsampingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabungan)
                    .addGroup(panelsampingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtAngsuran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtAnggota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPetugas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelsampingLayout.setVerticalGroup(
            panelsampingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelsampingLayout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(txtPetugas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(txtAnggota)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(txtSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(txtPinjam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(txtAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(tabungan)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        panelpojok.setBackground(new java.awt.Color(0, 255, 0));

        id.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        id.setForeground(new java.awt.Color(102, 102, 102));
        id.setText("YOU ARE...");

        javax.swing.GroupLayout panelpojokLayout = new javax.swing.GroupLayout(panelpojok);
        panelpojok.setLayout(panelpojokLayout);
        panelpojokLayout.setHorizontalGroup(
            panelpojokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelpojokLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(id)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelpojokLayout.setVerticalGroup(
            panelpojokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelpojokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tentang.setForeground(new java.awt.Color(204, 0, 204));
        tentang.setText("LIHAT");
        tentang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tentang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tentangActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 204));
        jLabel3.setText("TENTANG");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(tentang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(111, 111, 111)
                .addComponent(tentang)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        keanggotaan1.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        keanggotaan1.setForeground(new java.awt.Color(51, 153, 255));
        keanggotaan1.setText("LIHAT");
        keanggotaan1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        keanggotaan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keanggotaan1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("LAPORAN");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(keanggotaan1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(119, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(keanggotaan1)
                .addContainerGap())
        );

        panelatas.setBackground(new java.awt.Color(255, 255, 255));

        logout.setForeground(new java.awt.Color(204, 0, 0));
        logout.setText("KELUAR");
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutMouseExited(evt);
            }
        });

        namaaplikasi.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        namaaplikasi.setForeground(new java.awt.Color(0, 204, 0));
        namaaplikasi.setText("KOPERASI SIMPAN PINJAM");

        javax.swing.GroupLayout panelatasLayout = new javax.swing.GroupLayout(panelatas);
        panelatas.setLayout(panelatasLayout);
        panelatasLayout.setHorizontalGroup(
            panelatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelatasLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(namaaplikasi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 480, Short.MAX_VALUE)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        panelatasLayout.setVerticalGroup(
            panelatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelatasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelatasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaaplikasi, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        lainnya.setText("LIHAT");
        lainnya.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lainnya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lainnyaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        jLabel4.setText("BANTUAN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lainnya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(111, 111, 111)
                .addComponent(lainnya)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelsamping, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelpojok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelatas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(62, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelpojok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelatas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(170, Short.MAX_VALUE))
                    .addComponent(panelsamping, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAnggotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnggotaMouseClicked
        formAnggotaMaster fa = new formAnggotaMaster();
        fa.setId(id.getText());
        fa.setVisible(true);
        dispose();
    }//GEN-LAST:event_txtAnggotaMouseClicked

    private void txtSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSimpanMouseClicked
        simpan s = new simpan();
        s.setAkses(id.getText());
        s.setVisible(true);
        dispose();
    }//GEN-LAST:event_txtSimpanMouseClicked

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        new loginFrameProject().setVisible(true);
        dispose();
    }//GEN-LAST:event_logoutMouseClicked

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        logout.setForeground(Color.pink);
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        logout.setForeground(Color.red);
    }//GEN-LAST:event_logoutMouseExited

    private void txtAnggotaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnggotaMouseEntered
        txtAnggota.setForeground(Color.gray);
    }//GEN-LAST:event_txtAnggotaMouseEntered

    private void txtAnggotaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAnggotaMouseExited
        txtAnggota.setForeground(Color.white);
    }//GEN-LAST:event_txtAnggotaMouseExited

    private void txtSimpanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSimpanMouseEntered
        txtSimpan.setForeground(Color.gray);
    }//GEN-LAST:event_txtSimpanMouseEntered

    private void txtSimpanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSimpanMouseExited
        txtSimpan.setForeground(Color.white);
    }//GEN-LAST:event_txtSimpanMouseExited

    private void keanggotaan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keanggotaan1ActionPerformed
        reportFrame fr = new reportFrame();
        fr.setId(id.getText());
        fr.setVisible(true);
        dispose();
    }//GEN-LAST:event_keanggotaan1ActionPerformed

    private void txtPinjamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPinjamMouseEntered
        txtPinjam.setForeground(Color.gray);
    }//GEN-LAST:event_txtPinjamMouseEntered

    private void txtPinjamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPinjamMouseExited
        txtPinjam.setForeground(Color.white);
    }//GEN-LAST:event_txtPinjamMouseExited

    private void txtPinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPinjamMouseClicked
        pinjam p = new pinjam();
        p.setId(id.getText());
        p.setVisible(true);
        dispose();
    }//GEN-LAST:event_txtPinjamMouseClicked

    private void txtPetugasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPetugasMouseEntered
        txtPetugas.setForeground(Color.gray);
    }//GEN-LAST:event_txtPetugasMouseEntered

    private void txtPetugasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPetugasMouseExited
        txtPetugas.setForeground(Color.white);
    }//GEN-LAST:event_txtPetugasMouseExited

    private void txtPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPetugasMouseClicked
        akses();
        
        if(akses.equals("ADMIN")){
            formPetugas fp = new formPetugas();
            fp.setId(id.getText());
            fp.setVisible(true);
            dispose();
        }else if(akses.equals("PETUGAS")){
            formPetugas fp = new formPetugas();
            fp.setId(id.getText());
            fp.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_txtPetugasMouseClicked

    private void txtAngsuranMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAngsuranMouseEntered
        txtAngsuran.setForeground(Color.gray);
    }//GEN-LAST:event_txtAngsuranMouseEntered

    private void txtAngsuranMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAngsuranMouseExited
        txtAngsuran.setForeground(Color.white);
    }//GEN-LAST:event_txtAngsuranMouseExited

    private void panelsampingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelsampingMouseClicked
        
    }//GEN-LAST:event_panelsampingMouseClicked

    private void txtAngsuranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAngsuranMouseClicked
        angsuran ang = new angsuran();
        ang.setId(id.getText());
        ang.setVisible(true);
        dispose();
    }//GEN-LAST:event_txtAngsuranMouseClicked

    private void tabunganMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabunganMouseEntered
        tabungan.setForeground(Color.gray);
    }//GEN-LAST:event_tabunganMouseEntered

    private void tabunganMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabunganMouseExited
        tabungan.setForeground(Color.white);
    }//GEN-LAST:event_tabunganMouseExited

    private void tabunganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabunganMouseClicked
        tabunganSukarela ts = new tabunganSukarela();
        ts.setId(id.getText());
        ts.setVisible(true);
        dispose();
    }//GEN-LAST:event_tabunganMouseClicked

    private void tentangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tentangActionPerformed
        about.setSize(504,400);
        about.setLocationRelativeTo(jPanel6);
        about.setVisible(true);
    }//GEN-LAST:event_tentangActionPerformed

    private void lainnyaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lainnyaActionPerformed
        help.setSize(832,540);
        help.setLocationRelativeTo(jPanel6);
        help.setVisible(true);
    }//GEN-LAST:event_lainnyaActionPerformed

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
            java.util.logging.Logger.getLogger(berandaFrameMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(berandaFrameMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(berandaFrameMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(berandaFrameMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new berandaFrameMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog about;
    private gambarimport.bantuan bantuan1;
    private javax.swing.JDialog help;
    private javax.swing.JLabel id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton keanggotaan1;
    private javax.swing.JButton lainnya;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel namaaplikasi;
    private javax.swing.JPanel panelatas;
    private javax.swing.JPanel panelpojok;
    private javax.swing.JPanel panelsamping;
    private javax.swing.JLabel tabungan;
    private javax.swing.JButton tentang;
    private gambarimport.tentang tentang1;
    private javax.swing.JLabel txtAnggota;
    private javax.swing.JLabel txtAngsuran;
    private javax.swing.JLabel txtPetugas;
    private javax.swing.JLabel txtPinjam;
    private javax.swing.JLabel txtSimpan;
    // End of variables declaration//GEN-END:variables
}
