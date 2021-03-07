import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

	private String path;
	private String user;
	private String pass;
	private Connection conn = null;

	public DatabaseConnection(String path, String user, String pass) {
		super();
		this.path = path;
		this.user = user;
		this.pass = pass;
	}

	public boolean open() {
		try {
			conn = DriverManager.getConnection(path, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn != null) {
			return true;
		}

		return false;
	}

	public boolean close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}

	public boolean insertProizvod(int sifra, String naziv, int cijena, int sifraServisera) {
		String sql = "INSERT INTO proizvodi (sifra,nazivPredmeta,cijenaProizvoda,sifra_servisera) VALUES (" + sifra
				+ ",'" + naziv + "'," + cijena + "," + sifraServisera + ")";

		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertProdajaProizvod(int sifra) {
		String sql = "INSERT INTO proizvodi_prodaja (proizvod_id,prodaja_id) VALUES ("
				+ "(select id from proizvodi where sifra =" + sifra + "),"
				+ "(select id from prodaja where sifraProizvoda =" + sifra + "))";

		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertServiser(int sifra, String naziv, String adresa, String broj) {
		String sql = "INSERT INTO serviseri (sifraServisera,naziv,adresa,broj) VALUES (" + sifra + ",'" + naziv + "','"
				+ adresa + "','" + broj + "')";
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertProdaja(int sifraProdaje, String vrijemeProdaje, String kupac, String brojKupca,
			int sifraProizvoda, int racun) {
		String sql = "INSERT INTO prodaja (sifraProdaje,vrijemeProdaje,kupac,kontaktKupca,sifraProizvoda,iznosRacuna) VALUES ("
				+ sifraProdaje + ",'" + vrijemeProdaje + "','" + kupac + "','" + brojKupca + "'," + sifraProizvoda + ","
				+ racun + ")";

		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertProizvodi_Kategorija(int sifra, String nazivKategorije) {
		String sql = "INSERT INTO proizvodi_kategorija (proizvod_id,kategorija_id) VALUES ("
				+ "(select id from proizvodi where sifra ='" + sifra + "'),"
				+ "(select id from kategorija where nazivKategorije ='" + nazivKategorije + "'))";
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertProizvodi_serviser(int sifra_servisera, int sifra) {
		String sql = "INSERT INTO proizvodi_serviseri (proizvod_id,serviser_id) VALUES ("
				+ "(select id from proizvodi  where sifra_servisera = " + sifra_servisera + " AND sifra = " + sifra
				+ ")," + "(select id from serviseri where sifraServisera =" + sifra_servisera + "))";
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public List<String> getSveKategorija() {
		String sql = "SELECT nazivKategorije FROM kategorija";
		if (open()) {
			try {
				List<String> kategorije = new ArrayList<String>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);
				while (set.next()) {
					kategorije.add(set.getString("nazivKategorije"));
				}
				close();
				return kategorije;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;

	}

	public List<String> getAllServiser() {
		String sql = "SELECT naziv FROM serviseri";
		if (open()) {
			try {
				List<String> serviseri = new ArrayList<String>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);
				while (set.next()) {
					serviseri.add(set.getString("naziv"));
				}
				close();
				return serviseri;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	public List<Proizvodi> getProizvodi(String filter, String filter2) {
		String sql = "";

		if (filter == null && filter2 == null) {
			sql = "SELECT DISTINCT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
					+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,prodaja pr,serviseri s,proizvodi_prodaja pp,proizvodi_serviseri ps"
					+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id  AND p.sifra = pr.sifraProizvoda";

		} else if (filter == null && filter2.equalsIgnoreCase("Nista")) {
			sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
					+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_serviseri ps"
					+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id ";

		} else if (filter != null && filter2 == null) {
			String prviDio = filter.replaceAll("\\(", "").split("\\) ")[0];
			if (prviDio.equalsIgnoreCase("kategorija")) {
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_prodaja pp,prodaja po,proizvodi_serviseri ps "
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id  AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";

				if (filter != null) {
					filter = filter.replaceAll("\\(", "").split("\\) ")[1];
					sql += " AND k.nazivKategorije = '" + filter + "'";
				}

			} else if (prviDio.equalsIgnoreCase("serviser")) {
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_prodaja pp,prodaja po,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id   AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";
				if (filter != null) {
					filter = filter.replaceAll("\\(", "").split("\\) ")[1];
					sql += " AND s.naziv = '" + filter + "'";
				}

			}

		} else if (filter != null && filter2.equalsIgnoreCase("Nista")) {

			String prviDio = filter.replaceAll("\\(", "").split("\\) ")[0];

			if (prviDio.equalsIgnoreCase("kategorija")) {
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id ";
				if (filter != null) {
					filter = filter.replaceAll("\\(", "").split("\\) ")[1];
					sql += " AND k.nazivKategorije = '" + filter + "'";
				}
			} else if (prviDio.equalsIgnoreCase("serviser")) {
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id ";
				if (filter != null) {
					filter = filter.replaceAll("\\(", "").split("\\) ")[1];
					sql += " AND s.naziv = '" + filter + "'";
				}

			}

		} else if (filter != null && filter2 != null) {
			String prviDio = filter.replaceAll("\\(", "").split("\\) ")[0];
			String prviDioDrugiFilter = filter2.replaceAll("\\(", "").split("\\) ")[0];
			String drugiDio = filter.replaceAll("\\(", "").split("\\) ")[1];
			String drugiDioDrugiFilter = filter2.replaceAll("\\(", "").split("\\) ")[1];

			if (prviDio.equalsIgnoreCase("kategorija") && prviDioDrugiFilter.equalsIgnoreCase("broj")) {

				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,prodaja po,proizvodi_prodaja pp,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id  AND k.nazivKategorije = '"
						+ drugiDio + "' AND po.kontaktKupca LIKE '" + drugiDioDrugiFilter
						+ "%' AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";
			}
			if (prviDio.equalsIgnoreCase("kategorija") && prviDioDrugiFilter.equalsIgnoreCase("racun")) {
				String[] citavRacun = drugiDioDrugiFilter.split(" ");
				drugiDioDrugiFilter = citavRacun[citavRacun.length - 1];
				String veceManje = citavRacun[0];
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,prodaja po,proizvodi_prodaja pp,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id   AND k.nazivKategorije = '"
						+ drugiDio + "' AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";

				if (veceManje.equalsIgnoreCase("Veci")) {
					sql += " AND po.iznosRacuna >" + drugiDioDrugiFilter;

				} else {
					sql += " AND po.iznosRacuna <" + drugiDioDrugiFilter;
				}
			}
			if (prviDio.equalsIgnoreCase("serviser") && prviDioDrugiFilter.equalsIgnoreCase("broj")) {

				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,proizvodi_serviseri ps,prodaja po,proizvodi_prodaja pp"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id AND s.naziv = '"
						+ drugiDio + "' AND po.kontaktKupca LIKE '" + drugiDioDrugiFilter
						+ "%' AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";
			}
			if (prviDio.equalsIgnoreCase("serviser") && prviDioDrugiFilter.equalsIgnoreCase("racun")) {
				String[] citavRacun = drugiDioDrugiFilter.split(" ");
				drugiDioDrugiFilter = citavRacun[citavRacun.length - 1];
				String veceManje = citavRacun[0];
				
				sql = "SELECT p.sifra, p.nazivPredmeta, p.cijenaProizvoda,k.nazivKategorije,p.sifra_servisera,s.*"
						+ " FROM proizvodi p,kategorija k, proizvodi_kategorija pk,serviseri s,prodaja po,proizvodi_prodaja pp,proizvodi_serviseri ps"
						+ " WHERE pk.proizvod_id = p.id AND pk.kategorija_id = k.id AND p.id=ps.proizvod_id AND s.id=ps.serviser_id   AND s.naziv = '"
						+ drugiDio + "' AND p.id = pp.proizvod_id AND po.id = pp.prodaja_id";
				
				if (veceManje.equalsIgnoreCase("Veci")) {
					sql += " AND po.iznosRacuna >" + drugiDioDrugiFilter;

				} else {
					sql += " AND po.iznosRacuna <" + drugiDioDrugiFilter;
				}
			}

		}else if(filter != null && filter2 != null) {
			
		}
		if (open()) {
			try {

				List<Proizvodi> proizvodi = new ArrayList<Proizvodi>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);

				while (set.next()) {
					int sifra = Integer.parseInt(set.getString("sifra"));
					String naziv = set.getString("nazivPredmeta");
					int cijena = Integer.parseInt(set.getString("cijenaProizvoda"));
					String kategorija = set.getString("nazivKategorije");
					int sifra_servisera = Integer.parseInt(set.getString("sifra_servisera"));
					int sifraServisera = Integer.parseInt(set.getString("sifraServisera"));
					String sNaziv = set.getString("s.naziv");
					String sAdresa = set.getString("adresa");
					String broj = set.getString("broj");
					Serviser k = new Serviser(sifraServisera, sNaziv, sAdresa, broj);
					Proizvodi p = new Proizvodi(sifra, naziv, cijena, kategorija, sifra_servisera, k);
					proizvodi.add(p);
				}

				close();
				return proizvodi;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	public List<Prodaja> getProdaja(String filter) {
		String sql = "";
		if (filter == null) {
			sql = "SELECT pr.*,p.sifra" + " FROM prodaja pr,proizvodi p " + " WHERE p.sifra = pr.sifraProizvoda ";

		} else {
			String filter1 = filter.replaceAll("\\(", "").split("\\) ")[0];
			if (filter1.equalsIgnoreCase("broj")) {
				sql = "SELECT pr.*,p.sifra" + " FROM prodaja pr,proizvodi p " + " WHERE p.sifra = pr.sifraProizvoda ";
				if (filter != null) {
					filter = filter.replaceAll("\\(", "").split("\\) ")[1];
					sql += " AND pr.kontaktKupca LIKE '" + filter + "%'";

				}
			} else if (filter1.equalsIgnoreCase("racun")) {
				sql = "SELECT pr.*,p.sifra" + " FROM prodaja pr,proizvodi p " + " WHERE p.sifra = pr.sifraProizvoda ";
				if (filter != null) {
					String[] filter2 = filter.split(" ");
					filter = filter2[filter2.length - 1];
					String veceManje = filter2[1];
					if (veceManje.equalsIgnoreCase("Veci")) {
						sql += " AND pr.iznosRacuna >" + filter;
					} else {
						sql += " AND pr.iznosRacuna <" + filter;
					}

				}
			}
		}

		if (open()) {
			try {

				List<Prodaja> prodaje = new ArrayList<Prodaja>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);

				while (set.next()) {
					int sifraProdaje = Integer.parseInt(set.getString("sifraProdaje"));
					String vrijemeProdaje = set.getString("vrijemeProdaje");
					String kupac = set.getString("kupac");
					String brojKupca = set.getString("kontaktKupca");
					int sifraProizvoda = Integer.parseInt(set.getString("sifraProizvoda"));
					int racun = Integer.parseInt(set.getString("iznosRacuna"));
					Prodaja p = new Prodaja(sifraProdaje, vrijemeProdaje, kupac, brojKupca, sifraProizvoda, racun);

					prodaje.add(p);
				}

				close();
				return prodaje;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;
	}

	public boolean deleteDataFromTable(String table) {
		String sql = "DELETE FROM " + table;
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

}
