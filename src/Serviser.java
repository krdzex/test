
public class Serviser {

	private int sifra;
	private String ime;
	private String adresa;
	private String broj;
	
	public int getSifra() {
		return sifra;
	}
	public void setSifra(int sifra) {
		this.sifra = sifra;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getBroj() {
		return broj;
	}
	public void setBroj(String broj) {
		this.broj = broj;
	}
	public Serviser(int sifra, String ime, String adresa, String broj) {
		super();
		this.sifra = sifra;
		this.ime = ime;
		this.adresa = adresa;
		this.broj = broj;
	}
	@Override
	public String toString() {
		return "Serviser [sifra=" + sifra + ", ime=" + ime + ", adresa=" + adresa + ", broj=" + broj + "]";
	}
	
	
}
