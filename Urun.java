package Project;

public class Urun {
    private int urunId;
    private String urunAdi;
    private int miktar;
    private int depoId;

    // Constructor with all fields
    public Urun(int urunId, String urunAdi, int miktar, int depoId) {
        this.urunId = urunId;
        this.urunAdi = urunAdi;
        this.miktar = miktar;
        this.depoId = depoId;
    }

    // Constructor without urunId (for new entries)
    public Urun(String urunAdi, int miktar, int depoId) {
        this.urunAdi = urunAdi;
        this.miktar = miktar;
        this.depoId = depoId;
    }

    // Getter and Setter methods
    public int getUrunId() {
        return urunId;
    }

    public void setUrunId(int urunId) {
        this.urunId = urunId;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public int getDepoId() {
        return depoId;
    }

    public void setDepoId(int depoId) {
        this.depoId = depoId;
    }
}
