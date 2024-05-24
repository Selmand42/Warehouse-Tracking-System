package Project;

public class Kullanici {
    private int id;
    private String kullaniciAdi;
    private String sifre;
    private String rol;

    public Kullanici(int id, String kullaniciAdi, String sifre, String rol) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.rol = rol;
    }

    public Kullanici(String kullaniciAdi, String sifre, String rol) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public String getRol() {
        return rol;
    }
}
