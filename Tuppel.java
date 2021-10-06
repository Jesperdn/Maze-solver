
class Tuppel {
	private int x;	//
	private int y;	// koordinater

	public Tuppel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "("+x+", "+y+")";
	}

	public String hentKoordinater() {
		return "("+x+", "+y+")";
	}

	public int hentx() {
		return x;
	}

	public int henty() {
		return y;
	}

	/** sammenligner to tupler
	 * @param t Tuppel som skal sammenlikges
	 */
	public boolean erLik(Tuppel t) {
		return this.hentKoordinater().equals(t.hentKoordinater());	// Sjekker om koordinatene er like
	}
}
