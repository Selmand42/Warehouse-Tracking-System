package Project;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UrunTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Ürün Adı", "Miktar", "Depo ID"};
    private List<Urun> urunler;

    @Override
    public int getRowCount() {
        return urunler == null ? 0 : urunler.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Urun urun = urunler.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return urun.getUrunId();
            case 1:
                return urun.getUrunAdi();
            case 2:
                return urun.getMiktar();
            case 3:
                return urun.getDepoId();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setUrunler(List<Urun> urunler) {
        this.urunler = urunler;
        fireTableDataChanged();
    }
}
