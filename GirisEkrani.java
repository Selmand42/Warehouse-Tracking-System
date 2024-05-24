package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GirisEkrani extends JFrame {
    private JTextField kullaniciAdiField;
    private JPasswordField sifreField;
    private JButton girisButton;
    private KullaniciDAO kullaniciDAO;

    public GirisEkrani() {
        kullaniciDAO = new KullaniciDAO();

        setTitle("Giriş Ekranı");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Kullanıcı Adı:"));
        kullaniciAdiField = new JTextField();
        add(kullaniciAdiField);

        add(new JLabel("Şifre:"));
        sifreField = new JPasswordField();
        add(sifreField);

        girisButton = new JButton("Giriş");
        girisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullaniciAdi = kullaniciAdiField.getText();
                String sifre = new String(sifreField.getPassword());
                Kullanici kullanici = kullaniciDAO.getKullaniciByKullaniciAdiVeSifre(kullaniciAdi, sifre);
                if (kullanici != null) {
                    if ("yonetici".equals(kullanici.getRol())) {
                        KullaniciYonetimi kullaniciYonetimi = new KullaniciYonetimi();
                        kullaniciYonetimi.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                        kullaniciYonetimi.setVisible(true);
                    } else {
                        UrunYonetimi urunYonetimi = new UrunYonetimi(kullaniciAdi);
                        urunYonetimi.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                        urunYonetimi.setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(GirisEkrani.this, "Geçersiz kullanıcı adı veya şifre", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(girisButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GirisEkrani girisEkrani = new GirisEkrani();
                girisEkrani.setLocationRelativeTo(null); // Pencereyi ekranın ortasında açar
                girisEkrani.setVisible(true);
            }
        });
    }
}

