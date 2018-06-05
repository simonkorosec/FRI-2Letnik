package imenik;

import java.util.Comparator;

class PrijateljPrimerjajPoImenu implements Comparator<Prijatelj> {
    @Override
    public int compare(Prijatelj o1, Prijatelj o2) {
        String ime1 = o1.getPriimek() + ", " + o1.getIme();
        String ime2 = o2.getPriimek() + ", " + o2.getIme();
        return ime1.compareToIgnoreCase(ime2);
    }
}

class PrijateljPrimerjajPoTelSt implements Comparator<Prijatelj> {
    @Override
    public int compare(Prijatelj o1, Prijatelj o2) {
        String s1 = o1.getTelefonskaStevilka();
        String s2 = o2.getTelefonskaStevilka();

        s1 = s1.substring(s1.length() - 8);
        s2 = s2.substring(s2.length() - 8);
        return -s1.compareTo(s2);
    }
}

class Prijatelj implements java.io.Serializable {
    private String ime;
    private String priimek;
    private String telefonskaStevilka;

    public Prijatelj() {
    }

    public Prijatelj(String ime, String priimek, String telefonskaStevilka) {
        this.ime = ime;
        this.priimek = priimek;
        this.telefonskaStevilka = telefonskaStevilka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getTelefonskaStevilka() {
        return telefonskaStevilka;
    }

    public void setTelefonskaStevilka(String telefonskaStevilka) {
        this.telefonskaStevilka = telefonskaStevilka;
    }

    @Override
    public String toString() {
        return priimek + ", " + ime + " - " + telefonskaStevilka;
    }
}
