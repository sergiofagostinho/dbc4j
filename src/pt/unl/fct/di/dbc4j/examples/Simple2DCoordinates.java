package pt.unl.fct.di.dbc4j.examples;

public class Simple2DCoordinates {
	private int latitude;  // degrees
	private int longitude; // degrees
	
	public boolean invariant() {
		return
			(getLatitude() >= -90 && getLatitude() <= 90)
			&&
			(getLongitude() >= -180 && getLongitude() <= 180);
	}
	
	public Simple2DCoordinates(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Simple2DCoordinates() {
		this(0, 0);
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public static Simple2DCoordinates parse(String str) {
		String tokens[] = str.split(" ");
		int latitude = 
			Integer.parseInt(tokens[0].substring(0, tokens[0].length()-1));
		int longitude = 
			Integer.parseInt(tokens[1].substring(0, tokens[1].length()-1));
		Simple2DCoordinates result = new Simple2DCoordinates(latitude, longitude);
		return result;
	}
	public static boolean preParse(String str) {		
		return str.matches("(-)?(\\d)?\\d\\dº (-)?(\\d)?\\d\\dº"); // example: 45º 90º
	}
}