
import java.util.ArrayList;

public class Aapning extends HvitRute {

	private Labyrint labyrint;	// Labyrinten 
	protected ArrayList<Tuppel> veiTilAapning;	// Veien hit

	public Aapning(int x, int y, Labyrint l) {
		super(x,y,l);	// bruker konstruktør i HvitRute
		this.labyrint = l;	
	}

	/** gaa()
	 * 
	 * @param veiHit veien som er gått så langt
	 * @param d true/false for detaljert utskrift
	 */
	@Override
	public void gaa(ArrayList<Tuppel> veiHit, boolean d) {
		if (d) System.out.println("Fant en åpning!");

		veiTilAapning = new ArrayList<>(veiHit);	// kopierer veien hit.
		veiTilAapning.add(new Tuppel(x,y));			// legger til denne ruta.
		labyrint.leggTilUtvei(veiTilAapning);		// kaller på metode fra labyrint for å legge til hele utveien.
	}
}
