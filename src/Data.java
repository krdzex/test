
import java.util.List;

public class Data {

	static DatabaseConnection d = new DatabaseConnection("jdbc:mysql://localhost:3306/xml_tehnomax", "root", "");

	public static List<String> getKategorija() {
		return d.getSveKategorija();
	}

	public static List<String> getServiser() {
		return d.getAllServiser();
	}

	public static boolean importData(String path) {
		List<Proizvodi> proizvodi = Files.readExcel(path);
		List<Serviser> serviseri = Files.readServiseri(path);
		List<Prodaja> prodaje = Files.readProdaja(path);
		
	/*	for(Proizvodi p : proizvodi) {
			for(Serviser s : serviseri) {
				if(p.getSifraServisera()==s.getSifra()) {
					p.setServiseri(s);

				}
			}
		}*/
		if (serviseri != null) {
			for (Serviser s : serviseri) {
				boolean dodaj = d.insertServiser(s.getSifra(), s.getIme(), s.getAdresa(), s.getBroj());

				if (!dodaj) {
					return false;
				}
			}
		}
		if (proizvodi != null) {

			for (Proizvodi p : proizvodi) {
				boolean ok = d.insertProizvod(p.getSifra(), p.getNaziv(), p.getCijena(), p.getSifraServisera());
				boolean ok1 = d.insertProizvodi_Kategorija(p.getSifra(), p.getKategorija());
				boolean ok2 = d.insertProizvodi_serviser(p.getSifraServisera(),p.getSifra());

				if (!ok || !ok1 || !ok2
						) {
					return false;
				}
			}
		}
		
		if (prodaje != null) {
			for (Prodaja pr : prodaje) {
				boolean dodaj = d.insertProdaja(pr.getSifraProdaje(), pr.getVrijemeProdaje(), pr.getKupac(),
						pr.getBrojKupca(), pr.getSifraProizvoda(), pr.getRacun());
				boolean ok2 = d.insertProdajaProizvod(pr.getSifraProizvoda());
				if (!dodaj || !ok2) {
					return false;
				}
			}
		}

		return true;
	}
	
	

	public static List<Proizvodi> getProizvodi(String filter, String filter2) {
		if (filter.equalsIgnoreCase("SVI")) {

			filter = null;
		}
		return d.getProizvodi(filter, filter2);

	}

	public static List<Prodaja> getProdaja(String filter) {
		if (filter.equalsIgnoreCase("SVI")) {

			filter = null;
		}
		return d.getProdaja(filter);
	}

	public static Object[][] getProizvodiForTable(String filter1, String filter2) {

		if (filter1.equalsIgnoreCase("SVI")) {
			filter1 = null;
		}
		if (filter2.equalsIgnoreCase("SVI")) {
			filter2 = null;
		}
		List<Proizvodi> proizvodi = d.getProizvodi(filter1, filter2);
		if (proizvodi != null) {
			Object[][] objects = new Object[proizvodi.size()][4];
			int i = 0;
			for (Proizvodi p : proizvodi) {
				Object[] o = { p.getSifra(), p.getNaziv(), p.getCijena(), p.getKategorija(),
						p.getServiseri().getIme() };
				objects[i] = o;
				i++;
			}

			return objects;
		}
		return null;
	}

	public static Object[][] getProdajaForTable(String kategorija) {

		if (kategorija.equalsIgnoreCase("SVI")) {
			kategorija = null;
		}
		List<Prodaja> prodaje = d.getProdaja(kategorija);
		if (prodaje != null) {
			Object[][] objects = new Object[prodaje.size()][4];
			int i = 0;
			for (Prodaja p : prodaje) {
				Object[] o = { p.getSifraProdaje(), p.getVrijemeProdaje(), p.getKupac(), p.getBrojKupca(),
						p.getSifraProizvoda(), p.getRacun() };
				objects[i] = o;
				i++;
			}

			return objects;
		}
		return null;
	}

	public static boolean deleteAll() {
		boolean ok = d.deleteDataFromTable("proizvodi_serviseri");
		boolean ok1 = d.deleteDataFromTable("proizvodi_prodaja");
		boolean ok2 = d.deleteDataFromTable("proizvodi_kategorija");
		boolean ok3 = d.deleteDataFromTable("proizvodi");
		boolean ok4 = d.deleteDataFromTable("serviseri");
		boolean ok5 = d.deleteDataFromTable("prodaja");
	
		
		

		if (!ok || !ok1 || !ok2 || !ok3 || !ok4 || !ok5) {
			return false;
		}
		return true;
	}
}
