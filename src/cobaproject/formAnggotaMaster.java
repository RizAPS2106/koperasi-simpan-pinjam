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
public class formAnggotaMaster extends javax.swing.JFrame {

    String gender,tgl,akses;
    boolean umur17,ktpada,pinjamlunas;
    
    public formAnggotaMaster() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
     
        //set awal
        realtime();
        autokode();
        idAnggota.setEditable(false);
        simpanAnggota.setEnabled(false);
        hapus.setEnabled(false);
        lblid.setVisible(false);
        idAnggota.setVisible(false);
        tglreal.setEditable(false);
        tglAnggota.setDateFormatString("dd MMMMMMMMM YYYY");
        load_table();
    }

    public void setId(String ida){
        id.setText(ida);
        akses();
        load_table();
    }
    
    private void load_table(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Tempat Lahir");
        model.addColumn("Jenis Kelamin");
        model.addColumn("No Telp");
        model.addColumn("No Ktp");
        model.addColumn("Tanggal bergabung");
        
        //menampilkan data database ke dalam tabel
        try{
            int no=1;
            String sql = "select * from anggota";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                model.addRow(new Object[]{res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(12)});
            }
            tabelAnggota.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void akses(){
        try{
            String sql = "select * from admin where id = '"+id.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            if(res.next()){
                if(id.getText().equals(res.getString("id"))){
                    akses = "ADMIN";
                }
            }else{
                String sql1 = "select * from admin where id = '"+id.getText()+"'";
                java.sql.Connection conn1=(Connection)config.configDB();
                java.sql.Statement stm1=conn1.createStatement();
                java.sql.ResultSet res1=stm1.executeQuery(sql1);
                
                if(res1.next()){
                    if(res1.getString("akses").equals("PETUGAS")){
                        akses = "PETUGAS";
                    }
                }
            }
        }catch(Exception e){}
    }
    
    //menampilkan tanggal secara realtime/saat ini
    private void realtime(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        String tanggal = simpledate.format(tglrealtime);
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
    
    //mengecek pinjaman
    public void cekPinjam(){
        try{
            String sql = "SELECT * FROM pinjam WHERE idanggota = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            if(res.next()){
                if(idAnggota.getText().equals("idanggota")){
                    cekLunas();
                }
            }else{
                pinjamlunas = true;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //mengecek pinjaman lunas/belum
    public void cekLunas(){
        try{
            String sql = "SELECT * FROM pinjam WHERE idanggota = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            if(res.next()){
                if(res.getString("status").equals("LUNAS")){
                    pinjamlunas = true;
                }
            }else{
                pinjamlunas = false;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //tabungan yang di kembalikan
    public void tabunganSukarela(){
        try{
            String sql = "SELECT * FROM saldo WHERE idanggota = '"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                JOptionPane.showMessageDialog(null, "jumlah tabungan yang harus dikembalikan: "+res.getString("saldosukarela"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
            
    //fungsi simpan data ke database
    public void simpanData(){
        String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        gender();
        
        try{
            String sql1 = "UPDATE anggota SET nama = '"+namaAnggota.getText()+"', alamat = '"+alamatAnggota.getText()+"', tgllahir = '"+tgl+"', tempatlahir = '"+tmptAnggota.getText()+"', gender = '"+gender+"', notlp = '"+telpAnggota.getText()+"', noktp = '"+ktpAnggota.getText()+"' WHERE id = '"+idAnggota.getText()+"'";
            java.sql.Connection conn1=(Connection)config.configDB();
            java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
            pst1.execute();
            JOptionPane.showMessageDialog(null,"Data berhasil di ubah");
            bersihkan();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
    }
    
    // fungsi hapus data
    private void hapus(){
        try {
            String sql ="delete from anggota where id='"+idAnggota.getText()+"'";
            String sql1 ="delete from simpan where idanggota='"+idAnggota.getText()+"'";
            String sql2 ="delete from saldo where idanggota='"+idAnggota.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.PreparedStatement pst1=conn.prepareStatement(sql1);
            java.sql.PreparedStatement pst2=conn.prepareStatement(sql2);
            pst.execute();
            pst1.execute();
            pst2.execute();
            
            //hapus pinjaman dan angsuran
            String sql3 ="select * from pinjam where idanggota='"+idAnggota.getText()+"'";
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql3);
            while(res.next()){
                String sql31 ="delete from pinjam where id='"+res.getString("id")+"'";
                String sql32 ="delete from angsuran where idpinjam='"+res.getString("id")+"'";
                java.sql.PreparedStatement pst31=conn.prepareStatement(sql31);
                java.sql.PreparedStatement pst32=conn.prepareStatement(sql32);
                pst31.execute();
                pst32.execute();
            }
            //end of hapus pinjaman dan angsuran
            
            JOptionPane.showMessageDialog(this, "berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
        bersihkan();
    }
    
    //membersihkan form
    private void bersihkan(){
        autokode();
        realtime();
        namaAnggota.setText("");
        alamatAnggota.setText("");
        ((JTextField)tglAnggota.getDateEditor().getUiComponent()).setText("");
        tmptAnggota.setText("");
        telpAnggota.setText("");
        ktpAnggota.setText("");
        genderAnggota.clearSelection();
        tglreal.setText("");
        simpanAnggota.setEnabled(false);
        hapus.setEnabled(false);
    }
    
    //menentukan id secara auto
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
    
    //validasi umur 17 tahun
    public void validasiUmur(){
        Locale locale = new Locale("us","US");
        Locale.setDefault(locale);
        tglAnggota.setLocale(locale);
        tglAnggota.setDateFormatString("dd MMMMMMMMM YYYY");
        
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpledate3 = new SimpleDateFormat("YYYY", Locale.getDefault());
        
        //tanggal lahir
        String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        java.util.Date lahir = new Date(tgl);
        int tgllahir = Integer.parseInt(simpledate1.format(lahir));
        int blnlahir = Integer.parseInt(simpledate2.format(lahir));
        int thnlahir = Integer.parseInt(simpledate3.format(lahir));
        
        //tanggal sekarang
        int tanggalsekarang = Integer.parseInt(simpledate1.format(tglrealtime));
        int bulansekarang = Integer.parseInt(simpledate2.format(tglrealtime));
        int tahunsekarang = Integer.parseInt(simpledate3.format(tglrealtime));
        
        //validasi umur 17 tahun
        if(thnlahir < tahunsekarang){
            if(blnlahir < bulansekarang){
                int umur = tahunsekarang - thnlahir ;
                
                if(umur < 17){
                    umur17 = false;
                }else{
                    umur17 = true;
                }
            }else if(blnlahir == bulansekarang){
                if(tgllahir > tanggalsekarang){
                    int umur = tahunsekarang - thnlahir - 1;
                    
                    if(umur < 17){
                        umur17 = false;
                    }else{
                        umur17 = true;
                    }
                }else if(tgllahir <= tanggalsekarang){
                    int umur = tahunsekarang - thnlahir ;
                    
                    if(umur < 17){
                        umur17 = false;
                    }else{
                        umur17 = true;
                    }
                }
            }else if(blnlahir > bulansekarang){
                int umurs = tahunsekarang - thnlahir - 1;
                
                if(umurs < 17){
                    umur17 = false;
                }else{
                    umur17 = true;
                }
            }
        }
        Locale lokal = new Locale("id","ID");
        Locale.setDefault(lokal);
        tglAnggota.setLocale(lokal);
        tglAnggota.setDateFormatString("dd MMMMMMMMM YYYY");
    }
    
    public void validasiKtpAda(){
        try{
            String sql = "SELECT * FROM anggota WHERE id != '"+idAnggota.getText()+"' AND noktp = '"+ktpAnggota.getText()+"' ";
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
        hapus = new javax.swing.JButton();
        idAnggota = new javax.swing.JTextField();
        lblid = new javax.swing.JLabel();
        namaAnggota = new javax.swing.JTextField();
        clear = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelAnggota = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ktpAnggota = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tglreal = new javax.swing.JTextField();

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

        simpanAnggota.setText("UBAH DATA");
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

        hapus.setText("HAPUS DATA");
        hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
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

        tabelAnggota.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelAnggota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelAnggotaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelAnggota);

        jLabel3.setText("NO TELEPON     ");

        id.setText("YOU ARE...");

        jLabel6.setText("NO KTP");

        jLabel9.setText("TANGGAL BERGABUNG");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(simpanAnggota)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hapus))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tmptAnggota, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                    .addComponent(namaAnggota, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idAnggota, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(telpAnggota, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ktpAnggota)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(lakiAnggota)
                                                .addGap(18, 18, 18)
                                                .addComponent(wanitaAnggota))
                                            .addComponent(tglAnggota, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tglreal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblid, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tglAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(16, 16, 16)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(ktpAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tglreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(simpanAnggota)
                            .addComponent(hapus))
                        .addGap(25, 25, 25)
                        .addComponent(clear))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(id))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void simpanAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanAnggotaActionPerformed
        validasiUmur();
        validasiKtpAda();
        
        if(umur17 == true){
            if(ktpada == false){
                simpanData();
            }else{
                JOptionPane.showMessageDialog(null, "Ktp sudah digunakan");
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
            telpAnggota.requestFocus();
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
            simpanAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            lakiAnggota.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            simpanAnggota.requestFocus();
        }
        
    }//GEN-LAST:event_telpAnggotaKeyPressed

    private void tmptAnggotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tmptAnggotaKeyTyped
        
        hanyahuruf(evt);
        
    }//GEN-LAST:event_tmptAnggotaKeyTyped

    private void telpAnggotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telpAnggotaKeyTyped
        
        hanyaangka(evt);
        
    }//GEN-LAST:event_telpAnggotaKeyTyped

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(id.getText());
        bf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void tabelAnggotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelAnggotaMouseClicked
       String tgl=((JTextField)tglAnggota.getDateEditor().getUiComponent()).getText();
        
        // menampilkan data kedalam form
        int baris = tabelAnggota.rowAtPoint(evt.getPoint());
        String id =tabelAnggota.getValueAt(baris, 0).toString();
        idAnggota.setText(id);
        String nama = tabelAnggota.getValueAt(baris,1).toString();
        namaAnggota.setText(nama);
        String alamat=tabelAnggota.getValueAt(baris, 2).toString();
        alamatAnggota.setText(alamat);
        String tanggal=tabelAnggota.getValueAt(baris, 3).toString();
        ((JTextField)tglAnggota.getDateEditor().getUiComponent()).setText(tanggal);
        String tempat=tabelAnggota.getValueAt(baris, 4).toString();
        tmptAnggota.setText(tempat);
        String gendera = tabelAnggota.getValueAt(baris, 5).toString();
            switch(gendera){
                case "laki laki" : lakiAnggota.setSelected(true);
                break ;
                case "perempuan" : wanitaAnggota.setSelected(true);
                break;
            }
        String telp = tabelAnggota.getValueAt(baris,6).toString();
        telpAnggota.setText(telp);
        String ktp = tabelAnggota.getValueAt(baris,7).toString();
        ktpAnggota.setText(ktp);
        String  tglgab= tabelAnggota.getValueAt(baris,8).toString();
        tglreal.setText(tglgab);
        
        simpanAnggota.setEnabled(true);
        hapus.setEnabled(true);
    }//GEN-LAST:event_tabelAnggotaMouseClicked

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

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        cekPinjam();
        
        if(pinjamlunas == true){
            tabunganSukarela();
            hapus();
        }else{
            JOptionPane.showMessageDialog(null, "Tidak bisa di hapus karena ada pinjaman yang belum lunas");
        }
    }//GEN-LAST:event_hapusActionPerformed

    private void idAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idAnggotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idAnggotaActionPerformed

    private void tglAnggotaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tglAnggotaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tglAnggotaInputMethodTextChanged
    
    
    
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
            java.util.logging.Logger.getLogger(formAnggotaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formAnggotaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formAnggotaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formAnggotaMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formAnggotaMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamatAnggota;
    private javax.swing.JButton back;
    private javax.swing.JButton clear;
    private javax.swing.ButtonGroup genderAnggota;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idAnggota;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField ktpAnggota;
    private javax.swing.JRadioButton lakiAnggota;
    private javax.swing.JLabel lblid;
    private javax.swing.JTextField namaAnggota;
    private javax.swing.JButton simpanAnggota;
    private javax.swing.JTable tabelAnggota;
    private javax.swing.JTextField telpAnggota;
    private com.toedter.calendar.JDateChooser tglAnggota;
    private javax.swing.JTextField tglreal;
    private javax.swing.JTextField tmptAnggota;
    private javax.swing.JRadioButton wanitaAnggota;
    // End of variables declaration//GEN-END:variables
}
