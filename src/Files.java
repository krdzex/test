import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Files {

	public static List<Proizvodi> readExcel(String path) {
		File inputWorkbook = new File(path);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet("proizvodi");
			List<Proizvodi> proizvodi = new ArrayList<Proizvodi>();

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				int sifra = Integer.parseInt(cells[0].getContents());
				String naziv = cells[1].getContents();
				int cijena = Integer.parseInt(cells[2].getContents());
				int sifraServisera =Integer.parseInt(cells[3].getContents());
				Proizvodi s = new Proizvodi(sifra, naziv, cijena, "" ,sifraServisera,null);
				proizvodi.add(s);
			}
			Sheet sheet1 = w.getSheet("kategorija");
			for (int i = 1; i < sheet1.getRows(); i++) {
				Cell[] cells = sheet1.getRow(i);
				int sifra = Integer.parseInt(cells[0].getContents());
				String kategorija = cells[1].getContents();
				
				Proizvodi p = findBySifra(sifra, proizvodi);
				if (p != null) {
					p.setKategorija(kategorija);
				}

			}

			return proizvodi;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static List<Serviser> readServiseri(String path) {
		File inputWorkbook = new File(path);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			List<Serviser> serviseri = new ArrayList<Serviser>();
			Sheet sheet2 = w.getSheet("serviseri");
			for (int i = 1; i < sheet2.getRows(); i++) {
				Cell[] cells = sheet2.getRow(i);
				int sifraServisera = Integer.parseInt(cells[0].getContents());
				String ime = cells[1].getContents();
				String adresa = cells[2].getContents();
				String broj =cells[3].getContents();
				Serviser s = new Serviser(sifraServisera, ime, adresa, broj);
				serviseri.add(s);
			}
			return serviseri;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Prodaja> readProdaja(String path) {
		File inputWorkbook = new File(path);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			List<Prodaja> prodaje = new ArrayList<Prodaja>();
			Sheet sheet2 = w.getSheet("prodaja");
			for (int i = 1; i < sheet2.getRows(); i++) {
				Cell[] cells = sheet2.getRow(i);
				int sifraProdaje = Integer.parseInt(cells[0].getContents());
				String vrijemeProdaje = cells[1].getContents();
				String kupac = cells[2].getContents();
				String brojKupca = cells[3].getContents();
				int sifraProizvoda = Integer.parseInt(cells[4].getContents());
				int racun = Integer.parseInt(cells[5].getContents());
				
				Prodaja s = new Prodaja(sifraProdaje, vrijemeProdaje, kupac, brojKupca,sifraProizvoda,racun);
				prodaje.add(s);
			}
			return prodaje;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Proizvodi findBySifra(int sifra, List<Proizvodi> proizvodi) {
		for (Proizvodi p : proizvodi) {
			if (p.getSifra() == sifra) {
				return p;
			}
		}
		return null;

	}
	
	
	public static boolean kreirajJSONZaProizvode(String naziv, List<Proizvodi> proizvodi) {

		JSONArray list1 = new JSONArray();

		for (Proizvodi p : proizvodi) {
			JSONObject jO = new JSONObject();
			jO.put("naziv", p.getNaziv());
			jO.put("cijena", p.getCijena());
			jO.put("kategorija", p.getKategorija());
			jO.put("sifra", p.getSifra());
			jO.put("sifraServisera", p.getSifraServisera());
			jO.put("serviseri", p.getServiseri());
			list1.add(jO);
			
		}

		try {

			FileWriter file = new FileWriter(naziv + ".json");
			file.write(list1.toJSONString());
			file.flush();
			file.close();
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;

	}
	
	public static boolean kreirajJSONZaProdaju(String naziv, List<Prodaja> prodaje) {

		JSONArray list2 = new JSONArray();

		for (Prodaja p : prodaje) {
			JSONObject jO = new JSONObject();
			jO.put("sifraProdaje", p.getSifraProdaje());
			jO.put("vrijemeProdaje", p.getVrijemeProdaje());
			jO.put("kupac", p.getKupac());
			jO.put("brojKupca", p.getBrojKupca());
			jO.put("sifraProizvoda", p.getSifraProizvoda());
			jO.put("racun", p.getRacun());
			list2.add(jO);
			
		}

		try {

			FileWriter file = new FileWriter(naziv + ".json");
			file.write(list2.toJSONString());
			file.flush();
			file.close();
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;

	}
	
	public static boolean kreirajPDFZaProizvode(String naziv, List<Proizvodi> proizvodi) {
		com.itextpdf.text.Document d = new com.itextpdf.text.Document();
		try {
			PdfWriter.getInstance(d, new FileOutputStream(new File(naziv
					+ ".pdf")));
			d.open();
			Font headerFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLUE);
			Font titleFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.RED);
			Font textFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLACK);

			Paragraph p1 = new Paragraph("Proizvodi", titleFont);
			p1.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			d.add(p1);
			d.add(new Paragraph(" "));
			kreiranjeTabeleZaProizvode(d, textFont, headerFont, proizvodi);
			d.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}
	
	public static boolean kreirajPDFZaProdaju(String naziv, List<Prodaja> prodaje) {
		com.itextpdf.text.Document d = new com.itextpdf.text.Document();
		try {
			PdfWriter.getInstance(d, new FileOutputStream(new File(naziv
					+ ".pdf")));
			d.open();
			Font headerFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLUE);
			Font titleFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.RED);
			Font textFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLACK);

			Paragraph p1 = new Paragraph("Prodaje", titleFont);
			p1.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			d.add(p1);
			d.add(new Paragraph(" "));
			kreiranjeTabeleZaProdaju(d, textFont, headerFont, prodaje);
			d.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}
	private static void kreiranjeTabeleZaProdaju(com.itextpdf.text.Document d,
			Font textFont, Font headerFont, List<Prodaja> prodaje)
			throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		PdfPCell c1 = new PdfPCell(new Phrase("Sifra prodaje",headerFont));
		table.addCell(c1);

		PdfPCell c2 = new PdfPCell(new Phrase("Vrijeme prodaje", headerFont));
		table.addCell(c2);

		PdfPCell c3 = new PdfPCell(new Phrase("Ime kupca", headerFont));
		table.addCell(c3);

		PdfPCell c4 = new PdfPCell(new Phrase("Broj kupca", headerFont));
		table.addCell(c4);
		
		PdfPCell c5 = new PdfPCell(new Phrase("Sifra proizvoda", headerFont));
		table.addCell(c5);

		PdfPCell c6 = new PdfPCell(new Phrase("Racun", headerFont));
		table.addCell(c6);
		
		d.add(new Paragraph(" "));

		for (Prodaja p : prodaje) {

			table.addCell(new Phrase(String.valueOf(p.getSifraProdaje()),textFont));
			table.addCell(new Phrase(p.getVrijemeProdaje(), textFont));
			table.addCell(new Phrase(String.valueOf(p.getKupac())+"$",textFont));
			table.addCell(new Phrase(p.getBrojKupca(), textFont));
			table.addCell(new Phrase(String.valueOf(p.getSifraProizvoda()),textFont));
			table.addCell(new Phrase(String.valueOf(p.getRacun()),textFont));

		}

		d.add(table);
	}
	
	
	
	private static void kreiranjeTabeleZaProizvode(com.itextpdf.text.Document d,
			Font textFont, Font headerFont, List<Proizvodi> proizvodi)
			throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		PdfPCell c1 = new PdfPCell(new Phrase("Sifra proizvoda",headerFont));
		table.addCell(c1);

		PdfPCell c2 = new PdfPCell(new Phrase("Naziv proizvoda", headerFont));
		table.addCell(c2);

		PdfPCell c3 = new PdfPCell(new Phrase("Cijena proizvoda", headerFont));
		table.addCell(c3);

		PdfPCell c4 = new PdfPCell(new Phrase("Kategorija proizvoda", headerFont));
		table.addCell(c4);
		
		PdfPCell c5 = new PdfPCell(new Phrase("Naziv servisera", headerFont));
		table.addCell(c5);

		PdfPCell c6 = new PdfPCell(new Phrase("Broj servisera", headerFont));
		table.addCell(c6);
		
		d.add(new Paragraph(" "));

		for (Proizvodi p : proizvodi) {

			table.addCell(new Phrase(String.valueOf(p.getSifra()),textFont));
			table.addCell(new Phrase(p.getNaziv(), textFont));
			table.addCell(new Phrase(String.valueOf(p.getCijena())+"$",textFont));
			table.addCell(new Phrase(p.getKategorija(), textFont));
			table.addCell(new Phrase(p.getServiseri().getIme(),textFont));
			table.addCell(new Phrase(p.getServiseri().getBroj(),textFont));

		}

		d.add(table);
	}
	
	public static boolean kreirajXMLZaProizvode(String naziv, List<Proizvodi> proizvodi) {
		Document d = DocumentHelper.createDocument();
		Element root = d.addElement("Popis_proizvoda");
		for (Proizvodi p : proizvodi) {
			Element proizvod = root.addElement("Proizvod");
			Element sifraProizvoda = proizvod.addElement("SifraProizvoda");
			sifraProizvoda.setText(String.valueOf(p.getSifra()));
			Element nazivProizvoda = proizvod.addElement("Naziv");
			nazivProizvoda.setText(p.getNaziv());
			Element cijena = proizvod.addElement("Cijena");
			cijena.setText(String.valueOf(p.getCijena()));
			Element kategorija = proizvod.addElement("Kategorija");
			kategorija.setText(p.getKategorija());
			Element serviser = proizvod.addElement("Serviser");
			serviser.setText(p.getServiseri().getIme());
		}

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(naziv + ".xml"), "UTF8"), format);
			writer.write(d);
			writer.close();
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;

	}
	
	public static boolean kreirajXMLzaProdaju(String naziv, List<Prodaja> prodaje) {
		Document d = DocumentHelper.createDocument();
		Element root = d.addElement("Popis_prodaja");
		for (Prodaja p : prodaje) {
			Element prodaja = root.addElement("Prodaja");
			Element sifraProdaje = prodaja.addElement("SifraProdaje");
			sifraProdaje.setText(String.valueOf(p.getSifraProdaje()));
			Element vrijemeProdaje = prodaja.addElement("VrijemeProdaje");
			vrijemeProdaje.setText(p.getVrijemeProdaje());
			Element kupac = prodaja.addElement("Kupac");
			kupac.setText(p.getKupac());
			Element brojKupca = prodaja.addElement("BrojKupca");
			brojKupca.setText(p.getBrojKupca());
			Element sifraProizvoda = prodaja.addElement("SifraProizvoda");
			sifraProizvoda.setText(String.valueOf(p.getSifraProizvoda()));
			Element racun = prodaja.addElement("Racun");
			racun.setText(String.valueOf(p.getRacun()));
		}

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(naziv + ".xml"), "UTF8"), format);
			writer.write(d);
			writer.close();
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;

	}
	
	public static String validacijaProizvoda(String xml) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(
							"sema_popisa_proizvoda.xsd") }));

			SAXParser parser = factory.newSAXParser();
			SAXReader reader = new SAXReader(parser.getXMLReader());

			try {
				reader.read(new File(xml));
				return "Validacija uspjesna. ";
			} catch (Exception e) {
				return "Validacija nije uspjela. " + e.getMessage();
			}

		} catch (ParserConfigurationException | SAXException e) {
		}
		return "Greska u validaciji.";

	}
	public static String validacijaProdaje(String xml) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(
							"sema_popisa_prodaje.xsd") }));

			SAXParser parser = factory.newSAXParser();
			SAXReader reader = new SAXReader(parser.getXMLReader());

			try {
				reader.read(new File(xml));
				return "Validacija uspjesna. ";
			} catch (Exception e) {
				return "Validacija nije uspjela. " + e.getMessage();
			}

		} catch (ParserConfigurationException | SAXException e) {
		}
		return "Greska u validaciji.";

	}
	
}
