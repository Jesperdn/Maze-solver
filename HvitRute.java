
import java.util.ArrayList;

class HvitRute extends Rute {

	public HvitRute(int x, int y, Labyrint l) {
		super(x,y,l);	// bruker konstruktør fra Rute
	}

	/** Går til naboer
	 * @param veiHit veien som er gått så langt
	 * @param detaljert boolean for detaljert utskrift
	 */
	public void gaa(ArrayList<Tuppel> veiHit, boolean detaljert) {
		if (detaljert) System.out.println(hentKoordinater());

		Tuppel denne = new Tuppel(x,y);	// Sjekker om tuppelet har vært
		for (Tuppel t : veiHit) {		// besøkt på denne veien før.
			if (t.erLik(denne)) return;	// Går i så fall tilbake
		}

		ArrayList<Tuppel> veiHerfra = new ArrayList<>(veiHit);	// Kopierer veien 
		veiHerfra.add(denne);	// Legger til denne ruta


		if (veiHerfra.size() > 1) {	// Finnes det noen tupler i ruta fra før?
			Tuppel forrigeRute = veiHit.get(veiHerfra.size()-2);	// Hent den forrige ruta
			for (Rute nabo : naboer) {	// Går gjennom naboene til dennne ruta
				if (!(nabo.hentKoordinater().equals(forrigeRute.hentKoordinater()))) {	// Hvis naboen ikke er forrige rute
					nabo.gaa(veiHerfra, detaljert);	// Gå til den naboen
				}
			}
		} else {	// Hvis veiHerfra er tom
			for (Rute nabo : naboer) {			//
				nabo.gaa(veiHerfra, detaljert);	// Gå til alle naboene
			}
		}
	}

	@Override
	public String charTilTegn() {
		return " ";
	}

}
