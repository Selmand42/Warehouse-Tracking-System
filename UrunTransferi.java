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

public class UrunTransferi extends JFrame {
    private JComboBox<Depo> kaynakDepoComboBox;
    private JComboBox<Depo> hedefDepoComboBox;
    private JTextField urunAdiField;
    private JTextField miktarField;
    private JButton transferButton;
    private UrunDAO urunDAO;
    private DepoDAO depoDAO;
    private UrunYonetimi urunYonetimi;
    private String username;

    public UrunTransferi(UrunYonetimi urunYonetimi, String username) {
        this.urunYonetimi = urunYonetimi;
        this.username = username;
        urunDAO = new UrunDAO(username);
        depoDAO = new DepoDAO();

        setTitle("Ürün Transferi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // JFrame.EXIT_ON_CLOSE yerine DISPOSE_ON_CLOSE kullanıldı
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Kaynak Depo:"));
        kaynakDepoComboBox = new JComboBox<>();
        List<Depo> depolar = depoDAO.getAllDepolar();
        for (Depo depo : depolar) {
            kaynakDepoComboBox.addItem(depo);
        }
        add(kaynakDepoComboBox);

        add(new JLabel("Hedef Depo:"));
        hedefDepoComboBox = new JComboBox<>();
        for (Depo depo : depolar) {
            hedefDepoComboBox.addItem(depo);
        }
        add(hedefDepoComboBox);

        add(new JLabel("Ürün Adı:"));
        urunAdiField = new JTextField();
        add(urunAdiField);

        add(new JLabel("Miktar:"));
        miktarField = new JTextField();
        add(miktarField);

        transferButton = new JButton("Transfer Et");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urunAdi = urunAdiField.getText();
                int miktar = Integer.parseInt(miktarField.getText());
                Depo kaynakDepo = (Depo) kaynakDepoComboBox.getSelectedItem();
                Depo hedefDepo = (Depo) hedefDepoComboBox.getSelectedItem();

                if (kaynakDepo.getDepoId() == hedefDepo.getDepoId()) {
                    JOptionPane.showMessageDialog(UrunTransferi.this, "Kaynak ve hedef depo aynı olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                } else {
                    urunDAO.transferUrun(urunAdi, miktar, kaynakDepo.getDepoId(), hedefDepo.getDepoId());
                    JOptionPane.showMessageDialog(UrunTransferi.this, "Ürün başarıyla transfer edildi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    urunYonetimi.refreshTable(); // Ürün yönetimi tablosunu güncelle
                }
            }
        });
        add(transferButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UrunYonetimi urunYonetimi = new UrunYonetimi("testUser"); // Test kullanıcı adı, burada giriş ekranından geçiş yapılabilir
                UrunTransferi urunTransferi = new UrunTransferi(urunYonetimi, "testUser"); // Ürün transferi ekranı
                urunTransferi.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                urunTransferi.setVisible(true);
            }
        });
    }
}
