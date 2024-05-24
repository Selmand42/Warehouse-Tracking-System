package Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UrunDAO {
    private String username;

    public UrunDAO(String username) {
        this.username = username;
    }

    // Ürün ekleme veya güncelleme metodu
    public void addOrUpdateUrun(Urun urun) {
        String checkSql = "SELECT * FROM urunler WHERE urun_adi = ? AND depo_id = ?";
        String updateSql = "UPDATE urunler SET miktar = miktar + ? WHERE urun_adi = ? AND depo_id = ?";
        String insertSql = "INSERT INTO urunler (urun_adi, miktar, depo_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            
            // Önce ürünün depoda olup olmadığını kontrol edelim
            checkStatement.setString(1, urun.getUrunAdi());
            checkStatement.setInt(2, urun.getDepoId());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Ürün zaten depoda varsa, miktarını güncelleyelim
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, urun.getMiktar());
                    updateStatement.setString(2, urun.getUrunAdi());
                    updateStatement.setInt(3, urun.getDepoId());
                    updateStatement.executeUpdate();
                    LogHelper.logAction("Ürün güncellendi: " + urun.getUrunAdi() + ", miktar: " + urun.getMiktar() + ", depo: " + urun.getDepoId(), username);
                }
            } else {
                // Ürün depoda yoksa, yeni bir ürün ekleyelim
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setString(1, urun.getUrunAdi());
                    insertStatement.setInt(2, urun.getMiktar());
                    insertStatement.setInt(3, urun.getDepoId());
                    insertStatement.executeUpdate();
                    LogHelper.logAction("Ürün eklendi: " + urun.getUrunAdi() + ", miktar: " + urun.getMiktar() + ", depo: " + urun.getDepoId(), username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ürün silme metodu
    public void deleteUrun(int id, String urunAdi, int depoId) {
        String sql = "DELETE FROM urunler WHERE urun_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            LogHelper.logAction("Ürün silindi: " + urunAdi + ", depo: " + depoId, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ürün satma metodu
    public void sellUrun(String urunAdi, int miktar, int depoId) {
        String checkSql = "SELECT miktar FROM urunler WHERE urun_adi = ? AND depo_id = ?";
        String updateSql = "UPDATE urunler SET miktar = miktar - ? WHERE urun_adi = ? AND depo_id = ?";
        String deleteSql = "DELETE FROM urunler WHERE urun_adi = ? AND depo_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            
            checkStatement.setString(1, urunAdi);
            checkStatement.setInt(2, depoId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int mevcutMiktar = resultSet.getInt("miktar");
                if (mevcutMiktar >= miktar) {
                    // Ürünün miktarını güncelle
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setInt(1, miktar);
                        updateStatement.setString(2, urunAdi);
                        updateStatement.setInt(3, depoId);
                        updateStatement.executeUpdate();
                        LogHelper.logAction("Ürün satıldı: " + urunAdi + ", miktar: " + miktar + ", depo: " + depoId, username);
                    }
                    
                    // Eğer miktar sıfır veya negatif olduysa ürünü sil
                    if (mevcutMiktar - miktar <= 0) {
                        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                            deleteStatement.setString(1, urunAdi);
                            deleteStatement.setInt(2, depoId);
                            deleteStatement.executeUpdate();
                            LogHelper.logAction("Ürün tamamen satıldı ve silindi: " + urunAdi + ", depo: " + depoId, username);
                        }
                    }
                } else {
                    throw new SQLException("Yeterli miktarda ürün yok.");
                }
            } else {
                throw new SQLException("Depoda bu ürün bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm ürünleri listeleme metodu
    public List<Urun> getAllUrunler() {
        List<Urun> urunler = new ArrayList<>();
        String sql = "SELECT * FROM urunler";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int urunId = resultSet.getInt("urun_id");
                String urunAdi = resultSet.getString("urun_adi");
                int miktar = resultSet.getInt("miktar");
                int depoId = resultSet.getInt("depo_id");
                urunler.add(new Urun(urunId, urunAdi, miktar, depoId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urunler;
    }

    // Ürün transfer metodu
    public void transferUrun(String urunAdi, int miktar, int kaynakDepoId, int hedefDepoId) {
        // Kaynak depodaki ürünü kontrol et
        String checkSql = "SELECT miktar FROM urunler WHERE urun_adi = ? AND depo_id = ?";
        String updateKaynakSql = "UPDATE urunler SET miktar = miktar - ? WHERE urun_adi = ? AND depo_id = ?";
        String updateHedefSql = "UPDATE urunler SET miktar = miktar + ? WHERE urun_adi = ? AND depo_id = ?";
        String insertHedefSql = "INSERT INTO urunler (urun_adi, miktar, depo_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, urunAdi);
            checkStatement.setInt(2, kaynakDepoId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int mevcutMiktar = resultSet.getInt("miktar");
                if (mevcutMiktar >= miktar) {
                    // Kaynak depodan miktarı düş
                    try (PreparedStatement updateKaynakStatement = connection.prepareStatement(updateKaynakSql)) {
                        updateKaynakStatement.setInt(1, miktar);
                        updateKaynakStatement.setString(2, urunAdi);
                        updateKaynakStatement.setInt(3, kaynakDepoId);
                        updateKaynakStatement.executeUpdate();
                        LogHelper.logAction("Ürün transfer edildi: " + urunAdi + ", miktar: " + miktar + " kaynaktan: " + kaynakDepoId + " hedefe: " + hedefDepoId, username);
                    }

                    // Hedef depoda ürün var mı kontrol et
                    try (PreparedStatement checkHedefStatement = connection.prepareStatement(checkSql)) {
                        checkHedefStatement.setString(1, urunAdi);
                        checkHedefStatement.setInt(2, hedefDepoId);
                        resultSet = checkHedefStatement.executeQuery();

                        if (resultSet.next()) {
                            // Hedef depoda ürün varsa miktarı güncelle
                            try (PreparedStatement updateHedefStatement = connection.prepareStatement(updateHedefSql)) {
                                updateHedefStatement.setInt(1, miktar);
                                updateHedefStatement.setString(2, urunAdi);
                                updateHedefStatement.setInt(3, hedefDepoId);
                                updateHedefStatement.executeUpdate();
                            }
                        } else {
                            // Hedef depoda ürün yoksa yeni kayıt ekle
                            try (PreparedStatement insertHedefStatement = connection.prepareStatement(insertHedefSql)) {
                                insertHedefStatement.setString(1, urunAdi);
                                insertHedefStatement.setInt(2, miktar);
                                insertHedefStatement.setInt(3, hedefDepoId);
                                insertHedefStatement.executeUpdate();
                            }
                        }
                    }
                } else {
                    throw new SQLException("Kaynak depoda yeterli miktarda ürün yok.");
                }
            } else {
                throw new SQLException("Kaynak depoda ürün bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
