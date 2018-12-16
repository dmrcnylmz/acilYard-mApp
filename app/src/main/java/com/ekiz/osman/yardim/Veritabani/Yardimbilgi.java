package com.ekiz.osman.yardim.Veritabani;

public class Yardimbilgi {
    private String Tc;
    private String Telefon;
    private String Enlem;
    private String Boylam;
    private String AcilDurum;

    public Yardimbilgi(String tc, String telefon, String enlem, String boylam, String acildurum){
        Tc=tc;
        Telefon=telefon;
        Enlem=enlem;
        Boylam=boylam;
        AcilDurum=acildurum;
    }

    public String getTc() {
        return Tc;
    }

    public void setTc(String tc) {
        Tc = tc;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getEnlem() {
        return Enlem;
    }

    public void setEnlem(String enlem) {
        Enlem = enlem;
    }

    public String getBoylam() {
        return Boylam;
    }

    public void setBoylam(String boylam) {
        Boylam = boylam;
    }

    public String getAcilDurum() {
        return AcilDurum;
    }

    public void setAcilDurum(String acilDurum) {
        AcilDurum = acilDurum;
    }
}
