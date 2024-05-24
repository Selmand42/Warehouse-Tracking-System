package Project;

public class Depo {
    private int depoId;
    private String depoAdi;

    // Constructor with all fields
    public Depo(int depoId, String depoAdi) {
        this.depoId = depoId;
        this.depoAdi = depoAdi;
    }

    // Constructor without depoId (for new entries)
    public Depo(String depoAdi) {
        this.depoAdi = depoAdi;
    }

    // Getter and Setter methods
    public int getDepoId() {
        return depoId;
    }

    public void setDepoId(int depoId) {
        this.depoId = depoId;
    }

    public String getDepoAdi() {
        return depoAdi;
    }

    public void setDepoAdi(String depoAdi) {
        this.depoAdi = depoAdi;
    }

    // Override toString method for JComboBox display
    @Override
    public String toString() {
        return depoAdi;
    }
}

