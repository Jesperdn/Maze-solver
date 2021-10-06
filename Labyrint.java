
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class Labyrint {

	private static Rute[][] labyrint;		// Labyrinten
	private static int antAapninger = 0;	// Antall åpninger i labyrinten
	private int antRader, antKolonner;		// Antall rader og kolonner
	private File fil;						// Filen som skal leses inn
	protected static ArrayList<ArrayList<Tuppel>> utveier; // Alle utveier
	protected static int kortestIndex;

	public Labyrint(File fil)		//
	throws FileNotFoundException {	// Kaster exception videre
		Scanner scanner = new Scanner(fil);	// Scanner

		String[] forste_data = scanner.nextLine().split(" ");	//
		antRader = Integer.parseInt(forste_data[0]);			// finner rad og
		antKolonner = Integer.parseInt(forste_data[1]);			// kolonne
		labyrint = new Rute[antRader][antKolonner];				// oppretter array
		utveier = new ArrayList<>();

		int rad = 0; //starter i rad 0
		while (scanner.hasNextLine()) {
			int kol = 0;	// starter i kolonne 0
			String[] biter_fra_linje = scanner.nextLine().split("");

			Rute[] denne_raden = new Rute[antKolonner]; // Lager hele raden på en gang

			for (String bit : biter_fra_linje) {
				Rute rute = null;
				if (bit.equals(".")) {
					if (ruteErAapning(rad, kol, antRader, antKolonner)) {	// Åpning?
						rute = new Aapning(kol, rad, this);	// Oppretter Aapning
						antAapninger++;					//
					} else {
						rute = new HvitRute(kol, rad, this);	// Oppretter HvitRute
					}
				} else if (bit.equals("#")) {
					rute = new SortRute(kol, rad, this);		// Oppretter SortRute
				}
				denne_raden[kol] = rute;	// Legger til ruten i raden
				kol++;	// Går til neste plass
			}
			labyrint[rad] = denne_raden;	//Legger til hele raden når ferdig
			rad++;	// Går til neste rad
		}

		for (int i = 0; i < antRader; i++) {		//
			for (int j = 0; j < antKolonner; j++) {	// Går gjennom hele arrayet
				Rute denne = labyrint[i][j];	// Finner denne ruta

				Rute nord, sor, ost, vest;	// Instanserer pekere

				// Finn naboer for rader
				if (i==0) {	// øverst
					nord = null;			//
					sor = labyrint[i+1][j];	// Pek på naboer som sjekkes
				} else if (i == antRader-1) { // nederst
					sor = null;
					nord = labyrint[i-1][j];
				} else { // innenfor
					nord = labyrint[i-1][j];
					sor = labyrint[i+1][j];
				}

				// Finn naboer for kolonner
				if (j == 0) { // venstre kant
					vest = null;
					ost = labyrint[i][j+1];
				} else if (j == antKolonner-1) { // høyre kant
					ost = null;
					vest = labyrint[i][j-1];
				} else { // innenfor
					ost = labyrint[i][j+1];
					vest = labyrint[i][j-1];
				}
				// Naboene til ruten
				Rute[] naboer = {nord,sor,ost,vest};
				denne.settNaboer(naboer);

			}
		}
	}

	/** Sjekker om en rute er en åpning
	 * @param x rad
	 * @param y kolonne
	 * @param rader antall rader
	 * @param kolonner antall kolonner
	 */
	private static boolean ruteErAapning(int x, int y, int rader, int kolonner) {
		return (x == 0 || x == rader-1 || y == 0 || y == kolonner-1);
	}

	/** Finner utvei fra en bestemt rute
	 * @param rad rad
	 * @param kol kolonne
	 * @param detaljert boolean for detaljert visning
	 */
	public ArrayList<ArrayList<Tuppel>> finnUtveiFra(int rad, int kol, boolean detaljert) {
		utveier.clear();	// Rydder listen med utveier for å kunne kjøre programmet flere ganger
		hentRute(rad,kol).finnUtvei(detaljert); // Henter ruten som er bedt om, og kaller på finnUtvei fra den
		return utveier;	// returnerer lista med utveier
	}

	/** Finner riktig rute
	 * @param rad
	 * @param kolonne
	 */
	private static Rute hentRute(int rad, int kol) {
		try {
			return labyrint[kol][rad];	// returnerer ruten på koordinatet som er gitt
		} catch (ArrayIndexOutOfBoundsException e) {	// Går til en basisrute dersom bruker taster koordinat utenfor labyrinten
			System.out.println("Koordinatene finnes ikke i labyrinten, går til failsafe (1,1)");
			return labyrint[1][1];
		}
	}

	/** Legger til en utvei
	 * @param utvei
	 */
	public static void leggTilUtvei(ArrayList<Tuppel> utvei) {
		utveier.add(utvei);
	}

	/** Finner korteste utvei av de som er laget
	 * 
	 */
	public ArrayList<Tuppel> finnKortesteUtvei() {
		ArrayList<Tuppel> kortesteUtvei = null;	
		int kortest = 10000;	// Utgangspunkt
		int teller = 0;
		for (ArrayList<Tuppel> utvei : utveier) {	// Går gjennom alle utveier som er funnet
			teller++; 
			if (utvei.size() < kortest) {	// Sjekker om utvei er kortere enn noen så langt
				kortesteUtvei = new ArrayList<>(utvei);	// Peker på den som er kortest
				kortest = utvei.size();				    // Gjør denne til foreløpig korteste
				kortestIndex = teller;
			}
		}
		return kortesteUtvei;	// Returnerer korteste utvei
	}

	
	public ArrayList<Tuppel> finnLengsteUtvei() {
		ArrayList<Tuppel> lengsteUtvei = null;
		int lengst = 0;
		for (ArrayList<Tuppel> utvei : utveier) {
			if (utvei.size() > lengst) {
				lengsteUtvei = new ArrayList<>(utvei);
				lengst = utvei.size();
			}
		}
		return lengsteUtvei;
	}

	public int hentKortesteIndex() {
		return kortestIndex;
	}

	/** Returnerer antall utveier i labyrinten
	 * 
	 */
	public int hentAntUtveier() {
		return utveier.size();
	}

	public Rute[][] hentLabyrint() {
		return labyrint;
	}

	public int antRader() {
		return antRader;
	}

	public int antKolonner() {
		return antKolonner;
	}

	@Override
	public String toString() {
		String s = "";
		s += "\nRader: " + antRader + " | Kolonner: " + antKolonner+"\n";
		for (int x = 0; x < antRader; x++) {
			for (int y = 0; y < antKolonner; y++) {
				s += labyrint[x][y].charTilTegn();
			}
			s+="\n";
		}
		return s;
	}
}
