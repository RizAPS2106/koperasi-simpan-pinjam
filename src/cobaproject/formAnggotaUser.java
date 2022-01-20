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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
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
public class formAnggotaUser extends javax.swing.JFrame {
    
    String gender,tgl;
    int panjangpass;
    boolean umur17,ktpada,usernameada,passwordvalid;
    
    public formAnggotaUser() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //set awal
        realtime();
        autokode();
        idAnggota.setEditable(false);
        lblid.setVisible(false);
        idAnggota.setVisible(false);
        tglAnggota.setDateFormatString("dd MMMMMMMMM YYYY");
        lbltgl.setVisible(false);
        tanggalreal.setVisible(false);
    }
    
    public void setId(String ida){
        idAnggota.setText(ida);
        
        tampilkanProfil();
    }
    
    public void tampilkanProfil(){
        try{
            String sql = "SELECT * FROM anggota WHERE id = '"+idAnggota.getText()+"'";
            java.sql.Connection conn = (Connection)config.configDB();
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            if(res.next()){
                if(idAnggota.getText().equals(res.getString("id"))){
                    simpanAnggota.setText("UBAH");
                    agreepokok.setVisible(false);
                    agreewajib.setVisible(false);
                    agreelunas.setVisible(false);
                
                    namaAnggota.setText(res.getString("nama"));
                    alamatAnggota.setText(res.getString("alamat"));
                    ((JTextField)tglAnggota.getDateEditor().getUiComponent()).setText(res.getString("tgllahir"));
                    tmptAnggota.setText(res.getString("tempatlahir"));
                    if(res.getString("gender").equals("laki laki")){
                        lakiAnggota.setSelected(true);
                    }else if(res.getString("gender").equals("perempuan")){
                        wanitaAnggota.setSelected(true);
                    }
                    telpAnggota.setText(res.getString("notlp"));
                    ktpAnggota.setText(res.getString("noktp"));
                    tanggalreal.setText(res.getString("tglgabung"));
                    usern.setText(res.getString("username"));
                    passw.setText(res.getString("password"));
                    clear.setVisible(false);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //menampilkan tanggal secara realtime/saat ini
    private void realtime(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        String tanggal = simpledate.format(tglrealtime);
        tanggalreal.setText(tanggal);
    }
    
    //fungsi simpan data ke database
    public void simpanData(){
        String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        gender();
        
        try{
            String sql = "SELECT * FROM anggota WHERE id = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            if(res.next()){
                if(idAnggota.getText().equals(res.getString("id"))){
                    String sql1 = "UPDATE anggota SET nama = '"+namaAnggota.getText()+"', alamat = '"+alamatAnggota.getText()+"', tgllahir = '"+tgl+"', tempatlahir = '"+tmptAnggota.getText()+"', gender = '"+gender+"', notlp = '"+telpAnggota.getText()+"', noktp = '"+ktpAnggota.getText()+"', username = '"+usern.getText()+"', password = '"+passw.getText()+"' WHERE id = '"+idAnggota.getText()+"'";
                    java.sql.Connection conn1=(Connection)config.configDB();
                    java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
                    pst1.execute();
                    JOptionPane.showMessageDialog(null,"Profil berubah");
                    tampilkanProfil();
                }
            }else{
                if(agreepokok.isSelected() && agreewajib.isSelected() && agreelunas.isSelected()){
                    String sql1 = "INSERT INTO anggota VALUES ('"+idAnggota.getText()+"','"+namaAnggota.getText()+"','"+alamatAnggota.getText()+"','"+tgl+"','"+tmptAnggota.getText()+"','"+gender+"','"+telpAnggota.getText()+"','"+ktpAnggota.getText()+"','SETUJU','SETUJU','SETUJU','"+tanggalreal.getText()+"','DAFTAR TUNGGU','"+usern.getText()+"','"+passw.getText()+"')";
                    java.sql.Connection conn1=(Connection)config.configDB();
                    java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
                    pst1.execute();
                    JOptionPane.showMessageDialog(null,"Berhasil daftar");
                    JOptionPane.showMessageDialog(null,"Anggota belum aktif lakukan simpanan di kasir untuk mengaktifkan");
                    bersihkan();
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //menentukan gender
    public void gender(){
        
        if(lakiAnggota.isSelected()){
            gender = "laki laki";
        }
        if(wanitaAnggota.isSelected()){
            gender = "perempuan";
        }
        
    }
    
    //fungsi ubah data ke database
    private void editData(){
        String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        
        try {
            String sql ="UPDATE anggota SET nama = '"+namaAnggota.getText()+"', alamat = '"+alamatAnggota.getText()+"', tempatlahir= '"+tmptAnggota.getText()+"', tgllahir = '"+tgl+"', gender= '"+gender+"', notlp= '"+telpAnggota.getText()+"', noktp= '"+ktpAnggota.getText()+"' WHERE id = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "berhasil di edit");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        bersihkan();
    }
    
    //fungsi hapus data
    private void hapus(){
        try {
            String sql ="delete from anggota where id='"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        bersihkan();
    }
    
    //fungsi membersihkan form
    private void bersihkan(){
        autokode();
        realtime();
        namaAnggota.setText("");
        alamatAnggota.setText("");
        ((JTextField)tglAnggota.getDateEditor().getUiComponent()).setText("");
        tmptAnggota.setText("");
        telpAnggota.setText("");
        ktpAnggota.setText("");
        usern.setText("");
        passw.setText("");
        agreepokok.setSelected(false);
        agreewajib.setSelected(false);
        agreelunas.setSelected(false);
        genderAnggota.clearSelection();
        simpanAnggota.setEnabled(true);
    }
    
    //set id auto
    private void autokode(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM anggota";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            
            while(rs.next()){
                if(rs.first()==false){
                    idAnggota.setText("A001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idAnggota.setText("A" + nomor);
                }
            }
        }catch(Exception e){}
    }
    
    //validasi umur sudah 17 tahun/belum
    public void validasiUmur(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpledate3 = new SimpleDateFormat("YYYY", Locale.getDefault());
        
        //tanggal lahir
        String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        java.util.Date tanggallahir = new Date(tgl);
        int tgllahir = Integer.parseInt(simpledate1.format(tanggallahir));
        int blnlahir = Integer.parseInt(simpledate2.format(tanggallahir));
        int thnlahir = Integer.parseInt(simpledate3.format(tanggallahir));
        
        //tanggal sekarang
        int tanggalsekarang = Integer.parseInt(simpledate1.format(tglrealtime));
        int bulansekarang = Integer.parseInt(simpledate2.format(tglrealtime));
        int tahunsekarang = Integer.parseInt(simpledate3.format(tglrealtime));
        
        //validasi
        if(thnlahir < tahunsekarang){
            if(blnlahir > bulansekarang){
                int umur = tahunsekarang - thnlahir -1;
                
                if(umur < 17){
                    umur17 = false;
                }else{
                    umur17 = true;
                }
            }else if(blnlahir == bulansekarang){
                if(tgllahir <= tanggalsekarang){
                    int umur = tahunsekarang - thnlahir;
                    
                    if(umur < 17){
                        umur17 = false;
                    }else{
                        umur17 = true;
                    }
                }else if(tgllahir > tanggalsekarang){
                    int umur = tahunsekarang - thnlahir - 1;
                    
                    if(umur < 17){
                        umur17 = false;
                    }else{
                        umur17 = true;
                    }
                }
            }else if(blnlahir < bulansekarang){
                int umurs = tahunsekarang - thnlahir;
                
                if(umurs < 17){
                    umur17 = false;
                }else{
                    umur17 = true;
                }
            }
        }
    }
    
    //validasi ktp sudah ada/belum
    public void validasiKtpAda(){
        try{
            String sql = "SELECT * FROM anggota WHERE noktp = '"+ktpAnggota.getText()+"' AND id != '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
                if(rs.next()){
                    if(ktpAnggota.getText().equals(rs.getString("noktp"))){
                        ktpada = true;
                    }
                }else{
                    ktpada = false;
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //validasi username sudah digunakan/belum
    public void validasiUsername(){
        try{
            String sql = "SELECT * FROM anggota WHERE username = '"+usern.getText()+"' AND id != '"+idAnggota.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
                if(rs.next()){
                    if(usern.getText().equals(rs.getString("username"))){
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
        panjangpass = passw.getText().length();
        if(panjangpass < 4 || panjangpass > 16){
            passwordvalid = false;
        }else{
            passwordvalid = true;
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

        genderAnggota = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tglAnggota = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        tmptAnggota = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lakiAnggota = new javax.swing.JRadioButton();
        wanitaAnggota = new javax.swing.JRadioButton();
        telpAnggota = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        alamatAnggota = new javax.swing.JTextArea();
        simpanAnggota = new javax.swing.JButton();
        back = new javax.swing.JButton();
        idAnggota = new javax.swing.JTextField();
        lblid = new javax.swing.JLabel();
        namaAnggota = new javax.swing.JTextField();
        clear = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ktpAnggota = new javax.swing.JTextField();
        agreewajib = new javax.swing.JCheckBox();
        agreelunas = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        usern = new javax.swing.JTextField();
        agreepokok = new javax.swing.JCheckBox();
        lihatpass = new javax.swing.JCheckBox();
        passw = new javax.swing.JPasswordField();
        lbltgl = new javax.swing.JLabel();
        tanggalreal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("FORM ANGGOTA");

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jLabel4.setText("NAMA                 ");

        jLabel5.setText("ALAMAT             ");

        tglAnggota.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tglAnggotaInputMethodTextChanged(evt);
            }
        });
        tglAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglAnggotaKeyPressed(evt);
            }
        });

        jLabel7.setText("TEMPAT LAHIR  ");

        tmptAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tmptAnggotaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tmptAnggotaKeyTyped(evt);
            }
        });

        jLabel8.setText("JENIS KELAMIN ");

        genderAnggota.add(lakiAnggota);
        lakiAnggota.setText("Laki laki");
        lakiAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lakiAnggotaKeyPressed(evt);
            }
        });

        genderAnggota.add(wanitaAnggota);
        wanitaAnggota.setText("Perempuan");
        wanitaAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                wanitaAnggotaKeyPressed(evt);
            }
        });

        telpAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                telpAnggotaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                telpAnggotaKeyTyped(evt);
            }
        });

        alamatAnggota.setColumns(20);
        alamatAnggota.setRows(5);
        alamatAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                alamatAnggotaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(alamatAnggota);

        simpanAnggota.setText("DAFTAR");
        simpanAnggota.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpanAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanAnggotaActionPerformed(evt);
            }
        });
        simpanAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                simpanAnggotaKeyPressed(evt);
            }
        });

        back.setText("Kembali");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        idAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idAnggotaActionPerformed(evt);
            }
        });

        lblid.setText("ID                       ");

        namaAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                namaAnggotaKeyPressed(evt);
            }
        });

        clear.setText("BERSIHKAN FORM");
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        jLabel2.setText("TGL LAHIR         ");

        jLabel3.setText("NO TELEPON     ");

        jLabel6.setText("NO KTP");

        ktpAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ktpAnggotaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ktpAnggotaKeyTyped(evt);
            }
        });

        agreewajib.setText("Sanggup membayar simpanan wajib setiap bulan");
        agreewajib.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                agreewajibKeyPressed(evt);
            }
        });

        agreelunas.setText("Sanggup membayar pinjaman hingga lunas");
        agreelunas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                agreelunasKeyPressed(evt);
            }
        });

        jLabel9.setText("USERNAME");

        jLabel10.setText("PASSWORD");

        usern.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usernKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usernKeyReleased(evt);
            }
        });

        agreepokok.setText("Sanggup membayar simpanan pokok");
        agreepokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                agreepokokKeyPressed(evt);
            }
        });

        lihatpass.setText("lihat");
        lihatpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lihatpassActionPerformed(evt);
            }
        });

        passw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwKeyPressed(evt);
            }
        });

        lbltgl.setText("TANGGAL");

        tanggalreal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tanggalrealKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(210, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(simpanAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(agreewajib)
                    .addComponent(agreelunas)
                    .addComponent(agreepokok)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(back)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(lbltgl, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(47, 47, 47)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tmptAnggota)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addComponent(namaAnggota)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lakiAnggota)
                                    .addGap(18, 18, 18)
                                    .addComponent(wanitaAnggota))
                                .addComponent(idAnggota)
                                .addComponent(telpAnggota)
                                .addComponent(ktpAnggota, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(tglAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(usern)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(passw)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lihatpass))
                                .addComponent(tanggalreal))
                            .addGap(2, 2, 2))))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblid, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tglAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tmptAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lakiAnggota)
                    .addComponent(wanitaAnggota))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telpAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ktpAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(usern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lihatpass)
                        .addComponent(passw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbltgl)
                    .addComponent(tanggalreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(agreepokok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(agreewajib, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(agreelunas)
                .addGap(18, 18, 18)
                .addComponent(simpanAnggota)
                .addGap(18, 18, 18)
                .addComponent(clear)
                .addGap(18, 18, 18)
                .addComponent(back)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void simpanAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanAnggotaActionPerformed
        validasiUmur();
        validasiKtpAda();
        validasiUsername();
        validasiPassword();
        
        if(umur17 == true){
            if(ktpada == false){
                if(usernameada == false){
                    if(passwordvalid == true){
                        simpanData();
                    }else{
                        JOptionPane.showMessageDialog(null, "Password minimal 4 karakter dan maximal 16 karakter");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Username sudah di gunakan");
                }
            }else{
                JOptionPane.showMessageDialog(null, "No KTP sudah di gunakan");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Umur tidak mencukupi");
        }
            
    }//GEN-LAST:event_simpanAnggotaActionPerformed
    
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
    
    private void alamatAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alamatAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
          tglAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_TAB){
          tglAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
          namaAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
          tglAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_alamatAnggotaKeyPressed

    private void lakiAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lakiAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            lakiAnggota.setSelected(true);
            gender();
            telpAnggota.requestFocus();
        } 
        if(evt.getKeyCode() == KeyEvent.VK_RIGHT){
            wanitaAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            tmptAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            telpAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_lakiAnggotaKeyPressed

    private void wanitaAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_wanitaAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            wanitaAnggota.setSelected(true);
            gender();
            telpAnggota.requestFocus();
        } 
        if(evt.getKeyCode() == KeyEvent.VK_LEFT){
            lakiAnggota.requestFocus(true);
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            tmptAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            telpAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_wanitaAnggotaKeyPressed

    private void simpanAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_simpanAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            simpanData();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            agreelunas.requestFocus();
        }
        
    }//GEN-LAST:event_simpanAnggotaKeyPressed

    private void tglAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            tmptAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            alamatAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            tmptAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_tglAnggotaKeyPressed

    private void tmptAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tmptAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            lakiAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            tglAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            lakiAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_tmptAnggotaKeyPressed

    private void telpAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telpAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            ktpAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            lakiAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            ktpAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_telpAnggotaKeyPressed

    private void tmptAnggotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tmptAnggotaKeyTyped
        
        hanyahuruf(evt);
        
    }//GEN-LAST:event_tmptAnggotaKeyTyped

    private void telpAnggotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telpAnggotaKeyTyped
        
        hanyaangka(evt);
        
    }//GEN-LAST:event_telpAnggotaKeyTyped

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        try{
            String sql = "SELECT * FROM anggota WHERE id = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            if(res.next()){
                if(idAnggota.getText().equals(res.getString("id"))){
                    berandaFrameUser bf = new berandaFrameUser();
                    bf.setId(idAnggota.getText());
                    bf.setVisible(true);
                    dispose();
                }
            }else{
                loginFrameProject lf = new loginFrameProject();
                lf.setVisible(true);
                dispose();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_backActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        
        bersihkan();
        
    }//GEN-LAST:event_clearActionPerformed

    private void namaAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaAnggotaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            alamatAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_TAB){
            alamatAnggota.requestFocus();
        }
    }//GEN-LAST:event_namaAnggotaKeyPressed

    private void idAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idAnggotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idAnggotaActionPerformed

    private void tglAnggotaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tglAnggotaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tglAnggotaInputMethodTextChanged

    private void lihatpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lihatpassActionPerformed
        if(lihatpass.isSelected()){
            passw.setEchoChar((char)0);
        }else{
            passw.setEchoChar('*');
        }
    }//GEN-LAST:event_lihatpassActionPerformed

    private void usernKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_usernKeyReleased

    private void ktpAnggotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ktpAnggotaKeyTyped
        
        hanyaangka(evt);
        
    }//GEN-LAST:event_ktpAnggotaKeyTyped

    private void ktpAnggotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ktpAnggotaKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            usern.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            telpAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            usern.requestFocus();
        }
        
    }//GEN-LAST:event_ktpAnggotaKeyPressed

    private void usernKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            passw.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            ktpAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            passw.requestFocus();
        }
        
    }//GEN-LAST:event_usernKeyPressed

    private void passwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            agreepokok.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            usern.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            agreepokok.requestFocus();
        }
        
    }//GEN-LAST:event_passwKeyPressed

    private void agreepokokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_agreepokokKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(agreepokok.isSelected()){
                agreepokok.setSelected(false);
            }else{
                agreepokok.setSelected(true);
            }
            agreewajib.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            passw.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            agreewajib.requestFocus();
        }
        
    }//GEN-LAST:event_agreepokokKeyPressed

    private void agreewajibKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_agreewajibKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(agreewajib.isSelected()){
                agreewajib.setSelected(false);
            }else{
                agreewajib.setSelected(true);
            }
            agreelunas.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            agreepokok.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            agreelunas.requestFocus();
        }
        
    }//GEN-LAST:event_agreewajibKeyPressed

    private void agreelunasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_agreelunasKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(agreelunas.isSelected()){
                agreelunas.setSelected(false);
            }else{
                agreelunas.setSelected(true);
            }
            simpanAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            agreewajib.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            simpanAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_agreelunasKeyPressed

    private void tanggalrealKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tanggalrealKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggalrealKeyPressed
    
    
    
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
            java.util.logging.Logger.getLogger(formAnggotaUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formAnggotaUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formAnggotaUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formAnggotaUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new formAnggotaUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox agreelunas;
    private javax.swing.JCheckBox agreepokok;
    private javax.swing.JCheckBox agreewajib;
    private javax.swing.JTextArea alamatAnggota;
    private javax.swing.JButton back;
    private javax.swing.JButton clear;
    private javax.swing.ButtonGroup genderAnggota;
    private javax.swing.JTextField idAnggota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField ktpAnggota;
    private javax.swing.JRadioButton lakiAnggota;
    private javax.swing.JLabel lblid;
    private javax.swing.JLabel lbltgl;
    private javax.swing.JCheckBox lihatpass;
    private javax.swing.JTextField namaAnggota;
    private javax.swing.JPasswordField passw;
    private javax.swing.JButton simpanAnggota;
    private javax.swing.JTextField tanggalreal;
    private javax.swing.JTextField telpAnggota;
    private com.toedter.calendar.JDateChooser tglAnggota;
    private javax.swing.JTextField tmptAnggota;
    private javax.swing.JTextField usern;
    private javax.swing.JRadioButton wanitaAnggota;
    // End of variables declaration//GEN-END:variables
}
