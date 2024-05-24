package Project;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class KullaniciTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Kullanıcı Adı", "Şifre", "Rol"};
    private List<Kullanici> kullanicilar;

    @Override
    public int getRowCount() {
        return kullanicilar == null ? 0 : kullanicilar.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Kullanici kullanici = kullanicilar.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return kullanici.getId();
            case 1:
                return kullanici.getKullaniciAdi();
            case 2:
                return kullanici.getSifre();
            case 3:
                return kullanici.getRol();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setKullanicilar(List<Kullanici> kullanicilar) {
        this.kullanicilar = kullanicilar;
        fireTableDataChanged();
    }
}
