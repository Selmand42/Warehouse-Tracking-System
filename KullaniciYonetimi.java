package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KullaniciYonetimi extends JFrame {
    private JTextField kullaniciAdiField;
    private JTextField sifreField;
    private JComboBox<String> rolComboBox;
    private JButton ekleButton;
    private JButton silButton;
    private JTable kullaniciTable;
    private KullaniciTableModel kullaniciTableModel;
    private KullaniciDAO kullaniciDAO;

    public KullaniciYonetimi() {
        kullaniciDAO = new KullaniciDAO();

        setTitle("Kullanıcı Yönetimi");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Kullanıcı Adı:"));
        kullaniciAdiField = new JTextField();
        panel.add(kullaniciAdiField);

        panel.add(new JLabel("Şifre:"));
        sifreField = new JTextField();
        panel.add(sifreField);

        panel.add(new JLabel("Rol:"));
        rolComboBox = new JComboBox<>(new String[]{"yonetici", "kullanici"});
        panel.add(rolComboBox);

        ekleButton = new JButton("Ekle");
        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullaniciAdi = kullaniciAdiField.getText();
                String sifre = sifreField.getText();
                String rol = (String) rolComboBox.getSelectedItem();
                Kullanici kullanici = new Kullanici(kullaniciAdi, sifre, rol);
                kullaniciDAO.addKullanici(kullanici);
                refreshTable();
            }
        });
        panel.add(ekleButton);

        silButton = new JButton("Sil");
        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kullaniciTable.getSelectedRow();
                if (selectedRow != -1) {
                    int kullaniciId = (int) kullaniciTableModel.getValueAt(selectedRow, 0);
                    kullaniciDAO.deleteKullanici(kullaniciId);
                    refreshTable();
                }
            }
        });
        panel.add(silButton);

        add(panel, BorderLayout.NORTH);

        kullaniciTableModel = new KullaniciTableModel();
        kullaniciTable = new JTable(kullaniciTableModel);
        add(new JScrollPane(kullaniciTable), BorderLayout.CENTER);

        refreshTable();
    }

    private void refreshTable() {
        List<Kullanici> kullanicilar = kullaniciDAO.getAllKullanicilar();
        kullaniciTableModel.setKullanicilar(kullanicilar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KullaniciYonetimi().setVisible(true);
            }
        });
    }
}
