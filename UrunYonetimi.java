package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UrunYonetimi extends JFrame {
    private JComboBox<Depo> depoComboBox;
    private JTextField urunAdiField;
    private JTextField miktarField;
    private JButton ekleButton;
    private JButton silButton;
    private JButton transferButton;
    private JButton satButton;
    private JTable urunTable;
    private UrunTableModel urunTableModel;
    private UrunDAO urunDAO;
    private DepoDAO depoDAO;
    private String username;

    public UrunYonetimi(String username) {
        this.username = username;
        urunDAO = new UrunDAO(username);
        depoDAO = new DepoDAO();

        setTitle("Ürün Yönetimi");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Depo Seçimi:"));
        depoComboBox = new JComboBox<>();
        List<Depo> depolar = depoDAO.getAllDepolar();
        for (Depo depo : depolar) {
            depoComboBox.addItem(depo);
        }
        panel.add(depoComboBox);

        panel.add(new JLabel("Ürün Adı:"));
        urunAdiField = new JTextField();
        panel.add(urunAdiField);

        panel.add(new JLabel("Miktar:"));
        miktarField = new JTextField();
        panel.add(miktarField);

        ekleButton = new JButton("Ekle");
        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urunAdi = urunAdiField.getText();
                int miktar = Integer.parseInt(miktarField.getText());
                Depo selectedDepo = (Depo) depoComboBox.getSelectedItem();
                Urun urun = new Urun(urunAdi, miktar, selectedDepo.getDepoId());
                urunDAO.addOrUpdateUrun(urun);
                refreshTable();
            }
        });
        panel.add(ekleButton);

        silButton = new JButton("Sil");
        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = urunTable.getSelectedRow();
                if (selectedRow != -1) {
                    int urunId = (int) urunTableModel.getValueAt(selectedRow, 0);
                    String urunAdi = (String) urunTableModel.getValueAt(selectedRow, 1);
                    int depoId = (int) urunTableModel.getValueAt(selectedRow, 3);
                    urunDAO.deleteUrun(urunId, urunAdi, depoId);
                    refreshTable();
                }
            }
        });
        panel.add(silButton);

        transferButton = new JButton("Transfer Et");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UrunTransferi urunTransferi = new UrunTransferi(UrunYonetimi.this, username); // Ürün transferi ekranına ana ekranı ve kullanıcı adını geçiyoruz
                urunTransferi.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                urunTransferi.setVisible(true);
            }
        });
        panel.add(transferButton);

        satButton = new JButton("Sat");
        satButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urunAdi = urunAdiField.getText();
                int miktar = Integer.parseInt(miktarField.getText());
                Depo selectedDepo = (Depo) depoComboBox.getSelectedItem();
                urunDAO.sellUrun(urunAdi, miktar, selectedDepo.getDepoId());
                refreshTable();
            }
        });
        panel.add(satButton);

        add(panel, BorderLayout.NORTH);

        urunTableModel = new UrunTableModel();
        urunTable = new JTable(urunTableModel);
        add(new JScrollPane(urunTable), BorderLayout.CENTER);

        refreshTable();
    }

    public void refreshTable() {
        List<Urun> urunler = urunDAO.getAllUrunler();
        urunTableModel.setUrunler(urunler);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = "testUser"; // Test kullanıcı adı, burada giriş ekranından geçiş yapılabilir
                UrunYonetimi urunYonetimi = new UrunYonetimi(username);
                urunYonetimi.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                urunYonetimi.setVisible(true);
            }
        });
    }
}
