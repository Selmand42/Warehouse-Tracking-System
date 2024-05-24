package Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KullaniciDAO {
    // Kullanıcı ekleme metodu
    public void addKullanici(Kullanici kullanici) {
        String sql = "INSERT INTO kullanicilar (kullanici_adi, sifre, rol) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, kullanici.getKullaniciAdi());
            statement.setString(2, kullanici.getSifre());
            statement.setString(3, kullanici.getRol());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kullanıcı silme metodu
    public void deleteKullanici(int id) {
        String sql = "DELETE FROM kullanicilar WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm kullanıcıları listeleme metodu
    public List<Kullanici> getAllKullanicilar() {
        List<Kullanici> kullanicilar = new ArrayList<>();
        String sql = "SELECT * FROM kullanicilar";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String kullaniciAdi = resultSet.getString("kullanici_adi");
                String sifre = resultSet.getString("sifre");
                String rol = resultSet.getString("rol");
                kullanicilar.add(new Kullanici(id, kullaniciAdi, sifre, rol));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kullanicilar;
    }

    // Kullanıcıyı kullanıcı adı ve şifre ile doğrulama metodu
    public Kullanici getKullaniciByKullaniciAdiVeSifre(String kullaniciAdi, String sifre) {
        String sql = "SELECT * FROM kullanicilar WHERE kullanici_adi = ? AND sifre = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, kullaniciAdi);
            statement.setString(2, sifre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String rol = resultSet.getString("rol");
                    return new Kullanici(id, kullaniciAdi, sifre, rol);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
