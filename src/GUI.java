import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JFrame frame;
	private JTable table;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JComboBox comboBox;
	private JFileChooser fc;
	private JButton btnImport;
	private JButton btnObrisi;
	private JComboBox comboBox_1;
	private JLabel lblProizvod;
	private JLabel lblProdaja;
	private JButton napraviJson;
	private JTextField textField;
	private JButton btnNapraviPdf;
	private JButton btnNapraviXml;
	private JLabel lblUnesiteImeFajla;
	private JLabel lblPrikaz;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getShow());
		frame = this;
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx");
		fc.setFileFilter(filter);
		table = new JTable();
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(23, 160, 747, 276);
		frame.getContentPane().add(scroll);
		contentPane.add(getComboBox());
		contentPane.add(getBtnImport());
		contentPane.add(getBtnObrisi());
		contentPane.add(getComboBox_1());
		contentPane.add(getLblProizvod());
		contentPane.add(getLblProdaja());
		contentPane.add(getNapraviJson());
		contentPane.add(getTextField());
		contentPane.add(getBtnNapraviPdf());
		contentPane.add(getBtnNapraviXml());
		contentPane.add(getLblUnesiteImeFajla());
		contentPane.add(getLblPrikaz());
	}

	private JButton getShow() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Prikazi");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {

						Object[][] proizvodi = Data.getProizvodiForTable(comboBox.getSelectedItem().toString(),
								comboBox_1.getSelectedItem().toString());

						Object[] columnNames = { "Sifra", "Naziv", "Cijena", "Kategorija", "Serviser" };

						DefaultTableModel dtm = new DefaultTableModel(proizvodi, columnNames);
						table.setModel(dtm);
					}

					if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& !comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {

						Object[][] prodaje = Data.getProdajaForTable(comboBox_1.getSelectedItem().toString());

						Object[] columnNames = { "sifraProdaje", "vrijemeProdaje", "kupac", "kontaktKupca",
								"sifraProizvoda", "iznosRacuna" };

						DefaultTableModel dtm = new DefaultTableModel(prodaje, columnNames);
						table.setModel(dtm);
					}

					if (!comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& !comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {

						Object[][] proizvodi = Data.getProizvodiForTable(comboBox.getSelectedItem().toString(),
								comboBox_1.getSelectedItem().toString());

						Object[] columnNames = { "Sifra", "Naziv", "Cijena", "Kategorija", "Serviser" };

						DefaultTableModel dtm = new DefaultTableModel(proizvodi, columnNames);
						table.setModel(dtm);
					}

				}
			});

			btnNewButton.setBounds(43, 51, 97, 25);
		}
		return btnNewButton;
	}

	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
			comboBox.setBounds(184, 52, 147, 22);
			List<String> list = Data.getKategorija();
			comboBox.addItem("Nista");
			comboBox.addItem("SVI");
			if (list != null) {
				for (String string : list) {
					comboBox.addItem("(kategorija) " + string);
				}

			}
			List<String> listServiseri = Data.getServiser();
			if (list != null) {
				for (String string : listServiseri) {
					comboBox.addItem("(serviser) " + string);
				}
			}
		}
		return comboBox;
	}

	private JComboBox getComboBox_1() {
		if (comboBox_1 == null) {
			comboBox_1 = new JComboBox();
			comboBox_1.setBounds(373, 52, 180, 22);
			comboBox_1.addItem("Nista");
			comboBox_1.addItem("SVI");
			comboBox_1.addItem("(broj) 067");
			comboBox_1.addItem("(broj) 068");
			comboBox_1.addItem("(broj) 069");
			comboBox_1.addItem("(racun) Veci od 500");
			comboBox_1.addItem("(racun) Manji od 500");

		}
		return comboBox_1;
	}

	private JButton getBtnImport() {
		if (btnImport == null) {
			btnImport = new JButton("import");
			btnImport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fc.showOpenDialog(frame);
					boolean ok = Data.importData(fc.getSelectedFile().getPath());
					if (ok) {
						JOptionPane.showMessageDialog(frame, "Uspjesan import.");
					} else {
						JOptionPane.showMessageDialog(frame, "Greska u importu.", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			btnImport.setBounds(706, 51, 97, 25);
		}
		return btnImport;
	}

	private JButton getBtnObrisi() {
		if (btnObrisi == null) {
			btnObrisi = new JButton("Obrisi");
			btnObrisi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean ok = Data.deleteAll();
					if (ok) {
						JOptionPane.showMessageDialog(frame, "Uspjesno.");
					} else {
						JOptionPane.showMessageDialog(frame, "Greska u brisanju.", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			btnObrisi.setBounds(706, 13, 97, 25);
		}
		return btnObrisi;
	}

	private JLabel getLblProizvod() {
		if (lblProizvod == null) {
			lblProizvod = new JLabel("Proizvod:");
			lblProizvod.setBounds(184, 27, 56, 16);
		}
		return lblProizvod;
	}

	private JLabel getLblProdaja() {
		if (lblProdaja == null) {
			lblProdaja = new JLabel("Prodaja:");
			lblProdaja.setBounds(373, 27, 56, 16);
		}
		return lblProdaja;
	}

	private JButton getNapraviJson() {
		if (napraviJson == null) {
			napraviJson = new JButton("Napravi JSON");
			napraviJson.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean ok = false;
					String ime = textField.getText();
					if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& !comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {
						List<Prodaja> prodaje = Data.getProdaja(comboBox_1.getSelectedItem().toString());
						ok = Files.kreirajJSONZaProdaju("pdf/" + ime, prodaje);
					} else {
						List<Proizvodi> proizvodi = Data.getProizvodi(comboBox.getSelectedItem().toString(),
								comboBox_1.getSelectedItem().toString());
						ok = Files.kreirajJSONZaProizvode("json/" + ime, proizvodi);
					}
					if (ok) {
						JOptionPane.showMessageDialog(frame, "Uspjesan eksport. ");
					} else {
						JOptionPane.showMessageDialog(frame, "Neuspjesan eksport. ");
					}
				}
			});
			napraviJson.setBounds(575, 13, 120, 25);
		}
		return napraviJson;
	}

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setBounds(232, 115, 197, 22);
			textField.setColumns(10);
		}
		return textField;
	}

	private JButton getBtnNapraviPdf() {
		if (btnNapraviPdf == null) {
			btnNapraviPdf = new JButton("Napravi pdf");
			btnNapraviPdf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean ok = false;
					String ime = textField.getText();
					if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& !comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {
						List<Prodaja> prodaje = Data.getProdaja(comboBox_1.getSelectedItem().toString());
						ok = Files.kreirajPDFZaProdaju("pdf/" + ime, prodaje);
					} else {
						List<Proizvodi> proizvodi = Data.getProizvodi(comboBox.getSelectedItem().toString(),
								comboBox_1.getSelectedItem().toString());
						ok = Files.kreirajPDFZaProizvode("pdf/" + ime, proizvodi);
					}

					if (ok) {
						JOptionPane.showMessageDialog(frame, "Uspjesan eksport. ");
					} else {
						JOptionPane.showMessageDialog(frame, "Neuspjesan eksport. ");
					}
				}
			});
			btnNapraviPdf.setBounds(574, 51, 120, 25);
		}
		return btnNapraviPdf;
	}

	private JButton getBtnNapraviXml() {
		if (btnNapraviXml == null) {
			btnNapraviXml = new JButton("Napravi xml");
			btnNapraviXml.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					boolean ok = false;
					boolean ok2 = false;

					String ime = textField.getText();
					if (comboBox.getSelectedItem().toString().equalsIgnoreCase("Nista")
							&& !comboBox_1.getSelectedItem().toString().equalsIgnoreCase("Nista")) {
						List<Prodaja> prodaje = Data.getProdaja(comboBox_1.getSelectedItem().toString());
						ok2 = Files.kreirajXMLzaProdaju("xml/" + ime, prodaje);
					} else {
						List<Proizvodi> proizvodi = Data.getProizvodi(comboBox.getSelectedItem().toString(),
								comboBox_1.getSelectedItem().toString());
						ok = Files.kreirajXMLZaProizvode("xml/" + ime, proizvodi);
					}
					
					if (ok && ok2 == false) {
						String poruka = Files.validacijaProizvoda("xml/" + ime + ".xml");
						JOptionPane.showMessageDialog(frame, "Uspjesan eksport proizvoda." + poruka);
					} else if(ok == false && ok2 == false){
						JOptionPane.showMessageDialog(frame, "Neuspjesan eksport. ");
					}else if(ok == false && ok2) {
						String poruka = Files.validacijaProdaje("xml/" + ime + ".xml");
						JOptionPane.showMessageDialog(frame, "Uspjesan eksport prodaje." + poruka);
					}
				}
			});
			btnNapraviXml.setBounds(575, 89, 120, 25);
		}
		return btnNapraviXml;
	}
	private JLabel getLblUnesiteImeFajla() {
		if (lblUnesiteImeFajla == null) {
			lblUnesiteImeFajla = new JLabel("Unesite ime fajla za eksport:");
			lblUnesiteImeFajla.setBounds(40, 105, 180, 42);
		}
		return lblUnesiteImeFajla;
	}
	private JLabel getLblPrikaz() {
		if (lblPrikaz == null) {
			lblPrikaz = new JLabel("Prikaz:");
			lblPrikaz.setBounds(54, 27, 56, 16);
		}
		return lblPrikaz;
	}
}
