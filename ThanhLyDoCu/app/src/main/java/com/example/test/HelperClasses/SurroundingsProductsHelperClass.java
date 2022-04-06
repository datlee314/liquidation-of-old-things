package com.example.test.HelperClasses;

public class SurroundingsProductsHelperClass {
    String Tenhinh, gia, noidung, Link, tendm, tinhtrang, sdt,khuvuc, date, idcustomer, id;


    public SurroundingsProductsHelperClass(){}

    public SurroundingsProductsHelperClass(String tenhinh, String gia, String noidung, String link, String tendm, String tinhtrang, String sdt, String khuvuc, String date, String idcustomer, String id) {
        Tenhinh = tenhinh;
        this.gia = gia;
        this.noidung = noidung;
        Link = link;
        this.tendm = tendm;
        this.tinhtrang = tinhtrang;
        this.sdt = sdt;
        this.khuvuc = khuvuc;
        this.date = date;
        this.idcustomer = idcustomer;
        this.id = id;
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

    public String getTendm() {
        return tendm;
    }

    public void setTendm(String tendm) {
        this.tendm = tendm;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(String idcustomer) {
        this.idcustomer = idcustomer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
