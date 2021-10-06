
import java.util.ArrayList;

abstract class Rute {

	private Labyrint labyrint;	// Labyrinten ruten er en del av
	protected int x;	//
	protected int y;	// Koordinater
	protected Rute[] naboer; // Rutens naboer
	protected static ArrayList<Tuppel> veiHerfra;	// Veien til åpningen

	public Rute(int x, int y, Labyrint labyrint) {
		this.x = x;
		this.y = y;
		this.labyrint = labyrint;
		this.naboer = null;
	}

	public abstract String charTilTegn();
	public abstract void gaa(ArrayList<Tuppel> veiHit, boolean d);

	/** Setter naboer for ruten
	 * @param naboer Alle naboene til denne ruten
	 */
	public void settNaboer(Rute[] naboer) {
		this.naboer = naboer;
	}

	public String toString() {
		return "("+x+", "+y+")";
	}

	public String hentKoordinater() {
		return "("+x+", "+y+")";
	}

	/** Finner utvei fra denne ruten
	 * @param detaljert boolean for detaljert visning
	 */
	public void finnUtvei(boolean detaljert) {
		veiHerfra = new ArrayList<>();	// Lag en ny ArrayList som holder på utveien
		this.gaa(veiHerfra, detaljert);	// Gaa fra denne ruten
	}
}
