/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.awt.Color;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author rizky
 */
public class angsuran extends javax.swing.JFrame {

    /**
     * Creates new form bayarPinjam
     */
    public angsuran() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //settingan awal
        idangsuran.setEditable(false);
        idpinj.setEditable(false);
        idang.setEditable(false);
        namang.setEditable(false);
        tglpinjam.setEditable(false);
        tglkembali.setEditable(false);
        jumlahpinj.setEditable(false);
        bulanangs.setEditable(false);
        jumlahangs.setEditable(false);
        rpjasa.setEditable(false);
        jumbay.setEditable(false);
        rpkembali.setEditable(false);
        load_table_ang();
    }
    
    public void setId(String ida){
        id.setText(ida);
    }
    
    //kode otomatis id angsuran
    private void autokode(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM angsuran";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    idangsuran.setText("ANG001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idangsuran.setText("ANG" + nomor);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    //menampilkan isi tabel
    private void load_table_ang(){
        
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
        model.addColumn("tanggal pengembalian");
        model.addColumn("status");
        
        //menampilkan database, tabel pinjam ke dalam jtable
        try{
            String sql = "select * from pinjam where status = 'BELUM LUNAS'";
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
        model1.addColumn("id anggota");
        model1.addColumn("nama");
        model1.addColumn("jumlah");
        model1.addColumn("sisa");
        model1.addColumn("jasa");
        model1.addColumn("angsuran");
        model1.addColumn("tanggal pinjaman");
        model1.addColumn("tanggal pengembalian");
        model1.addColumn("status");
        
        //menampilkan database, tabel anggota, kolom id dan nama ke dalam jtable
        try{
            String sql = "select * from pinjam where id like '%"+cariidpinj.getText()+"%' and status = 'BELUM LUNAS'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                String sql1 = "select * from anggota where id = '"+res.getString("idanggota")+"'";
                java.sql.Statement stm1=conn.createStatement();
                java.sql.ResultSet res1=stm1.executeQuery(sql1);
                        
                while(res1.next()){
                    model1.addRow(new Object[]{res.getString(1),res.getString(2),res1.getString("nama"),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)});
                }
            }    
            tblpinjaman.setModel(model1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    //menampilkan tanggal secara realtime/saat ini
    private void realtime(){
        java.util.Date tglrealtime = new java.util.Date();
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        String tanggal = simpledate.format(tglrealtime);
        tglkembali.setText(tanggal);
    }
    
    //insert ke database tabel angsuran dan update database tabel pinjaman
    public void bayarPinjamData(){
        //fungsi
        try{
            String sql = "SELECT * FROM pinjam WHERE id = '"+idpinj.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                int jp,sis,sis2,dibayar,dibayar2,rpangs,blnangs,rpjas,bulankeangs,jumlahbayar,jumlahbayar2;
                double persenjas;
                
                jp = Integer.parseInt(rs.getString("jumlah"));
                sis = Integer.parseInt(jumlahpinj.getText());
                blnangs = Integer.parseInt(rs.getString("angsuran"));
                rpangs = jp/blnangs;
                persenjas = Double.parseDouble(rs.getString("jasa")) / 100;
                rpjas = (int) (sis*persenjas);
                jumlahbayar = (int) (rpangs + rpjas);
                dibayar = jp-sis;
                bulankeangs = (dibayar/rpangs)+1;
                jumlahbayar2 = Integer.parseInt(rpbayar.getText()) - Integer.parseInt(rpkembali.getText());
                dibayar2 = jumlahbayar2 - rpjas;
                sis2 = sis - dibayar2;
                
                int rpbay,jumlahbay;
                rpbay = Integer.parseInt(rpbayar.getText());
                jumlahbay = Integer.parseInt(jumbay.getText());
                
                if(rpbay >= jumlahbay){
                    
                    //mengupdate data pinjaman
                    String sql1 = "UPDATE pinjam SET sisa = '"+sis2+"', status = '"+cmbstat.getSelectedItem().toString()+"' WHERE id = '"+idpinj.getText()+"'";
                    java.sql.PreparedStatement pst=conn.prepareStatement(sql1);
                    pst.execute();
                    
                    //memasukkan data angsuran
                    String sql2 = "INSERT INTO angsuran VALUES('"+idangsuran.getText()+"','"+idpinj.getText()+"','"+tglkembali.getText()+"','"+jumlahangs.getText()+"','"+rpjasa.getText()+"','"+jumbay.getText()+"','"+bulanangs.getText()+"')";
                    java.sql.PreparedStatement pst1=conn.prepareStatement(sql2);
                    pst1.execute();
                    
                    JOptionPane.showMessageDialog(null, "Transaksi berhasil");
                    
                    bersihkan();
                    cetakStruk();
                }else{
                    JOptionPane.showMessageDialog(null, "Uang kurang");
                }    
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table_ang();
    }
    
    //menampilkan isi database, tabel simpan ke form simpan
    public void tampilkanAllFromIDP(){
        try{
            String sql = "SELECT * FROM pinjam WHERE id = '"+idpinj.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement st=conn.createStatement();
            java.sql.ResultSet rs=st.executeQuery(sql);
            
            while(rs.next()){
                String sql1 = "SELECT * FROM anggota WHERE id = '"+rs.getString("idanggota")+"'";
                java.sql.Statement st1=conn.createStatement();
                java.sql.ResultSet rs1=st1.executeQuery(sql1);
                
                while(rs1.next()){
                    idpinj.setText(rs.getString("id"));
                    idang.setText(rs.getString("idanggota"));
                    namang.setText(rs1.getString("nama"));
                    jumlahpinj.setText(rs.getString("sisa"));
                    tglpinjam.setText(rs.getString("tanggalpinjam"));
                    realtime();
                    cmbstat.setSelectedItem(rs.getString("status"));

                    int jp,sis,dibayar,rpangs,blnangs,rpjas,bulankeangs,jumlahbayar;
                    double persenjas;
                    jp = Integer.parseInt(rs.getString("jumlah"));
                    sis = Integer.parseInt(jumlahpinj.getText());
                    
                    blnangs = Integer.parseInt(rs.getString("angsuran"));
                    rpangs = jp/blnangs;
                    
                    persenjas = Double.parseDouble(rs.getString("jasa")) / 100;
                    rpjas = (int) (sis*persenjas);
                    
                    jumlahbayar = (int) (rpangs + rpjas);
                    dibayar = jp-sis;
                    bulankeangs = (dibayar/rpangs)+1;
                    
                    bulanangs.setText(""+bulankeangs);
                    jumlahangs.setText(""+rpangs);
                    rpjasa.setText(""+rpjas);
                    jumbay.setText(""+jumlahbayar);
                    rpbayar.setText("0");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //menghitung kembalian
    public void hitungankembalian(){
        if(!(rpbayar.getText().equals(""))){
            int jumlahbayar,dibayar,kembalian;
            jumlahbayar = Integer.parseInt(jumbay.getText());
            dibayar = Integer.parseInt(rpbayar.getText());
                if(dibayar >= jumlahbayar){
                    kembalian = dibayar - jumlahbayar;
                    rpkembali.setText(""+kembalian);
                }else{
                    rpkembali.setText("");
                }
        }else{
            rpkembali.setText("");
        }
    }
    
    public void hitungLunas(){
        try{
            String sql = "SELECT * FROM pinjam WHERE id = '"+idpinj.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                
                int jp,sis,sis2,dibayar,dibayar2,rpangs,blnangs,rpjas,bulankeangs,jumlahbayar,jumlahbayar2;
                double persenjas;
                jp = Integer.parseInt(rs.getString("jumlah"));
                sis = Integer.parseInt(jumlahpinj.getText());
                blnangs = Integer.parseInt(rs.getString("angsuran"));
                rpangs = jp/blnangs;
                persenjas = Double.parseDouble(rs.getString("jasa")) / 100;
                rpjas = (int) (sis*persenjas);
                jumlahbayar = (int) (rpangs + rpjas);
                dibayar = jp-sis;
                bulankeangs = (dibayar/rpangs)+1;
                jumlahbayar2 = Integer.parseInt(rpbayar.getText()) - Integer.parseInt(rpkembali.getText());
                dibayar2 = jumlahbayar2 - rpjas;
                sis2 = sis - dibayar2;
                
                if(sis2 == 0){
                    cmbstat.setSelectedItem("LUNAS");
                }else if(sis >= 0){
                    cmbstat.setSelectedItem("BELUM LUNAS");
                }
            }
        }catch(Exception e){
        
        }
    }
    
    public void bersihkan(){
        idangsuran.setText("");
        idpinj.setText("");
        idang.setText("");
        namang.setText("");
        tglpinjam.setText("");
        tglkembali.setText("");
        jumlahpinj.setText("");
        bulanangs.setText("");
        jumlahangs.setText("");
        rpjasa.setText("");
        jumbay.setText("");
        rpbayar.setText("");
        rpkembali.setText("");
        cmbstat.setSelectedItem("BELUM LUNAS");
    }
    
    //fungsi mencetak struk di jasper
    public void cetakStruk(){
        //set lokal
        Locale lokal = new Locale("id","ID");
        Locale.setDefault(lokal);
        
        try{
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("strukAngsuran.jasper"), null, config.configDB());
            JasperViewer.viewReport(jp, false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        
        //set lokal
        Locale locale = new Locale("us","US");
        Locale.setDefault(locale);
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        idpinj = new javax.swing.JTextField();
        idang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tglpinjam = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tglkembali = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        rpjasa = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        rpbayar = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cmbstat = new javax.swing.JComboBox();
        bayarbut = new javax.swing.JButton();
        batalbut = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblpinjaman = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jumlahpinj = new javax.swing.JTextField();
        back = new javax.swing.JButton();
        jumbay = new javax.swing.JTextField();
        bulanangs = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        rpkembali = new javax.swing.JTextField();
        id = new javax.swing.JLabel();
        namang = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jumlahangs = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        lblidangsuran = new javax.swing.JLabel();
        idangsuran = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cariidpinj = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("ANGSURAN");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel2.setText("ID PINJAMAN ");

        jLabel3.setText("ID ANGGOTA ");

        jLabel4.setText("NAMA ANGGOTA ");

        jLabel6.setText("TANGGAL PEMINJAMAN ");

        jLabel9.setText("TANGGAL ANGSURAN");

        tglkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglkembaliActionPerformed(evt);
            }
        });

        jLabel10.setText("JASA ");

        jLabel11.setForeground(new java.awt.Color(0, 204, 0));
        jLabel11.setText("Rp");

        rpjasa.setForeground(new java.awt.Color(0, 204, 0));

        jLabel12.setText("BAYAR ");

        jLabel13.setForeground(new java.awt.Color(0, 204, 0));
        jLabel13.setText("Rp");

        rpbayar.setForeground(new java.awt.Color(0, 204, 0));
        rpbayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rpbayarKeyReleased(evt);
            }
        });

        jLabel14.setText("STATUS ");

        cmbstat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BELUM LUNAS", "LUNAS" }));

        bayarbut.setText("BAYAR");
        bayarbut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bayarbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarbutActionPerformed(evt);
            }
        });

        batalbut.setText("BATAL");
        batalbut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        batalbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalbutActionPerformed(evt);
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

        jLabel16.setText("JUMLAH BAYAR ");

        jLabel17.setText("ANGSURAN");

        jLabel18.setText("Bulan ke");

        jLabel7.setForeground(new java.awt.Color(0, 204, 0));
        jLabel7.setText("Rp");

        jLabel8.setText("SISA PINJAMAN");

        jLabel19.setForeground(new java.awt.Color(0, 204, 0));
        jLabel19.setText("Rp");

        jumlahpinj.setForeground(new java.awt.Color(0, 204, 0));

        back.setText("Kembali");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jumbay.setForeground(new java.awt.Color(0, 204, 0));

        jLabel5.setText("KEMBALIAN ");

        jLabel15.setForeground(new java.awt.Color(0, 204, 0));
        jLabel15.setText("Rp");

        rpkembali.setForeground(new java.awt.Color(0, 204, 0));

        id.setText("YOU ARE...");

        jLabel21.setText("JUMLAH ANGSURAN");

        jumlahangs.setForeground(new java.awt.Color(0, 204, 0));

        jLabel22.setForeground(new java.awt.Color(0, 204, 0));
        jLabel22.setText("Rp");

        lblidangsuran.setText("ID ANGSURAN");

        idangsuran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idangsuranActionPerformed(evt);
            }
        });

        jLabel24.setText("Cari");

        cariidpinj.setForeground(new java.awt.Color(204, 204, 204));
        cariidpinj.setText("id pinjaman");
        cariidpinj.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariidpinjMouseClicked(evt);
            }
        });
        cariidpinj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariidpinjKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(32, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bayarbut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batalbut))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblidangsuran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(idang)
                                    .addComponent(tglpinjam)
                                    .addComponent(tglkembali)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rpjasa))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rpbayar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bulanangs))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jumbay))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rpkembali))
                                    .addComponent(cmbstat, 0, 161, Short.MAX_VALUE)
                                    .addComponent(namang)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel22))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jumlahangs)
                                            .addComponent(jumlahpinj)))
                                    .addComponent(idangsuran, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                    .addComponent(idpinj))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cariidpinj, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(back)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idangsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblidangsuran)
                    .addComponent(jLabel24)
                    .addComponent(cariidpinj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idpinj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(namang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tglpinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tglkembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jumlahpinj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(bulanangs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jumlahangs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(rpjasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel7)
                            .addComponent(jumbay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(rpbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel15)
                            .addComponent(rpkembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(cmbstat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(batalbut)
                            .addComponent(bayarbut)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id)
                    .addComponent(back))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblpinjamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblpinjamanMouseClicked
        // menampilkan data dari tabel ke form
        int baris = tblpinjaman.rowAtPoint(evt.getPoint());
        String pin = tblpinjaman.getValueAt(baris, 0).toString();
        autokode();
        idpinj.setText(pin);
        tampilkanAllFromIDP();
    }//GEN-LAST:event_tblpinjamanMouseClicked

    private void rpbayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rpbayarKeyReleased
        hitungankembalian();
        hitungLunas();
    }//GEN-LAST:event_rpbayarKeyReleased

    private void bayarbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarbutActionPerformed
        if(!(rpkembali.getText().equals(""))){
            int kembalian;
            kembalian = Integer.parseInt(rpkembali.getText());
            if(kembalian >=0){
                bayarPinjamData();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Uang kurang");
        }
    }//GEN-LAST:event_bayarbutActionPerformed

    private void tglkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglkembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglkembaliActionPerformed

    private void idangsuranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idangsuranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idangsuranActionPerformed

    private void batalbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalbutActionPerformed
        bersihkan();
    }//GEN-LAST:event_batalbutActionPerformed

    private void cariidpinjMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariidpinjMouseClicked
        cariidpinj.setText("");
        cariidpinj.setForeground(Color.black);
    }//GEN-LAST:event_cariidpinjMouseClicked

    private void cariidpinjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariidpinjKeyReleased
        
        if(cariidpinj.getText().equals("")){
            load_table_ang();
        }else{
            load_table_cariang();
        }
    }//GEN-LAST:event_cariidpinjKeyReleased

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(id.getText());
        bf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

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
            java.util.logging.Logger.getLogger(angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new angsuran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton batalbut;
    private javax.swing.JButton bayarbut;
    private javax.swing.JTextField bulanangs;
    private javax.swing.JTextField cariidpinj;
    private javax.swing.JComboBox cmbstat;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idang;
    private javax.swing.JTextField idangsuran;
    private javax.swing.JTextField idpinj;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jumbay;
    private javax.swing.JTextField jumlahangs;
    private javax.swing.JTextField jumlahpinj;
    private javax.swing.JLabel lblidangsuran;
    private javax.swing.JTextField namang;
    private javax.swing.JTextField rpbayar;
    private javax.swing.JTextField rpjasa;
    private javax.swing.JTextField rpkembali;
    private javax.swing.JTable tblpinjaman;
    private javax.swing.JTextField tglkembali;
    private javax.swing.JTextField tglpinjam;
    // End of variables declaration//GEN-END:variables
}
