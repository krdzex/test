
public class Prodaja {
private int sifraProdaje;
private String vrijemeProdaje;
private String kupac;
private String brojKupca;
private int sifraProizvoda;
private int racun;
public int getSifraProdaje() {
	return sifraProdaje;
}
public void setSifraProdaje(int sifraProdaje) {
	this.sifraProdaje = sifraProdaje;
}
public String getVrijemeProdaje() {
	return vrijemeProdaje;
}
public void setVrijemeProdaje(String vrijemeProdaje) {
	this.vrijemeProdaje = vrijemeProdaje;
}
public String getKupac() {
	return kupac;
}
public void setKupac(String kupac) {
	this.kupac = kupac;
}
public String getBrojKupca() {
	return brojKupca;
}
public void setBrojKupca(String brojKupca) {
	this.brojKupca = brojKupca;
}
public int getSifraProizvoda() {
	return sifraProizvoda;
}
public void setSifraProizvoda(int sifraProizvoda) {
	this.sifraProizvoda = sifraProizvoda;
}
public int getRacun() {
	return racun;
}
public void setRacun(int racun) {
	this.racun = racun;
}
public Prodaja(int sifraProdaje, String vrijemeProdaje, String kupac, String brojKupca, int sifraProizvoda, int racun) {
	super();
	this.sifraProdaje = sifraProdaje;
	this.vrijemeProdaje = vrijemeProdaje;
	this.kupac = kupac;
	this.brojKupca = brojKupca;
	this.sifraProizvoda = sifraProizvoda;
	this.racun = racun;
}
@Override
public String toString() {
	return "Prodaja [sifraProdaje=" + sifraProdaje + ", vrijemeProdaje=" + vrijemeProdaje + ", kupac=" + kupac
			+ ", brojKupca=" + brojKupca + ", sifraProizvoda=" + sifraProizvoda + ", racun=" + racun + "]";
}


}
