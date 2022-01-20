/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import view.menuSimpananFrame;

/**
 *
 * @author rizky
 */
public class pinjam extends javax.swing.JFrame {
    
    boolean pinjamlunas,hapuslunas,formkosong;
    
    /**
     * Creates new form pinjam
     */
    public pinjam() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //set awal
        labelidpinjam.setVisible(false);
        idpinj.setVisible(false);
        idang.setEditable(false);
        namang.setEditable(false);
        tglpinjam.setEditable(false);
        tglkembali.setEditable(false);
        autokode();
        realtime();
        bulandepan();
        jikajumlahkosong();
        load_table();
    }
    
    public void setId(String ida){
        id.setText(ida);
    }
    
    //menampilkan isi tabel
    private void load_table(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("id anggota");
        model.addColumn("nama");
        model.addColumn("jumlah");
        model.addColumn("sisa");
        model.addColumn("jasa");
        model.addColumn("angsuran");
        model.addColumn("tanggal pinjaman");
        model.addColumn("jatuh tempo");
        model.addColumn("status");
        
        //menampilkan database, tabel pinjam ke dalam jtable
        try{
            String sql = "select * from pinjam";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                    
                String sql1 = "select * from anggota where id = '"+res.getString("idanggota")+"'";
                java.sql.Statement stm1=conn.createStatement();
                java.sql.ResultSet res1=stm1.executeQuery(sql1);
                    
                while(res1.next()){
                    model.addRow(new Object[]{res.getString(1),res.getString(2),res1.getString("nama"),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)});
                }
            }
            tblpinjaman.setModel(model);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
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
            String sql = "select * from anggota where nama like '%"+carinama.getText()+"%' and keterangan = 'AKTIF'";
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
    
    //kode otomatis id pinjam
    private void autokode(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM pinjam";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    idpinj.setText("PJ001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idpinj.setText("PJ" + nomor);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    //settingan jika jumlah kosong
    private void jikajumlahkosong(){
        if(jumlahpinj.getText().equals("") || jumlahpinj.getText().equals("0")){
            bulanangs.setEditable(false);
            persenjasa.setEditable(false);
        }else{
            bulanangs.setEditable(true);
            persenjasa.setEditable(true);
        }
    }
    
    //menampilkan tanggal secara realtime/saat ini
    private void realtime(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        String tanggal = simpledate.format(tglrealtime);
        tglpinjam.setText(tanggal);
    }
    
    //menampilkan tanggal yang sama di bulan berikutnya
    private void bulandepan(){
        
        //set realtime
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        
        //set bulan
        SimpleDateFormat simpledate1 = new SimpleDateFormat("MMMMMMMMM");
        SimpleDateFormat simpledate2 = new SimpleDateFormat("MM");
        int bulan = Integer.parseInt(simpledate2.format(tglrealtime))-1;
        if(bulanangs.getText().equals("")){
            tglkembali.setText("");
        }else{
            int angs = Integer.parseInt(bulanangs.getText());
            int plusbulan = bulan+angs;
            
            //set tanggal
            SimpleDateFormat simpledate3 = new SimpleDateFormat("dd", Locale.getDefault());
            int tanggal2 = Integer.parseInt(simpledate3.format(tglrealtime));
        
            //set tahun
            SimpleDateFormat simpledate4 = new SimpleDateFormat("YYYY", Locale.getDefault());
            int tahun = Integer.parseInt(simpledate4.format(tglrealtime));        
            int tahun2 = tahun - 1900;
        
            //set date
            java.util.Date tglrealtime2 = new java.util.Date(tahun2 , plusbulan, tanggal2);
        
            //complete here
            String tanggal3 = simpledate.format(tglrealtime2);
            tglkembali.setText(tanggal3);
        }
    }
    
    public void batasPinjam(){
        try{
            String sql = "SELECT * FROM saldo WHERE idanggota = '"+idang.getText()+"'";
            java.sql.Connection conn = config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            
            if(res.next()){
                if(!(jumlahpinj.getText().equals(""))){
                    int batas = Integer.parseInt(res.getString("saldowajib"));
                    int jumlah = Integer.parseInt(jumlahpinj.getText());
                
                    if(jumlah > batas){
                        jumlahpinj.setText(""+batas);
                        JOptionPane.showMessageDialog(null, "Batas pinjaman = "+res.getString("saldowajib")+"");
                    }
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
     public void batasAngsuran(){
        if(!(bulanangs.getText().equals(""))){
            int angsur = Integer.parseInt(bulanangs.getText());
            
            if(angsur > 12){
                bulanangs.setText("12");
                JOptionPane.showMessageDialog(null, "Batas angsuran = 12 bulan");
            }
        }
    }
    
    //menampilkan isi database, tabel pinjam ke form pinjam
    public void tampilkanAllFromIDP(){
        try{
            String sql = "SELECT * FROM pinjam WHERE id = '"+idpinj.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement st=conn.createStatement();
            java.sql.ResultSet rs=st.executeQuery(sql);
            
            while(rs.next()){
                String sql1 = "SELECT * FROM anggota WHERE id = '"+rs.getString("idanggota")+"'";
                java.sql.Statement st1=conn.createStatement();
                java.sql.ResultSet rs1=st1.executeQuery(sql1);
                
                while(rs1.next()){
                    idang.setText(rs.getString("idanggota"));
                    namang.setText(rs1.getString("nama"));
                    jumlahpinj.setText(rs.getString("jumlah"));
                    persenjasa.setText(rs.getString("jasa"));
                    bulanangs.setText(rs.getString("angsuran"));
                    tglpinjam.setText(rs.getString("tanggalpinjam"));
                    tglkembali.setText(rs.getString("jatuhtempo"));
                    cmbstat.setSelectedItem(rs.getString("status"));
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //jika anggota masih meminjam uang
    public void validasiPinjamLunas(){
        try{
            String sql = "SELECT * FROM pinjam WHERE idanggota = '"+idang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()){
                if(idang.getText().equals(rs.getString("idanggota"))){
                    String sql1 = "SELECT * FROM pinjam WHERE idanggota = '"+idang.getText()+"' AND status = 'LUNAS'";
                    java.sql.Statement stmt1 = conn.createStatement();
                    java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
                    
                    if(rs1.next()){
                        if(idang.getText().equals(rs1.getString("idanggota"))){
                            pinjamlunas = true;
                        }
                    }else{
                        pinjamlunas = false;
                    }
                }
            }else{
                pinjamlunas = true;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //jika pinjaman belum lunas
    public void validasiHapusLunas(){
        try{
            String sql = "SELECT * FROM pinjam WHERE id = '"+idpinj.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next()){
                if(rs.getString("status").equals("LUNAS")){
                    hapuslunas = true;
                }
            }else{
                hapuslunas = false;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //jika form kosong
    public void jikaformkosong(){
        String idpinjam = idpinj.getText();
        String idanggota = idang.getText();
        String jumlahpinjaman = jumlahpinj.getText();
        String angsuran =  bulanangs.getText();
        String jasa = persenjasa.getText();
        String jatuhtempo = tglkembali.getText();
        
        if(idpinjam.equals("") || idanggota.equals("") || jumlahpinjaman.equals("") || angsuran.equals("") || jasa.equals("") || jatuhtempo.equals("")){
            formkosong = true;
        }else{
            formkosong = false;
        }
    }
    
    //insert ke database tabel pinjam
    public void pinjamData(){
        try{
            String sql ="INSERT INTO pinjam VALUES('"+idpinj.getText()+"','"+idang.getText()+"','"+jumlahpinj.getText()+"','"+jumlahpinj.getText()+"','"+persenjasa.getText()+"','"+bulanangs.getText()+"','"+tglpinjam.getText()+"','"+tglkembali.getText()+"','"+cmbstat.getSelectedItem().toString()+"')";
            java.sql.Connection conn = (Connection)config.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Proses Pinjaman Berhasil");
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table();
        bersihkan();
    }
    
    //menghapus data di tabel pinjam
    public void hapusData(){
        
        int yakin = JOptionPane.showConfirmDialog(null, "Yakin akan di hapus ?", "Hapus Data Pinjaman", JOptionPane.YES_NO_OPTION);
        
        if(yakin == JOptionPane.YES_OPTION){
            try{
                String sql ="DELETE FROM pinjam WHERE id = '"+idpinj.getText()+"'";
                String sql1 ="DELETE FROM angsuran WHERE idpinjam = '"+idpinj.getText()+"'";
                java.sql.Connection conn = (Connection)config.configDB();
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                java.sql.PreparedStatement pst1 = conn.prepareStatement(sql1);
                pst.execute();
                pst1.execute();
                JOptionPane.showMessageDialog(null, "Pinjaman berhasil di hapus");
            }catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            load_table();
            bersihkan();
        }
    }
    
    //membersihkan form
    public void bersihkan(){
        autokode();
        realtime();
        idang.setText("");
        namang.setText("");
        jumlahpinj.setText("");
        bulanangs.setText("");
        persenjasa.setText("");
        cmbstat.setSelectedItem("BELUM LUNAS");
        tglkembali.setText("");
        jikajumlahkosong();
    }
    
    //fungsi mencetak struk di jasper
    public void cetakStruk(){
        //set lokal
        Locale lokal = new Locale("id","ID");
        Locale.setDefault(lokal);
        
        try{
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("strukPinjaman.jasper"), null, config.configDB());
            JasperViewer.viewReport(jp, false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
        //set lokal
        Locale locale = new Locale("us","US");
        Locale.setDefault(locale);
    }
    
    //hanya dapat mengetikkan angka
    public void hanyaangka(KeyEvent a){
        if(Character.isAlphabetic(a.getKeyChar())){
            a.consume();
            JOptionPane.showMessageDialog(null,"hanya angka",null,JOptionPane.WARNING_MESSAGE);
        }
    }
    
    //hanya dapat mengetikkan huruf
    public void hanyahuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
            JOptionPane.showMessageDialog(null,"hanya huruf",null,JOptionPane.WARNING_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelidpinjam = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        idpinj = new javax.swing.JTextField();
        idang = new javax.swing.JTextField();
        carinama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblcari = new javax.swing.JTable();
        jumlahpinj = new javax.swing.JTextField();
        tglpinjam = new javax.swing.JTextField();
        tglkembali = new javax.swing.JTextField();
        pinjam = new javax.swing.JButton();
        batal = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblpinjaman = new javax.swing.JTable();
        bulanangs = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbstat = new javax.swing.JComboBox();
        back = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        persenjasa = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        namang = new javax.swing.JTextField();
        hapus = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("Form Pinjaman");

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

        labelidpinjam.setText("ID PINJAMAN");

        jLabel3.setText("ID ANGGOTA");

        jLabel4.setText("NAMA ANGGOTA");

        jLabel5.setText("JUMLAH PINJAMAN");

        jLabel6.setText("TANGGAL PEMINJAMAN");

        jLabel7.setText("JATUH TEMPO");

        jLabel9.setText("ANGSURAN");

        carinama.setForeground(new java.awt.Color(204, 204, 204));
        carinama.setText("nama");
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
        jScrollPane1.setViewportView(tblcari);

        jumlahpinj.setForeground(new java.awt.Color(0, 204, 0));
        jumlahpinj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahpinjKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jumlahpinjKeyTyped(evt);
            }
        });

        pinjam.setText("PINJAM");
        pinjam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjamActionPerformed(evt);
            }
        });

        batal.setText("BATAL");
        batal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalActionPerformed(evt);
            }
        });

        tblpinjaman.setModel(new javax.swing.table.DefaultTableModel(
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
        tblpinjaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblpinjamanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblpinjaman);

        bulanangs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bulanangsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bulanangsKeyTyped(evt);
            }
        });

        jLabel11.setText("Bulan");

        jLabel12.setForeground(new java.awt.Color(0, 204, 0));
        jLabel12.setText("Rp");

        jLabel8.setText("STATUS");

        cmbstat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BELUM LUNAS", "LUNAS" }));

        back.setText("Kembali");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jLabel13.setText("JASA");

        persenjasa.setForeground(new java.awt.Color(204, 0, 0));
        persenjasa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                persenjasaKeyTyped(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(204, 0, 0));
        jLabel14.setText("%");

        id.setText("YOU ARE...");

        hapus.setText("HAPUS");
        hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        jLabel2.setText("Cari");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelidpinjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tglkembali)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(tglpinjam)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jumlahpinj))
                                    .addComponent(cmbstat, 0, 182, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bulanangs)
                                            .addComponent(persenjasa))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(idpinj)
                                    .addComponent(idang)
                                    .addComponent(namang)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(carinama))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pinjam)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batal)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(back)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelidpinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idpinj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(namang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(carinama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tglpinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jumlahpinj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(bulanangs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(persenjasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbstat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tglkembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pinjam, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(batal)
                                .addComponent(hapus))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(id))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void carinamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carinamaMouseClicked
        carinama.setText("");
        carinama.setForeground(Color.black);
    }//GEN-LAST:event_carinamaMouseClicked

    private void batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalActionPerformed
        bersihkan();
    }//GEN-LAST:event_batalActionPerformed

    private void carinamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyReleased
        load_table_cariang();
    }//GEN-LAST:event_carinamaKeyReleased

    private void tblcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcariMouseClicked
        // menampilkan data dari tabel ke form
        int baris = tblcari.rowAtPoint(evt.getPoint());
        String id = tblcari.getValueAt(baris,0).toString();
        String nama = tblcari.getValueAt(baris,1).toString();
        idang.setText(id);
        namang.setText(nama);
        carinama.setText("");
        jumlahpinj.setText("");
    }//GEN-LAST:event_tblcariMouseClicked

    private void pinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjamActionPerformed
        validasiPinjamLunas();
        jikaformkosong();
        
        if(formkosong == false){
            if(pinjamlunas == true){
                pinjamData();
            }else{
                JOptionPane.showMessageDialog(null, "Anggota ini masih meminjam uang");
                bersihkan();
            }  
        }else{
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }
    }//GEN-LAST:event_pinjamActionPerformed

    private void jumlahpinjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahpinjKeyReleased
        jikajumlahkosong();
        batasPinjam();
    }//GEN-LAST:event_jumlahpinjKeyReleased

    private void bulanangsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bulanangsKeyReleased
        bulandepan();
        batasAngsuran();
    }//GEN-LAST:event_bulanangsKeyReleased

    private void tblpinjamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblpinjamanMouseClicked
        int baris = tblpinjaman.rowAtPoint(evt.getPoint());
        String id = tblpinjaman.getValueAt(baris, 0).toString();
        idpinj.setText(id);
        tampilkanAllFromIDP();
        hapus.setEnabled(true);
    }//GEN-LAST:event_tblpinjamanMouseClicked

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(id.getText());
        bf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void jumlahpinjKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahpinjKeyTyped
        hanyaangka(evt);
    }//GEN-LAST:event_jumlahpinjKeyTyped

    private void bulanangsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bulanangsKeyTyped
        hanyaangka(evt);
    }//GEN-LAST:event_bulanangsKeyTyped

    private void persenjasaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_persenjasaKeyTyped
        hanyaangka(evt);
    }//GEN-LAST:event_persenjasaKeyTyped

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        validasiHapusLunas();
        
        if(hapuslunas == true){
            hapusData();
        }else{
            JOptionPane.showMessageDialog(null, "Pinjaman tidak dapat di hapus");
            bersihkan();
        }
    }//GEN-LAST:event_hapusActionPerformed

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
            java.util.logging.Logger.getLogger(pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pinjam().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton batal;
    private javax.swing.JTextField bulanangs;
    private javax.swing.JTextField carinama;
    private javax.swing.JComboBox cmbstat;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idang;
    private javax.swing.JTextField idpinj;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTextField jumlahpinj;
    private javax.swing.JLabel labelidpinjam;
    private javax.swing.JTextField namang;
    private javax.swing.JTextField persenjasa;
    private javax.swing.JButton pinjam;
    private javax.swing.JTable tblcari;
    private javax.swing.JTable tblpinjaman;
    private javax.swing.JTextField tglkembali;
    private javax.swing.JTextField tglpinjam;
    // End of variables declaration//GEN-END:variables
}
