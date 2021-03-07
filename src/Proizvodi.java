
public class Proizvodi {

	private String naziv;
	private int cijena;
	private String kategorija;
	private int sifra;
	private int sifraServisera;
	private Serviser serviseri;

	public Proizvodi(int sifra,String naziv, int cijena, String kategorija,int sifraServisera,Serviser serviseri) {
		super();
		this.naziv = naziv;
		this.cijena = cijena;
		this.kategorija = kategorija;
		this.sifra = sifra;
		this.sifraServisera = sifraServisera;
		this.serviseri = serviseri;
	}
	

	public int getSifraServisera() {
		return sifraServisera;
	}


	public void setSifraServisera(int sifraServisera) {
		this.sifraServisera = sifraServisera;
	}


	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getCijena() {
		return cijena;
	}

	public void setCijena(int cijena) {
		this.cijena = cijena;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public int getSifra() {
		return sifra;
	}

	public void setSifra(int sifra) {
		this.sifra = sifra;
	}

	@Override
	public String toString() {
		return "Proizvodi: naziv=" + naziv + ", cijena=" + cijena + ", kategorija=" + kategorija + ", sifra=" + sifra + "serviseri:" + serviseri;
	}


	public Serviser getServiseri() {
		return serviseri;
	}


	public void setServiseri(Serviser serviseri) {
		this.serviseri = serviseri;
	}

}
