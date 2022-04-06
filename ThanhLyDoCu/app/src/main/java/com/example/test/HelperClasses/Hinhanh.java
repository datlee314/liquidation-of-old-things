package com.example.test.HelperClasses;

public class Hinhanh {
    String Tenhinh;
    String gia;
    String noidung;
    String Link;
String tendm;
String tinhtrang;
String sdt,khuvuc;
String date;
String idcustomer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
public Hinhanh(){

}

    public Hinhanh(String idcustomer,String date,String khuvuc, String tinhtrang, String tendm, String id, String tenhinh, String gia, String noidung, String link, String sdt) {

      this.idcustomer=idcustomer;
       this.date=date;
        this.khuvuc = khuvuc;
    this.tinhtrang=tinhtrang;
    this.tendm=tendm;
        this.id = id;
        Tenhinh = tenhinh;
        this.gia = gia;
        this.noidung = noidung;
        Link = link;
        this.sdt = sdt;

    }
    public String getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(String idcustomer) {
        this.idcustomer = idcustomer;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }
    public String getTendm() {
        return tendm;
    }

    public void setTendm(String tendm) {
        this.tendm = tendm;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTenhinh() {
        return Tenhinh;
    }


    public void setTenhinh(String tenhinh) {
        Tenhinh = tenhinh;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }


}
