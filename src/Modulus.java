
public class Modulus {
	
	static class Pass{
		int a;
	}
	
	public static int modDivide(int a, int b, int m) {
		
		if(a == 0) {
			return 0;
		}
		
		if((a < 0 && b < 0) || (b < 0 && a > 0)) {
			a = -1 * a;
			b = -1 * b;
		}
		
		a = modNegative(a, m);
		
		int rv = -1;
		int inv = modInverse(b, m); 
	    if (inv == -1) 
	       return -1; 
	    else
	       rv = (inv * a) % m; 	
	    
	    return rv;
	}

	private static int modInverse(int b, int m) {
		Pass x = new Pass();
		Pass y = new Pass();
	    int g = gcdExtended(b, m, x, y); 
	    if (g != 1) 
	        return -1; 
	    return (x.a%m + m) % m; 
	}

	private static int gcdExtended(int a, int b, Pass x, Pass y) {
		if (a == 0) { 
	        x.a = 0;
	        y.a = 1; 
	        return b; 
	    } 
	    Pass x1 = new Pass();
	    Pass y1 = new Pass(); // To store results of recursive call 
	    int gcd = gcdExtended(b%a, a, x1, y1); 
	    x.a = y1.a - (b/a) * x1.a; 
	    y.a = x1.a; 
	    return gcd; 
	}
	
	public static int modNegative(int a, int p) {
		a = a % p;
		if(a < 0) {
			a = a + p;	
		}
		return a;
	}
	
}
