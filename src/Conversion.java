
public class Conversion {

	public static void main(String[] args) {
		System.out.println(convertToBase(282, 2));
	}

	public static String convertToBase(int d, int db) {
		int rem = 0;
		StringBuilder rv = new StringBuilder("");
		while (d != 0) {
			rem = d % db;
			rv.append(getChar(rem));
			d = d / db;
		}
		
		rv.reverse();
		return rv.toString();
	}

	private static char getChar(int num) {
		if (num >= 0 && num <= 9) 
	        return (char)(num + '0'); 
	    else
	        return (char)(num - 10 + 'A'); 
	}

	public static int getNum(char ch) {
		if(ch >= '0' && ch <= '9') {
			return ch - '0';
		} else {
			return ch + 10 - 'A';
		}
	}
}
