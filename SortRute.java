
import java.util.ArrayList;

class SortRute extends Rute {

	public SortRute(int x, int y, Labyrint l) {
		super(x,y,l);	// Bruker konstruktør fra Rute
	}

	@Override
	public String charTilTegn() {
		return "|";
	}

	/**
	 * @param veiHit veien som er gått så langt
	 * @param d boolean for detaljert utskrift
	 */
	@Override
	public void gaa(ArrayList<Tuppel> veiHit, boolean d) {
		if (d) System.out.println("Stanget i en vegg");
	}
}
