package com.example.jmp.model;

public class DataModel {
    private int id;
    private  String name;
    private  String telepon;
    private  String jk;
    private  String alamat;
    private byte[]image;

    public DataModel(int id, String name, String telepon, String jk, String alamat, byte[] image) {
        this.id = id;
        this.name = name;
        this.telepon = telepon;
        this.jk = jk;
        this.alamat = alamat;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
