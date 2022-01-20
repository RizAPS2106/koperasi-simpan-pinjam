/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.menuSimpananFrame;

/**
 *
 * @author rizky
 */
public class transaksiWajib extends javax.swing.JFrame {
    
    boolean bulansama;
    /**
     * Creates new form bayarWajbFrame
     */
    public transaksiWajib() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //settingan awal
        idsimp.setEditable(false);
        idang.setEditable(false);
        idsal.setEditable(false);
        sisa.setEditable(false);
        
        //tampil-tampil
        load_table();
        load_table_cariang();
        tampilkanDataFromId();
        tampilkanDataSimpanFromId();
        realtime();
        
        //fungsi awal
        bulantahun();
    }
    
    public void setAkses(String aks){
        akses.setText(aks);
    }
    
    private void jikaidkosong(){
        if(idsimp.getText().equals("")){
            bayarbutton.setEnabled(false);
        }else{
            bayarbutton.setEnabled(true);
        }
    }
    
    //menentukan lunas tidaknya suatu simpanan
    private void status(){
        if(jumlah.getText().equals("0")){
            stat.setSelectedItem("LUNAS");
        }else if(sisa.getText().equals("0")){
            stat.setSelectedItem("LUNAS");
        }else{
            stat.setSelectedItem("BELUM LUNAS");
        }
    }
    
    //settinan jika sudah lunas
    private void jikalunas(){
        
        if(jumlah.getText().equals("0")){
            jumlah.setEditable(false);
            bayar.setEditable(false);
        }else{
            jumlah.setEditable(true);
            bayar.setEditable(true);
        }
    }
    
    //mengitung bayaran
    private void hitungan(){
        if(jumlah.getText().equals("") || bayar.getText().equals("")){
            sisa.setText("");
        }else{
            int jmlh,byr,sis;
            jmlh = Integer.parseInt(jumlah.getText());
            byr = Integer.parseInt(bayar.getText());
            sis = jmlh-byr;
            sisa.setText(""+sis);
        }
    }
    
     //menampilkan tanggal secara realtime/saat ini
    private void realtime(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("MMMMMMMMM", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
        String tanggalrealtime = simpledate.format(tglrealtime);
        String bulanrealtime = simpledate1.format(tglrealtime);
        String tahunrealtime = simpledate2.format(tglrealtime);
        tgl.setText(tanggalrealtime);
        bulan.setSelectedItem(bulanrealtime);
        tahun.setText(tahunrealtime);
    }
    
    //ketika bulan & tahun di pilih
    private void bulantahun(){
        
        //jika tahun nya kosong
        if(tahun.getText().equals("")){
            jatuhtempo.setText("");
        //jika tahun tidak kosong    
        }else{
            jatuhtempo.setText("15 "+bulan.getSelectedItem()+" "+tahun.getText());
        }
    }
    
    public void validasiBulan(){
        try{
            String sql1 = "SELECT * FROM simpan WHERE idanggota = '"+idang.getText()+"'";
            java.sql.Connection conn1=(Connection)config.configDB();
            java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
            pst1.execute();
            java.sql.Statement stmt1 = conn1.createStatement();
            java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
            
            if(rs1.next()){
                java.util.Date tglrealtime = new java.util.Date();
                SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
                SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpledate1 = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
                String tanggal = simpledate0.format(tglrealtime);
                String pilihbulan = bulan.getSelectedItem().toString();
                String pilihtahun = tahun.getText();
                String tanggaldefault = tanggal+" "+pilihbulan+" "+pilihtahun;
                java.util.Date tgldef= new Date(tanggaldefault);
                int tgl = Integer.parseInt(simpledate0.format(tgldef));
                int bulan = Integer.parseInt(simpledate1.format(tgldef));
                int tahun = Integer.parseInt(simpledate2.format(tgldef));
                
                String tanggalsimpan = rs1.getString("tanggal");
                java.util.Date tglsimpan = new Date(tanggalsimpan);
                SimpleDateFormat simpledate3 = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpledate4 = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat simpledate5 = new SimpleDateFormat("YYYY", Locale.getDefault());
                int tanggalsimp = Integer.parseInt(simpledate3.format(tglsimpan));
                int bulansimp = Integer.parseInt(simpledate4.format(tglsimpan));
                int tahunsimp = Integer.parseInt(simpledate5.format(tglsimpan));
                
                if(bulansimp == bulan && tahunsimp == tahun){
                    bulansama = true;
                }else{
                    bulansama = false;
                }
            }
        }catch(Exception e){
        
        }
    }
    
    private void load_table(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("id anggota");
        model.addColumn("nama");
        model.addColumn("jumlah");
        model.addColumn("dibayar");
        model.addColumn("sisa");
        model.addColumn("tanggal");
        model.addColumn("status");
        model.addColumn("jenis");
        model.addColumn("bulan");
        model.addColumn("tahun");
        model.addColumn("jatuh tempo");
        
        //menampilkan data dari database
        try{
            int no=1;
            String sql = "select * from simpan where jenis = 'WAJIB' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
                while(res.next()){
                    model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10), res.getString(11), res.getString(12)});
                }
            tabelSimpananWajib.setModel(model);
        }catch(Exception e){
            
        }
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
    
    //menampilkan id dari database, tabel anggota, nama
    public void tampilkanDataFromNama(){
            try{
                String sql = "SELECT id FROM anggota WHERE nama='"+namang.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                idang.setText((String)ob[0]);
            }
                rs.close();
                st.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
    
    //menampilkan namaanggota dari database, tabel anggota, id
    public void tampilkanDataFromId(){
            try{
                String sql = "SELECT nama FROM anggota WHERE id='"+idang.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                namang.setText((String)ob[0]);
            }
                rs.close();
                st.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
    
    //menampilkan namaanggota dari database, tabel anggota, id
    public void tampilkanIDSfromIDA(){
            try{
                String sql = "SELECT id,bulan,tahun FROM simpan WHERE idanggota='"+idang.getText()+"' AND jenis='WAJIB' AND bulan='"+bulan.getSelectedItem()+"' AND tahun='"+tahun.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                if(bulan.getSelectedItem().equals(rs.getString("bulan")) && tahun.getText().equals(rs.getString("tahun"))){
                    Object[] ob = new Object[3];
                    ob[0] = rs.getString(1);
                    idsimp.setText((String)ob[0]);
                }
            }else{
                idsimp.setText("");
            }
                rs.close();
                st.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
    
    //menampilkan namaanggota dari database, tabel anggota, id
    public void tampilkanIDSALfromIDA(){
            try{
                String sql = "SELECT id FROM saldo WHERE idanggota='"+idang.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                idsal.setText((String)ob[0]);
            }
                rs.close();
                st.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
    
    //menampilkan namaanggota dari database, tabel anggota, id
    public void tampilkanDataSimpanFromId(){
            try{
                String sql = "SELECT sisa FROM simpan WHERE id='"+idsimp.getText()+"'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                jumlah.setText((String)ob[0]);
                bayar.setText("0");
            }
                rs.close();
                st.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    }
    
    //menyimpan data simpanan ke database
    public void simpanData(){
        //fungsi
        try{
            String sql = "SELECT * FROM simpan WHERE id = '"+idsimp.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            //jika id sudah ada
            if(rs.next()){    
                if(idsimp.getText().equals(rs.getString("id"))){
                    
                    if(rs.getString("status").equals("LUNAS")){
                        JOptionPane.showMessageDialog(null, "Simpanan wajib ini sudah lunas");
                    }else if(rs.getString("status").equals("BELUM LUNAS")){
                        String sql1 = "UPDATE simpan SET dibayar = dibayar+"+bayar.getText()+", sisa = '"+sisa.getText()+"', tanggal = '"+tgl.getText()+"', status = '"+stat.getSelectedItem()+"' WHERE id = '"+idsimp.getText()+"'";
                        pst.executeUpdate(sql1);
                        JOptionPane.showMessageDialog(null, "Proses pembayaran berhasil");
                        saldoData();
                    }
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
    }
    
    //menyimpan data saldo ke database
    public void saldoData(){
        try{
            String sql = "SELECT id FROM saldo WHERE id = '"+idsal.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            //jika id sudah ada
            if(rs.next()){    
                if(idsal.getText().equals(rs.getString("id"))){
                    String sql1 ="UPDATE saldo SET saldowajib = saldowajib+"+bayar.getText()+", saldototal = saldopokok + saldowajib + saldosukarela WHERE id = '"+idsal.getText()+"'";
                    pst.executeUpdate(sql1);
                    JOptionPane.showMessageDialog(null, "Telah masuk ke saldo");
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
    }
    
    //membersihkan form
    private void bersihkan(){
        idsimp.setText("");
        idang.setText("");
        idsal.setText("");
        namang.setText("");
        carinama.setText("cari...");
        jumlah.setText("0");
        bayar.setText("0");
        sisa.setText("0");
        realtime();
        stat.setSelectedItem("BElUM LUNAS");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        idsimp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        idang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        idsal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        carinama = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcari = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jumlah = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        bayar = new javax.swing.JTextField();
        sisa = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tgl = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        stat = new javax.swing.JComboBox();
        bayarbutton = new javax.swing.JButton();
        batalbutton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSimpananWajib = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        bulan = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        tahun = new javax.swing.JTextField();
        back = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jatuhtempo = new javax.swing.JTextField();
        akses = new javax.swing.JLabel();
        namang = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("Transaksi Wajib");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel2.setText("ID SIMPANAN");

        jLabel4.setText("ID ANGGOTA");

        idang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idangActionPerformed(evt);
            }
        });

        jLabel13.setText("ID SALDO");

        jLabel3.setText("NAMA ANGGOTA");

        carinama.setText("cari...");
        carinama.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                carinamaMouseClicked(evt);
            }
        });
        carinama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                carinamaKeyReleased(evt);
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
        jScrollPane2.setViewportView(tblcari);

        jLabel5.setText("JUMLAH");

        jumlah.setForeground(new java.awt.Color(0, 204, 0));
        jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahKeyReleased(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 204, 0));
        jLabel8.setText("Rp");

        jLabel6.setText("BAYAR");

        jLabel9.setForeground(new java.awt.Color(0, 204, 0));
        jLabel9.setText("Rp");

        bayar.setForeground(new java.awt.Color(0, 204, 0));
        bayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bayarMouseClicked(evt);
            }
        });
        bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bayarKeyReleased(evt);
            }
        });

        sisa.setForeground(new java.awt.Color(0, 204, 0));

        jLabel10.setForeground(new java.awt.Color(0, 204, 0));
        jLabel10.setText("Rp");

        jLabel7.setText("SISA");

        jLabel12.setText("TANGGAL");

        jLabel11.setText("STATUS");

        stat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BELUM LUNAS", "LUNAS" }));

        bayarbutton.setText("BAYAR");
        bayarbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarbuttonActionPerformed(evt);
            }
        });

        batalbutton.setText("BATAL");
        batalbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalbuttonActionPerformed(evt);
            }
        });

        tabelSimpananWajib.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelSimpananWajib);

        jLabel14.setText("SIMPANAN BULAN");

        bulan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bulanActionPerformed(evt);
            }
        });

        jLabel15.setText("SIMPANAN TAHUN");

        tahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tahunActionPerformed(evt);
            }
        });
        tahun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tahunKeyReleased(evt);
            }
        });

        back.setText("Kembali");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jLabel16.setText("JATUH TEMPO");

        akses.setText("YOU ARE...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(akses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(back))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(40, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(stat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(carinama)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(bulan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(idsimp)
                                    .addComponent(idang)
                                    .addComponent(tahun)
                                    .addComponent(jatuhtempo)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sisa, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(idsal)
                                    .addComponent(namang)
                                    .addComponent(tgl, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bayarbutton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batalbutton)))
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idsimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(idsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(namang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(carinama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jatuhtempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel5)))
                            .addComponent(jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel7))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(stat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bayarbutton)
                            .addComponent(batalbutton)))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(akses))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void carinamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carinamaMouseClicked
        carinama.setText("");
    }//GEN-LAST:event_carinamaMouseClicked

    private void carinamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyReleased
        load_table_cariang();
    }//GEN-LAST:event_carinamaKeyReleased

    private void tblcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcariMouseClicked

        // menampilkan data dari tabel ke form
        int baris = tblcari.rowAtPoint(evt.getPoint());
        String id = tblcari.getValueAt(baris, 0).toString();
        String nama = tblcari.getValueAt(baris,1).toString();
        idang.setText(id);
        namang.setText(nama);
        carinama.setText("");
        tampilkanIDSfromIDA();
        tampilkanIDSALfromIDA();
        tampilkanDataSimpanFromId();
        hitungan();
        status();
        jikalunas();
        jikaidkosong();
        load_table_cariang();
    }//GEN-LAST:event_tblcariMouseClicked

    private void bayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bayarMouseClicked
        if(!(jumlah.getText().equals("0"))){
            bayar.setText("");
            hitungan();
        }
    }//GEN-LAST:event_bayarMouseClicked

    private void bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyReleased
        hitungan();
        status();
    }//GEN-LAST:event_bayarKeyReleased

    private void bayarbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarbuttonActionPerformed
        simpanData();
        bersihkan();
    }//GEN-LAST:event_bayarbuttonActionPerformed

    private void batalbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalbuttonActionPerformed
        bersihkan();
    }//GEN-LAST:event_batalbuttonActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        menuSimpananFrame msf = new menuSimpananFrame();
        msf.setId(akses.getText());
        msf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bulanActionPerformed
        bulantahun();
        tampilkanIDSfromIDA();
    }//GEN-LAST:event_bulanActionPerformed

    private void tahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tahunActionPerformed
        tampilkanIDSfromIDA();
    }//GEN-LAST:event_tahunActionPerformed

    private void tahunKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tahunKeyReleased
        tampilkanIDSfromIDA();
    }//GEN-LAST:event_tahunKeyReleased

    private void idangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idangActionPerformed

    private void jumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahKeyReleased
        hitungan();
        status();
    }//GEN-LAST:event_jumlahKeyReleased

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
            java.util.logging.Logger.getLogger(transaksiWajib.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksiWajib.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksiWajib.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksiWajib.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksiWajib().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel akses;
    private javax.swing.JButton back;
    private javax.swing.JButton batalbutton;
    private javax.swing.JTextField bayar;
    private javax.swing.JButton bayarbutton;
    private javax.swing.JComboBox bulan;
    private javax.swing.JTextField carinama;
    private javax.swing.JTextField idang;
    private javax.swing.JTextField idsal;
    private javax.swing.JTextField idsimp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jatuhtempo;
    private javax.swing.JTextField jumlah;
    private javax.swing.JTextField namang;
    private javax.swing.JTextField sisa;
    private javax.swing.JComboBox stat;
    private javax.swing.JTable tabelSimpananWajib;
    private javax.swing.JTextField tahun;
    private javax.swing.JTable tblcari;
    private javax.swing.JTextField tgl;
    // End of variables declaration//GEN-END:variables
}
