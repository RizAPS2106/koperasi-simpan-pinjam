/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cobaproject;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JMonthChooser;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFormattedTextField;
import view.menuSimpananFrame;
import java.awt.FlowLayout;
import java.text.NumberFormat;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author rizky
 */
public class simpan extends javax.swing.JFrame {
    
    boolean tanggal15,cekaktif,bulansama,bulansama1,minsimpansukarela;
    /**
     * Creates new form simpan
     */
    public simpan() {
        initComponents();
        
        //set fullscreen
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        
        //set awal
        autokode();
        autokodesaldo();
        idsimp.setEditable(false);
        idang.setEditable(false);
        namang.setEditable(false);
        idsimpang.setEditable(false);
        tglsimpan.setDateFormatString("dd MMMMMMMMM YYYY");
        ((JTextField)tglsimpan.getDateEditor().getUiComponent()).setEditable(false);
        labeljatuhtempo.setVisible(false);
        jatuhtempo.setVisible(false);
        cmbjenis.setSelectedItem("POKOK");
        jumlahsimp.setText("0");
        jLabel3.setVisible(false);
        idsimp.setVisible(false);
        jLabel7.setVisible(false);
        idsimpang.setVisible(false);
        realtime();
        jatuhtempo();
        load_table_ang();
        jikaidkosong();
        jikapokokterpilih();
        jikawajibterpilih();
        jikasukarelaterpilih();
    }
    
    public void setAkses(String aks){
        id.setText(aks);
    }
    
    private void jikaidkosong(){
        if(idang.getText().equals("")){
            simpan.setEnabled(false);
        }else{
            simpan.setEnabled(true);
        }
    }
    
    private void jikapokokterpilih(){
        if(cmbjenis.getSelectedItem().equals("POKOK")){
            jumlahsimp.setText("100000");
            if(jumlahsimp.getText().equals("")){
                jumlahsimp.setText("100000");
            }else{
                int jumlah = Integer.parseInt(jumlahsimp.getText());
                    if(jumlah < 100000){
                        jumlahsimp.setText("100000");
                    }
            }
            jumlahsimp.setEditable(true);
        }else{
            jumlahsimp.setEditable(false);
        }
    }
    
    private void jikawajibterpilih(){
        if(cmbjenis.getSelectedItem().equals("WAJIB")){
            jumlahsimp.setText("50000");
            ((JTextField)tglsimpan.getDateEditor().getUiComponent()).setEditable(true);
            if(jumlahsimp.getText().equals("")){
                jumlahsimp.setText("50000");
            }else{
                int jumlah = Integer.parseInt(jumlahsimp.getText());
                    if(jumlah < 50000){
                        jumlahsimp.setText("50000");
                    }
            }
        }
    }
    
    private void jikasukarelaterpilih(){
        if(cmbjenis.getSelectedItem().equals("SUKARELA")){
            jumlahsimp.setEditable(true);
                
            if(jumlahsimp.getText().equals("")){
                jumlahsimp.setText("50000");
            }else{
                int jumlah = Integer.parseInt(jumlahsimp.getText());
                if(jumlah < 50000){
                    minsimpansukarela = false;
                }else{
                    minsimpansukarela = true;
                }
            }
        }else{
            jumlahsimp.setEditable(false);
        }
    }
    
    //menampilkan tanggalsecara realtime
    private void realtime(){
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("MMMMMMMMM", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
        
        //tanggal sekarang
        java.util.Date tglrealtime = new java.util.Date();
        String tglreal = simpledate.format(tglrealtime);
        ((JTextField)tglsimpan.getDateEditor().getUiComponent()).setText(tglreal);
    }
    
    //menampilkan tanggal dan jatuh tempo secara realtime
    private void jatuhtempo(){
        
        SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
        SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat simpledate1 = new SimpleDateFormat("MMMMMMMMM", Locale.getDefault());
        SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
        
        //tanggal jatuh tempo
        String tglsimp = ((JTextField)tglsimpan.getDateEditor().getUiComponent()).getText();
        java.util.Date tanggalsimpan = new java.util.Date(tglsimp);
        String tgljatem = "15";
        
        String bulanjatem = simpledate1.format(tanggalsimpan);
        String tahunjatem = simpledate2.format(tanggalsimpan);
        String tgljatem1 = tgljatem+" "+bulanjatem+" "+tahunjatem;
        
        jatuhtempo.setText(tgljatem1);
    }
    
    //menampilkan isi tabel
    private void load_table_ang(){
        
        //membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("id anggota");
        model.addColumn("jumlah");
        model.addColumn("tanggal");
        model.addColumn("jenis");
        
        //menampilkan database, tabel simpan ke dalam jtable
        try{
            int no=1;
            String sql = "select * from simpan";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
                while(res.next()){
                    model.addRow(new Object[]{res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)});
                }
            tblsimpan.setModel(model);
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
    
    //kode otomatis id simpan
    private void autokode(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM simpan";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    idsimp.setText("S001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idsimp.setText("S" + nomor);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    //kode otomatis id saldo
    private void autokodesaldo(){
        try{
            String sql = "SELECT MAX(RIGHT(id,3)) FROM saldo";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            java.sql.Statement stmt=conn.createStatement();
            java.sql.ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    idsimpang.setText("SAL001");
                }else{
                    rs.last();
                    int autoid = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoid);
                    int noLong = nomor.length();
                    
                        for(int a=0;a<3-noLong;a++){
                            nomor = "0" + nomor;
                        }
                    idsimpang.setText("SAL" + nomor);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal "+e.getMessage());
        }
    }
    
    //mengecek tanggal sudah tanggal 15/belum
    public void cekTanggal(){
        java.util.Date tglrealtime = new Date();
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
    
    public void ubahKeteranganAktif(){
        try {
            String sql ="update anggota set keterangan = 'AKTIF' where id = '"+idang.getText()+"' ";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "anggota sudah aktif");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void ubahKeteranganDaftarTunggu(){
        try {
            String sql ="update anggota set keterangan = 'DAFTAR TUNGGU' where id = '"+idang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "anggota menjadi daftar tunggu");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void ubahKeteranganTidakAktif(){
        try {
            String sql ="update anggota set keterangan = 'TIDAK AKTIF' where id = '"+idang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(this, "anggota menjadi tidak aktif");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //mengecek simpanan wajib sudah ada atau belum pada bulan dan tahun tertentu
    public void validasiBulan(){
        try{
            String sql1 = "SELECT * FROM simpan WHERE idanggota = '"+idang.getText()+"' AND jenis = 'WAJIB' ";
            java.sql.Connection conn1=(Connection)config.configDB();
            java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
            pst1.execute();
            java.sql.Statement stmt1 = conn1.createStatement();
            java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
            
            if(rs1.next()){
                String tglin = ((JTextField)tglsimpan.getDateEditor().getUiComponent()).getText();
                java.util.Date tglinput = new Date(tglin);
                SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
                SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpledate1 = new SimpleDateFormat("MMMMMMMMM", Locale.getDefault());
                SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
                int tgl = Integer.parseInt(simpledate0.format(tglinput));
                String bulan = simpledate1.format(tglinput);
                int tahun = Integer.parseInt(simpledate2.format(tglinput));
                String bulantahun = String.valueOf(bulan+" "+tahun);
                
                String sql2 = "SELECT MID(tanggal,4,14) FROM simpan WHERE idanggota = '"+idang.getText()+"' AND jenis = 'WAJIB' AND MID(tanggal,4,14) = '"+bulantahun+"'";
                java.sql.Statement stmt2 = conn1.createStatement();
                java.sql.ResultSet rs2 = stmt2.executeQuery(sql2);
                
                if(rs2.next()){
                    if(bulantahun.equals(rs2.getString("MID(tanggal,4,14)"))){
                        bulansama = true;
                    }
                }else{
                    bulansama = false;
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //mengecek simpanan wajib sudah ada atau belum pada bulan dan tahun realtime
    public void validasiBulan1(){
        try{
            String sql1 = "SELECT * FROM simpan WHERE idanggota = '"+idang.getText()+"' AND jenis = 'WAJIB' ";
            java.sql.Connection conn1=(Connection)config.configDB();
            java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
            pst1.execute();
            java.sql.Statement stmt1 = conn1.createStatement();
            java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
            
            if(rs1.next()){
                java.util.Date tglrealtime = new java.util.Date();
                String tanggalsimpan = rs1.getString("tanggal");
                java.util.Date tglsimpan = new Date(tanggalsimpan);
                
                SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
                SimpleDateFormat simpledate0 = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat simpledate1 = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat simpledate2 = new SimpleDateFormat("YYYY", Locale.getDefault());
                
                //tgl realtime
                int tgl = Integer.parseInt(simpledate0.format(tglrealtime));
                int bulan = Integer.parseInt(simpledate1.format(tglrealtime));
                int tahun = Integer.parseInt(simpledate2.format(tglrealtime));
                
                //tgl simpan
                int tanggalsimp = Integer.parseInt(simpledate0.format(tglsimpan));
                int bulansimp = Integer.parseInt(simpledate1.format(tglsimpan));
                int tahunsimp = Integer.parseInt(simpledate2.format(tglsimpan));
                
                if(bulan == bulansimp && tahun == tahunsimp){
                    bulansama1 = true;
                }else{
                    bulansama1 = false;
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void simpanData(){
        String idsimpanan, idanggota, idsaldo, jum, bay, sis, tanggalsimpan, tahunsimpan, jattemsimpan;
        idsimpanan = idsimp.getText();
        idanggota = idang.getText();
        idsaldo = idsimpang.getText();
        jum = jumlahsimp.getText();
        tanggalsimpan = ((JTextField)tglsimpan.getDateEditor().getUiComponent()).getText();
        jattemsimpan = jatuhtempo.getText();
        
        if(idsimpanan.equals("") || idanggota.equals("") || idsaldo.equals("") || jum.equals("") || tanggalsimpan.equals("") || jattemsimpan.equals("")){
            JOptionPane.showMessageDialog(null, "Isi semua form");
        }else{
            simpan();
        }
    }
    
    //insert ke database , tabel simpan
    public void simpan(){
        String tanggalsimpan = ((JTextField)tglsimpan.getDateEditor().getUiComponent()).getText();
        
        //fungsi
        try{
            String sql0 = "SELECT * FROM simpan WHERE idanggota = '"+idang.getText()+"' && jenis = 'POKOK'";
            java.sql.Connection conn0=(Connection)config.configDB();
            java.sql.PreparedStatement pst0=conn0.prepareStatement(sql0);
            pst0.execute();
            java.sql.Statement stmt0 = conn0.createStatement();
            java.sql.ResultSet rs0 = stmt0.executeQuery(sql0);
                    
                //JIKA SUDAH ADA SIMPANAN POKOK
                if(rs0.next()){
                    if(idang.getText().equals(rs0.getString("idanggota"))){
                            
                        //jika jenis simpanan adalah wajib
                        if(cmbjenis.getSelectedItem().equals("WAJIB")){
                            String sql1 = "SELECT * FROM simpan WHERE idanggota = '"+idang.getText()+"' AND jenis = 'WAJIB'";
                            java.sql.Connection conn1=(Connection)config.configDB();
                            java.sql.Statement stmt1 = conn1.createStatement();
                            java.sql.ResultSet rs1 = stmt1.executeQuery(sql1);
                            
                            //jika anggota sudah ada
                            if(rs1.next()){
                                if(idang.getText().equals(rs1.getString("idanggota"))){
                                    validasiBulan();
                                    
                                    if(bulansama == true){
                                        JOptionPane.showMessageDialog(null, "Data untuk bulan ini sudah ada");
                                    }else{
                                        String sql11 = "INSERT INTO simpan VALUES('"+idsimp.getText()+"','"+idang.getText()+"','"+jumlahsimp.getText()+"','"+tanggalsimpan+"','"+cmbjenis.getSelectedItem().toString()+"','"+jatuhtempo.getText()+"')";
                                        pst0.executeUpdate(sql11);
                                        JOptionPane.showMessageDialog(null, "Proses Simpanan Berhasil");
                                        saldoData();
                                        validasiBulan1();
                                        
                                        if(bulansama1 == true){
                                            ubahKeteranganAktif();
                                        }
                                        cetakStruk();
                                    }
                                }
                            
                            //jika anggota belum ada
                            }else{
                                String sql11 = "INSERT INTO simpan VALUES('"+idsimp.getText()+"','"+idang.getText()+"','"+jumlahsimp.getText()+"','"+tanggalsimpan+"','"+cmbjenis.getSelectedItem().toString()+"','"+jatuhtempo.getText()+"')";
                                pst0.executeUpdate(sql11);
                                JOptionPane.showMessageDialog(null, "Proses Simpanan Berhasil");
                                saldoData();
                                validasiBulan1();
                                        
                                if(bulansama1 == true){
                                    ubahKeteranganAktif();
                                }
                                cetakStruk();
                            }
                            
                        //jika jenis simpanan adalah pokok
                        }else if(cmbjenis.getSelectedItem().equals("POKOK")){
                            
                            //notif
                            JOptionPane.showMessageDialog(null, "Simpanan pokok untuk anggota ini sudah dibuat");
                            
                        //jika jenis simpanan adalah sukarela    
                        }else if(cmbjenis.getSelectedItem().equals("SUKARELA")){
                            String sql2 = "SELECT * FROM simpan WHERE id = '"+idsimp.getText()+"'";
                            java.sql.Connection conn2=(Connection)config.configDB();
                            java.sql.PreparedStatement pst2=conn2.prepareStatement(sql2);
                            pst0.execute();
                            java.sql.Statement stmt2 = conn2.createStatement();
                            java.sql.ResultSet rs2 = stmt2.executeQuery(sql2);
                            
                            //jika id simpan sudah ada
                            if(rs2.next()){
                                if(idsimp.getText().equals(rs2.getString("id"))){
                                        jikasukarelaterpilih();
                                        if(minsimpansukarela == false){
                                            JOptionPane.showMessageDialog(null, "Minimal 50000");
                                        }else{
                                            saldoDataUbah();
                                            String sql21 = "UPDATE simpan SET jumlah = '"+jumlahsimp.getText()+"' WHERE id = '"+idsimp.getText()+"' && jenis = '"+cmbjenis.getSelectedItem()+"'";
                                            pst0.executeUpdate(sql21);
                                            JOptionPane.showMessageDialog(null, "Proses Ubah Simpanan Berhasil");
                                            JOptionPane.showMessageDialog(null, "Saldo Berubah");
                                        }
                                }
                            //jika id simpan belum ada
                            }else{
                                jikasukarelaterpilih();
                                if(minsimpansukarela == false){
                                    JOptionPane.showMessageDialog(null, "Minimal 50000");
                                }else{
                                    String sql22 = "INSERT INTO simpan VALUES('"+idsimp.getText()+"','"+idang.getText()+"','"+jumlahsimp.getText()+"','"+tanggalsimpan+"','"+cmbjenis.getSelectedItem().toString()+"','"+jatuhtempo.getText()+"')";
                                    pst0.executeUpdate(sql22);
                                    JOptionPane.showMessageDialog(null, "Proses Simpanan Berhasil");
                                    saldoData();
                                    cetakStruk();
                                }
                            }
                        }
                    }
                        
                    //JIKA BELUM ADA SIMPANAN POKOK
                    }else{
                        
                        //jika simpanan adalah pokok
                        if(cmbjenis.getSelectedItem().equals("POKOK")){
                            String sql1 = "INSERT INTO simpan VALUES('"+idsimp.getText()+"','"+idang.getText()+"','"+jumlahsimp.getText()+"','"+tanggalsimpan+"','"+cmbjenis.getSelectedItem().toString()+"','"+jatuhtempo.getText()+"')";
                            pst0.executeUpdate(sql1);
                            JOptionPane.showMessageDialog(null, "Proses Simpanan Berhasil");
                            saldoData();
                            cekTanggal();
                            
                            if(tanggal15 == true){
                                ubahKeteranganAktif();
                            }
                            cetakStruk();
                        //jika simpanan bukan pokok    
                        }else{
                            JOptionPane.showMessageDialog(null, "Simpanan pokok belum di buat untuk anggota ini");
                        }
                    }
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        load_table_ang();   
    }
    
    private void hapus(){
        // fungsi hapus data
        
        int yakin = JOptionPane.showConfirmDialog(null, "Yakin akan di hapus ?", "Hapus Data Simpanan", JOptionPane.YES_NO_OPTION);
        
        if(yakin == JOptionPane.YES_OPTION){
            
            try {
                String sql0 = "select * from simpan where id = '"+idsimp.getText()+"'";
                java.sql.Connection conn0=(Connection)config.configDB();
                java.sql.PreparedStatement pst0=conn0.prepareStatement(sql0);
                java.sql.Statement stm0=conn0.createStatement();
                java.sql.ResultSet res0=stm0.executeQuery(sql0);
            
                if(res0.next()){
                
                    if(cmbjenis.getSelectedItem().equals("POKOK")){
                        
                        String sql ="delete from simpan where idanggota='"+idang.getText()+"'";
                        java.sql.Connection conn=(Connection)config.configDB();
                        java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                        pst.execute();
                        JOptionPane.showMessageDialog(this, "berhasil di hapus");
                        resetSaldo();
                        ubahKeteranganDaftarTunggu();
                        
                    }else if(cmbjenis.getSelectedItem().equals("WAJIB")){
                        
                            String sql1 ="select * from simpan where id='"+idsimp.getText()+"'";
                            java.sql.Connection conn1=(Connection)config.configDB();
                            java.sql.PreparedStatement pst1=conn1.prepareStatement(sql1);
                            pst1.execute();
                            java.sql.Statement stm1 = conn1.createStatement();
                            java.sql.ResultSet res1 = stm1.executeQuery(sql1);
                            
                            if(res1.next()){
                                String tanggaldb = res1.getString("tanggal");
                                java.util.Date tgldb = new Date(tanggaldb);
                                java.util.Date tglreal = new java.util.Date();
                                SimpleDateFormat simpledate = new SimpleDateFormat("dd MMMMMMMMM YYYY", Locale.getDefault());
                                SimpleDateFormat simpledate1 = new SimpleDateFormat("dd", Locale.getDefault());
                                SimpleDateFormat simpledate2 = new SimpleDateFormat("MM", Locale.getDefault());
                                SimpleDateFormat simpledate3 = new SimpleDateFormat("YYYY", Locale.getDefault());
                                
                                int tanggalr = Integer.parseInt(simpledate1.format(tglreal));
                                int bulanr = Integer.parseInt(simpledate2.format(tglreal));
                                int tahunr = Integer.parseInt(simpledate3.format(tglreal));
                                
                                int tanggals = Integer.parseInt(simpledate1.format(tgldb));
                                int bulans = Integer.parseInt(simpledate2.format(tgldb));
                                int tahuns = Integer.parseInt(simpledate3.format(tgldb));
                                
                                if(tahuns < tahunr){
                                    JOptionPane.showMessageDialog(null, "Data tidak dapat di hapus");
                                }else if(tahuns == tahunr){
                                    if(bulans < bulanr){
                                        JOptionPane.showMessageDialog(null, "Data tidak dapat di hapus");
                                    }else{
                                        String sql ="delete from simpan where id='"+idsimp.getText()+"'";
                                        java.sql.Connection conn=(Connection)config.configDB();
                                        java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                                        pst.execute();
                                        JOptionPane.showMessageDialog(this, "berhasil di hapus");
                                        saldoDataHapus();
                                        cekTanggal();
                                        
                                        if(tanggal15 == false){
                                            ubahKeteranganTidakAktif();
                                        }
                                    }
                                }else{
                                    String sql ="delete from simpan where id='"+idsimp.getText()+"'";
                                    java.sql.Connection conn=(Connection)config.configDB();
                                    java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                                    pst.execute();
                                    JOptionPane.showMessageDialog(this, "berhasil di hapus");
                                    saldoDataHapus();
                                }
                            }
                    }else if(cmbjenis.getSelectedItem().equals("SUKARELA")){
                        String sql ="delete from simpan where id='"+idsimp.getText()+"'";
                        java.sql.Connection conn=(Connection)config.configDB();
                        java.sql.PreparedStatement pst=conn.prepareStatement(sql);
                        pst.execute();
                        JOptionPane.showMessageDialog(this, "berhasil di hapus");
                        saldoDataHapus();
                    }
                }
            }catch(Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            load_table_ang();
            bersihkan();
        }
    }
    
    //insert ke database tabel saldo
    public void saldoData(){
        
        //fungsi
        try{
            String sql = "SELECT id FROM saldo WHERE id = '"+idsimpang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet rs=stm.executeQuery(sql);
            
            //jika id sudah ada
            if(rs.next()){
                    if(idsimpang.getText().equals(rs.getString("id"))){
                        
                        //jika jenis simpanan adalah pokok
                        if(cmbjenis.getSelectedItem().toString().equals("POKOK")){
                            String sql1 = "UPDATE saldo SET saldopokok = saldopokok + '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst.executeUpdate(sql1);
                            JOptionPane.showMessageDialog(null, "Simpanan masuk ke saldo");
                        }
                        
                        //jika jenis simpanan adalah wajib
                        else if(cmbjenis.getSelectedItem().toString().equals("WAJIB")){
                            String sql12 = "UPDATE saldo SET saldowajib = saldowajib + '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst.executeUpdate(sql12);
                            JOptionPane.showMessageDialog(null, "Simpanan masuk ke saldo");
                        }
                        
                        //jika jenis simpanan adalah sukarela
                        else if(cmbjenis.getSelectedItem().toString().equals("SUKARELA")){
                            String sql13 = "UPDATE saldo SET saldosukarela = saldosukarela +'"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst.executeUpdate(sql13);
                            JOptionPane.showMessageDialog(null, "Simpanan masuk ke saldo");
                        }
                    }
                    
            //jika id belum ada        
            }else{
                
                //jika jenis simpanan adalah pokok
                if(cmbjenis.getSelectedItem().toString().equals("POKOK")){
                    String sql2 = "INSERT INTO saldo VALUES('"+idsimpang.getText()+"','"+idang.getText()+"','0','"+jumlahsimp.getText()+"','0')";
                    pst.executeUpdate(sql2);
                    JOptionPane.showMessageDialog(null, "Simpanan masuk ke saldo");
                    cetakStruk();
                }
                
                //jika jenis simpanan adalah wajib 
                else if(cmbjenis.getSelectedItem().toString().equals("WAJIB")){
                    JOptionPane.showMessageDialog(null, "Buat simpanan pokok dahulu...");
                }
                
                //jika jenis simpanan adalah wajib 
                else if(cmbjenis.getSelectedItem().toString().equals("SUKARELA")){
                    JOptionPane.showMessageDialog(null, "Buat simpanan pokok dahulu...");
                }
            }        
        }catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //insert ke database tabel saldo
    public void saldoDataUbah(){
        
        //fungsi
        try{
            String sql = "SELECT * FROM simpan WHERE id = '"+idsimp.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet rs=stm.executeQuery(sql);
            
            if(rs.next()){
            
                String sql0 = "SELECT id FROM saldo WHERE id = '"+idsimpang.getText()+"'";
                java.sql.Connection conn0=(Connection)config.configDB();
                java.sql.PreparedStatement pst0=conn0.prepareStatement(sql0);
                java.sql.Statement stm0=conn0.createStatement();
                java.sql.ResultSet rs0=stm0.executeQuery(sql0);
            
                //jika id sudah ada
                if(rs0.next()){
                    if(idsimpang.getText().equals(rs0.getString("id"))){

                        //jika jenis simpanan adalah pokok
                        if(cmbjenis.getSelectedItem().toString().equals("POKOK")){
                            String sql1 = "UPDATE saldo SET saldopokok = saldopokok - '"+rs.getString("jumlah")+"' + '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst0.executeUpdate(sql1);
                        }

                        //jika jenis simpanan adalah wajib
                        else if(cmbjenis.getSelectedItem().toString().equals("WAJIB")){
                            String sql12 = "UPDATE saldo SET saldowajib = saldowajib - '"+rs.getString("jumlah")+"' + '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst0.executeUpdate(sql12);
                        }

                        //jika jenis simpanan adalah sukarela
                        else if(cmbjenis.getSelectedItem().toString().equals("SUKARELA")){
                            String sql13 = "UPDATE saldo SET saldosukarela = saldosukarela - '"+rs.getString("jumlah")+"' + '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                            pst0.executeUpdate(sql13);
                        }
                    }      
                }
            }    
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    //insert ke database tabel saldo
    public void saldoDataHapus(){
        
        //fungsi
        try{
            String sql = "SELECT id FROM saldo WHERE id = '"+idsimpang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet rs=stm.executeQuery(sql);
            
            //jika id sudah ada
            if(rs.next()){
                if(idsimpang.getText().equals(rs.getString("id"))){
                        
                    //jika jenis simpanan adalah pokok
                    if(cmbjenis.getSelectedItem().toString().equals("POKOK")){
                        String sql1 = "UPDATE saldo SET saldopokok = saldopokok - '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                        pst.executeUpdate(sql1);
                        JOptionPane.showMessageDialog(null, "Saldo berkurang");
                    }
                        
                    //jika jenis simpanan adalah wajib
                    else if(cmbjenis.getSelectedItem().toString().equals("WAJIB")){
                        String sql12 = "UPDATE saldo SET saldowajib = saldowajib - '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                        pst.executeUpdate(sql12);
                        JOptionPane.showMessageDialog(null, "Saldo berkurang");
                    }
                        
                    //jika jenis simpanan adalah sukarela
                    else if(cmbjenis.getSelectedItem().toString().equals("SUKARELA")){
                        String sql13 = "UPDATE saldo SET saldosukarela = saldosukarela - '"+jumlahsimp.getText()+"' WHERE id = '"+idsimpang.getText()+"'";
                        pst.executeUpdate(sql13);
                        JOptionPane.showMessageDialog(null, "Saldo berkurang");
                    }
                }      
            }
        }catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void resetSaldo(){
        try{
            String sql = "DELETE FROM saldo WHERE id = '"+idsimpang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }    
    }
    
    //mengambil data nama dari database, tabel anggota, id
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
    
    //menampilkan data id saldo dari database, tabel saldo, id anggota 
    public void tampilkanDataIDSALfromIDA(){
        try{
            String sql = "SELECT * FROM saldo WHERE idanggota='"+idang.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement st=conn.createStatement();
            java.sql.ResultSet rs=st.executeQuery(sql);
        if(rs.next()){
            if(idang.getText().equals(rs.getString("idanggota"))){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(1);
                idsimpang.setText((String)ob[0]);
            }
        }else{
            autokodesaldo();
        }
            rs.close();
            st.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //menampilkan isi database, tabel simpan ke form simpan
    public void tampilkanAllFromIDS(){
        try{
            String sql = "SELECT * FROM simpan WHERE id = '"+idsimp.getText()+"'";
            java.sql.Connection conn=(Connection)config.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            java.sql.Statement st=conn.createStatement();
            java.sql.ResultSet rs=st.executeQuery(sql);
            
            while(rs.next()){
                idang.setText(rs.getString("idanggota"));
                tampilkanDataFromId();
                jumlahsimp.setText(rs.getString("jumlah"));
                ((JTextField)tglsimpan.getDateEditor().getUiComponent()).setText(rs.getString("tanggal"));
                jatuhtempo.setText(rs.getString("jatuhtempo"));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //membersihkan form
    private void bersihkan(){
        autokode();
        idang.setText("");
        autokodesaldo();
        namang.setText("");
        cmbjenis.setSelectedItem("POKOK");
        delete.setEnabled(false);
        realtime();
        jatuhtempo();
        jikaidkosong();
        jikapokokterpilih();
        jikawajibterpilih();
        jikasukarelaterpilih();
    }
    
    //fungsi mencetak struk di jasper
    public void cetakStruk(){
        //set lokal
        Locale lokal = new Locale("id","ID");
        Locale.setDefault(lokal);
        
        try{
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("strukSimpan.jasper"), null, config.configDB());
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
        jLabel2 = new javax.swing.JLabel();
        idang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        idsimp = new javax.swing.JTextField();
        simpan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblsimpan = new javax.swing.JTable();
        bersihkan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        idsimpang = new javax.swing.JTextField();
        cmbjenis = new javax.swing.JComboBox();
        carinama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblcari = new javax.swing.JTable();
        jatuhtempo = new javax.swing.JTextField();
        jumlahsimp = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        labeljatuhtempo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        id = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        namang = new javax.swing.JTextField();
        tglsimpan = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("Form Simpanan");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("ID ANGGOTA        ");

        idang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idangActionPerformed(evt);
            }
        });

        jLabel3.setText("ID SIMPANAN       ");

        simpan.setText("SIMPAN");
        simpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simpanMouseClicked(evt);
            }
        });
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        jLabel9.setText("NAMA ANGGOTA  ");

        tblsimpan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblsimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblsimpanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblsimpan);

        bersihkan.setText("BATAL");
        bersihkan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bersihkanActionPerformed(evt);
            }
        });

        jLabel7.setText("ID SALDO              ");

        cmbjenis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "WAJIB", "POKOK", "SUKARELA" }));
        cmbjenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbjenisActionPerformed(evt);
            }
        });

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

        jumlahsimp.setForeground(new java.awt.Color(0, 204, 0));
        jumlahsimp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jumlahsimpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jumlahsimpKeyTyped(evt);
            }
        });

        jLabel12.setText("JUMLAH                 ");

        jLabel17.setText("TANGGAL              ");

        labeljatuhtempo.setText("JATUH TEMPO      ");

        jLabel8.setForeground(new java.awt.Color(0, 204, 0));
        jLabel8.setText("Rp");

        jLabel13.setText("JENIS                    ");

        back.setText("Kembali");
        back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        id.setText("YOU ARE...");

        delete.setText("HAPUS");
        delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        tglsimpan.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                tglsimpanHierarchyChanged(evt);
            }
        });

        jLabel4.setText("Cari");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labeljatuhtempo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(simpan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(cmbjenis, 0, 219, Short.MAX_VALUE)
                            .addComponent(jatuhtempo)
                            .addComponent(idsimpang)
                            .addComponent(idang, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(namang)
                            .addComponent(tglsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carinama))
                            .addComponent(idsimp)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(delete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bersihkan, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jumlahsimp)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(back)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(idsimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idsimpang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(namang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(carinama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jumlahsimp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbjenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(tglsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labeljatuhtempo)
                            .addComponent(jatuhtempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bersihkan)
                            .addComponent(delete)
                            .addComponent(simpan)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back)
                    .addComponent(id))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        jatuhtempo();
        simpanData();
        bersihkan();
    }//GEN-LAST:event_simpanActionPerformed

    private void tblsimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblsimpanMouseClicked
        
        //menampilkan data dari tabel
        int baris = tblsimpan.rowAtPoint(evt.getPoint());
        String idsimpan = tblsimpan.getValueAt(baris, 0).toString();
        String jenis = tblsimpan.getValueAt(baris, 4).toString();
        idsimp.setText(idsimpan);
        cmbjenis.setSelectedItem(jenis);    
        tampilkanAllFromIDS();
        tampilkanDataIDSALfromIDA();
        delete.setEnabled(true);
        jikaidkosong();
    }//GEN-LAST:event_tblsimpanMouseClicked

    private void bersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bersihkanActionPerformed
        bersihkan();
    }//GEN-LAST:event_bersihkanActionPerformed

    private void carinamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyReleased
        load_table_cariang();
    }//GEN-LAST:event_carinamaKeyReleased

    private void tblcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcariMouseClicked
        
        // menampilkan data dari tabel ke form
        int baris = tblcari.rowAtPoint(evt.getPoint());
        String idanggota = tblcari.getValueAt(baris, 0).toString();
        String nama = tblcari.getValueAt(baris,1).toString();
        
        idang.setText(idanggota);
        namang.setText(nama);
        carinama.setText("");
        tampilkanDataFromId();
        tampilkanDataIDSALfromIDA();
        delete.setEnabled(false);
        jikaidkosong();
    }//GEN-LAST:event_tblcariMouseClicked

    private void carinamaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_carinamaMouseClicked
        carinama.setText("");
        carinama.setForeground(Color.black);
    }//GEN-LAST:event_carinamaMouseClicked

    private void simpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanMouseClicked
        if(idang.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Pilih anggota dahulu");
        }
    }//GEN-LAST:event_simpanMouseClicked

    private void cmbjenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbjenisActionPerformed
        jikapokokterpilih();
        jikawajibterpilih();
        jikasukarelaterpilih();
    }//GEN-LAST:event_cmbjenisActionPerformed

    private void jumlahsimpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahsimpKeyTyped
        hanyaangka(evt);
    }//GEN-LAST:event_jumlahsimpKeyTyped

    private void carinamaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carinamaKeyTyped
        hanyahuruf(evt);
    }//GEN-LAST:event_carinamaKeyTyped

    private void idangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idangActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        berandaFrameMaster bf = new berandaFrameMaster();
        bf.setId(id.getText());
        bf.setVisible(true);
        dispose();
    }//GEN-LAST:event_backActionPerformed

    private void jumlahsimpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumlahsimpKeyReleased
        
    }//GEN-LAST:event_jumlahsimpKeyReleased

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        hapus();
    }//GEN-LAST:event_deleteActionPerformed

    private void tglsimpanHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_tglsimpanHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tglsimpanHierarchyChanged

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
            java.util.logging.Logger.getLogger(simpan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(simpan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(simpan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(simpan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new simpan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JButton bersihkan;
    private javax.swing.JTextField carinama;
    private javax.swing.JComboBox cmbjenis;
    private javax.swing.JButton delete;
    private javax.swing.JLabel id;
    private javax.swing.JTextField idang;
    private javax.swing.JTextField idsimp;
    private javax.swing.JTextField idsimpang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jatuhtempo;
    private javax.swing.JTextField jumlahsimp;
    private javax.swing.JLabel labeljatuhtempo;
    private javax.swing.JTextField namang;
    private javax.swing.JButton simpan;
    private javax.swing.JTable tblcari;
    private javax.swing.JTable tblsimpan;
    private com.toedter.calendar.JDateChooser tglsimpan;
    // End of variables declaration//GEN-END:variables
}
