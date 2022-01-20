/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sun.net.www.MimeTable;
import view.menuSimpananFrame;

/**
 *
 * @author RPL3-09
 */
public class transaksiSukarela extends javax.swing.JFrame {

    /**
     * Creates new form transaksiSukarela
     */
    public transaksiSukarela() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //settingan awal
        idsimp.setEditable(false);
        idang.setEditable(false);
        idsal.setEditable(false);
        jumlah.setEditable(false);
        sisa.setEditable(false);
        jumlah.setText("0");
        bayar.setText("0");
        sisa.setText("0");
        
        //tampil tampil
        realtime();
        tampilkanIDSfromIDA();
        tampilkanIDSALfromIDA();
        tampilkanDataFromId();
        tampilkanDataFromNama();
        load_table();
        load_table_cariang();
        jikalunas();
    }
    
    public void setAkses(String aks){
        akses.setText(aks);
    }
    
    //menentukan lunas tidak nya suatu simpanan
    private void status(){
        if(jumlah.getText().equals("0")){
            cmbstat.setSelectedItem("LUNAS");
        }else if(sisa.getText().equals("0")){
            cmbstat.setSelectedItem("LUNAS");
        }else{
            cmbstat.setSelectedItem("BELUM LUNAS");
        }
    }
    
    //settingan jika sudah lunas
    private void jikalunas(){
        if(jumlah.getText().equals("0")){
            bayar.setEditable(false);
        }else{
            bayar.setEditable(true);
        }
    }
    
    //menghitung bayaran
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
        String tgl = simpledate.format(tglrealtime);
        tanggal.setText(tgl);
    }
    
    private void jikaidkosong(){
        if(idsimp.getText().equals("")){
            bayarbutton.setEnabled(false);
        }else{
            bayarbutton.setEnabled(true);
        }
    }
    
     //menampilkan data dari databse, tabel simpanan
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
        
        //menampilkan data database ke dalam tabel
        try{
            int no=1;
            String sql = "select * from simpan where jenis = 'SUKARELA'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
                while(res.next()){
                    String sql1 = "select * from anggota where id = '"+res.getString("idanggota")+"'";
                    java.sql.Connection conn1=(Connection)config.configDB();
                    java.sql.Statement stm1=conn1.createStatement();
                    java.sql.ResultSet res1=stm1.executeQuery(sql1);
                    while(res1.next()){
                        model.addRow(new Object[]{res.getString(1),res.getString(2),res1.getString("nama"),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)});
                    }
                }
            tabelSimpananSukarela.setModel(model);
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
            tabelcari.setModel(model1);
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
    
    public void tampilkanIDSfromIDA(){
            try{
                String sql = "SELECT id FROM simpan WHERE idanggota='"+idang.getText()+"' AND jenis = 'SUKARELA'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                idsimp.setText((String)ob[0]);
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
                String sql = "SELECT sisa,status FROM simpan WHERE id='"+idsimp.getText()+"' AND jenis = 'SUKARELA'";
                java.sql.Connection conn=(Connection)config.configDB();
                java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                java.sql.Statement st=conn.createStatement();
                java.sql.ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                ob[1] = rs.getString(2);
                jumlah.setText((String)ob[0]);
                cmbstat.setSelectedItem((String)ob[1]);
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
                        JOptionPane.showMessageDialog(null, "Simpanan sukarela ini sudah lunas");
                        bersihkan();
                    }else if(rs.getString("status").equals("BELUM LUNAS")){
                        String sql1 = "UPDATE simpan SET dibayar = dibayar+"+bayar.getText()+", sisa = '"+sisa.getText()+"', tanggal = '"+tanggal.getText()+"', status = '"+cmbstat.getSelectedItem()+"' WHERE id = '"+idsimp.getText()+"'";
                        pst.executeUpdate(sql1);
                        JOptionPane.showMessageDialog(null, "Proses pembayaran berhasil");
                        saldoData();
                        bersihkan();
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
                    String sql1 ="UPDATE saldo SET saldosukarela = saldosukarela+"+bayar.getText()+", saldototal = saldopokok + saldowajib + saldosukarela WHERE id = '"+idsal.getText()+"'";
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
        cmbstat.setSelectedItem("BElUM LUNAS");
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        idsimp = new javax.swing.JTextField();
        idang = new javax.swing.JTextField();
        idsal = new javax.swing.JTextField();
        carinama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelcari = new javax.swing.JTable();
        jumlah = new javax.swing.JTextField();
        bayar = new javax.swing.JTextField();
        sisa = new javax.swing.JTextField();
        tanggal = new javax.swing.JTextField();
        cmbstat = new javax.swing.JComboBox<String>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelSimpananSukarela = new javax.swing.JTable();
        back = new javax.swing.JButton();
        bayarbutton = new javax.swing.JButton();
        batalbutton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        akses = new javax.swing.JLabel();
        namang = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("Transaksi Sukarela");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addGap(13, 13, 13))
        );

        jLabel2.setText("ID SIMPANAN");

        jLabel3.setText("ID SALDO");

        jLabel4.setText("ID ANGGOTA");

        jLabel5.setText("NAMA ANGGOTA");

        jLabel6.setText("JUMLAH");

        jLabel7.setText("BAYAR");

        jLabel8.setText("SISA");

        jLabel9.setText("TANGGAL");

        jLabel10.setText("STATUS");

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
        });

        tabelcari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "id", "nama"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelcari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelcariMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelcari);

        jumlah.setForeground(new java.awt.Color(0, 204, 0));

        bayar.setForeground(new java.awt.Color(0, 204, 0));
        bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bayarKeyReleased(evt);
            }
        });

        sisa.setForeground(new java.awt.Color(0, 204, 0));

        cmbstat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BELUM LUNAS", "LUNAS" }));

        tabelSimpananSukarela.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelSimpananSukarela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelSimpananSukarelaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelSimpananSukarela);

        back.setText("Kembali");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        bayarbutton.setText("BAYAR");
        bayarbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarbuttonActionPerformed(evt);
            }
        });

        batalbutton.setText("BATAL");

        jLabel11.setForeground(new java.awt.Color(0, 204, 0));
        jLabel11.setText("Rp");

        jLabel12.setForeground(new java.awt.Color(0, 204, 0));
        jLabel12.setText("Rp");

        jLabel13.setForeground(new java.awt.Color(0, 204, 0));
        jLabel13.setText("Rp");

        akses.setText("YOU ARE...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(akses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(back))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(carinama)
                                    .addComponent(idsimp)
                                    .addComponent(idsal)
                                    .addComponent(idang)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(tanggal)
                                    .addComponent(cmbstat, 0, 182, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jumlah, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                            .addComponent(bayar)
                                            .addComponent(sisa)))
                                    .addComponent(namang, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(20, 20, 20))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bayarbutton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batalbutton)
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(idsimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(idsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(namang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(carinama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cmbstat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(batalbutton)
                            .addComponent(bayarbutton)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(akses))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void carinamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyReleased
        load_table_cariang();
    }//GEN-LAST:event_carinamaKeyReleased

    private void carinamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carinamaMouseClicked
        carinama.setText("");
    }//GEN-LAST:event_carinamaMouseClicked

    private void tabelSimpananSukarelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelSimpananSukarelaMouseClicked
        
        // menampilkan data dari tabel ke form
        int baris = tabelSimpananSukarela.rowAtPoint(evt.getPoint());
        String id = tabelSimpananSukarela.getValueAt(baris, 0).toString();
        String idanggota = tabelSimpananSukarela.getValueAt(baris, 1).toString();
        String nama = tabelSimpananSukarela.getValueAt(baris,2).toString();
        
        carinama.setEditable(false);
        idsimp.setText(id);
        idang.setText(idanggota);
        namang.setText(nama);
        carinama.setText("");
        load_table_cariang();
        tampilkanDataSimpanFromId();
        tampilkanIDSALfromIDA();
        hitungan();
        jikalunas();
    }//GEN-LAST:event_tabelSimpananSukarelaMouseClicked

    private void tabelcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelcariMouseClicked
        // menampilkan data dari tabel ke form
        int baris = tabelcari.rowAtPoint(evt.getPoint());
        String id = tabelcari.getValueAt(baris, 0).toString();
        String nama = tabelcari.getValueAt(baris,1).toString();
        idang.setText(id);
        namang.setText(nama);
        tampilkanIDSfromIDA();
        tampilkanIDSALfromIDA();
        tampilkanDataSimpanFromId();
        hitungan();
        status();
        jikalunas();
        jikaidkosong();
        carinama.setText("");
        load_table_cariang();
    }//GEN-LAST:event_tabelcariMouseClicked

    private void bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarKeyReleased
        hitungan();
        status();
    }//GEN-LAST:event_bayarKeyReleased

    private void bayarbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarbuttonActionPerformed
        simpanData();
    }//GEN-LAST:event_bayarbuttonActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        menuSimpananFrame msf = new menuSimpananFrame();
        msf.setId(akses.getText());
        msf.setVisible(true);
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
            java.util.logging.Logger.getLogger(transaksiSukarela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksiSukarela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksiSukarela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksiSukarela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksiSukarela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel akses;
    private javax.swing.JButton back;
    private javax.swing.JButton batalbutton;
    private javax.swing.JTextField bayar;
    private javax.swing.JButton bayarbutton;
    private javax.swing.JTextField carinama;
    private javax.swing.JComboBox<String> cmbstat;
    private javax.swing.JTextField idang;
    private javax.swing.JTextField idsal;
    private javax.swing.JTextField idsimp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JTextField jumlah;
    private javax.swing.JTextField namang;
    private javax.swing.JTextField sisa;
    private javax.swing.JTable tabelSimpananSukarela;
    private javax.swing.JTable tabelcari;
    private javax.swing.JTextField tanggal;
    // End of variables declaration//GEN-END:variables
}
