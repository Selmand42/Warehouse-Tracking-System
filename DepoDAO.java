package Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepoDAO {
    public void addDepo(Depo depo) {
        String sql = "INSERT INTO depolar (depo_adi) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, depo.getDepoAdi());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDepo(int id) {
        String sql = "DELETE FROM depolar WHERE depo_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Depo> getAllDepolar() {
        List<Depo> depolar = new ArrayList<>();
        String sql = "SELECT * FROM depolar";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int depoId = resultSet.getInt("depo_id");
                String depoAdi = resultSet.getString("depo_adi");
                depolar.add(new Depo(depoId, depoAdi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return depolar;
    }
}
