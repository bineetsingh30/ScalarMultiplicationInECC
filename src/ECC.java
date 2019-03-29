import java.util.Date;

public class ECC {
	int a;
	int b;
	int p;

	static class Point {
		int x;
		int y;
		boolean infinity;
		/*
		 *[0] = time
		 *[1] = number of adds
		 *[2] = number of doubles
		 *[3] = precomputation 
		 *[4] = hamming weight
		 */
		int[] factors = new int[5];
		
		public Point(int p1, int p2) {
			x = p1;
			y = p2;
		}

		public Point() {

		}

		@Override
		public String toString() {
			if (infinity) {
				return "O Point";
			}

			return "(" + x + ", " + y + ")";
		}
	}

	public ECC(int a, int b, int p) {
		this.a = a;
		this.b = b;
		this.p = p;
	}

	public static void main(String[] args) throws Exception {
		ECC curve = new ECC(4, 3, 607);
		Point P = new Point(234, 121);
//		Point Q = new Point(10,13);
//		Point R = new Point(234,121);

//		System.out.println(curve.add(P,Q));
//		System.out.println(curve.doubling(Q));
//		System.out.println(curve.add(R,R));
//		
//		System.out.println(Integer.MAX_VALUE);
//		System.out.println(Math.pow(2, 31));

//		System.out.println(curve.doubling(P));

		int k = 410;
//		
		long before = new Date().getTime();
		System.out.print("Naive Multiplication ==> (" + curve.NaiveMultiplication(P, k) + ")");
		long after = new Date().getTime();
		System.out.println("\t\t & \t Time taken = " + (after - before) + "ms");

		before = new Date().getTime();
		System.out.print("Binary Left To Right ==> (" + curve.BinaryLeftToRight(P, k) + ")");
		after = new Date().getTime();
		System.out.println("\t\t & \t Time taken = " + (after - before) + "ms");

		before = new Date().getTime();
		System.out.print("Binary Right To Left ==> (" + curve.BinaryRightToLeft(P, k) + ")");
		after = new Date().getTime();
		System.out.println("\t \t & \t Time taken = " + (after - before) + "ms");

		before = new Date().getTime();
		System.out.print("Binary Additon Subtraction ==> (" + curve.AdditionSubtraction(P, k) + ")");
		after = new Date().getTime();
		System.out.println("\t & \t Time taken = " + (after - before) + "ms");

		System.out.println(curve.Windowed(P, k, 3));
		
		System.out.println(curve.MontgomeryLadder(P, k));
		
		System.out.println(curve.wNAF(P, k, 3));
	}

	public Point add(Point P, Point Q) throws Exception {
		Point R = new Point();
		if (P.x == Q.x && P.y == Q.y) {
			return doubling(P);
		}

		if (P.infinity) {
			return Q;
		}

		if (Q.infinity) {
			return P;
		}

		int deltaY = P.y - Q.y;
		int deltaX = P.x - Q.x;

		if (deltaX != 0) {
			int lemda = Modulus.modDivide(deltaY, deltaX, this.p);
			if (lemda == -1) {
				throw new Exception("Not Possible");
			}
			;
			R.x = Modulus.modNegative(((lemda * lemda) - P.x - Q.x), this.p);
			R.y = Modulus.modNegative((lemda * (P.x - R.x) - P.y), this.p);
		} else {
			R.infinity = true;
		}
		return R;
	}

	public Point doubling(Point P) {
		if (P.infinity) {
			return P;
		}

		Point R = new Point();
		int lemda = Modulus.modDivide(3 * P.x * P.x + this.a, 2 * P.y, this.p);
		R.x = Modulus.modNegative(((lemda * lemda) - 2 * P.x), this.p);
		R.y = Modulus.modNegative((lemda * (P.x - R.x) - P.y), this.p);
		return R;
	}

	public Point NaiveMultiplication(Point P, int k) throws Exception {
		long beg = new Date().getTime();
		int noa = 0;
		Point R = new Point(P.x, P.y);
		for (int i = 1; i < k; i++) {
			R = add(R, P);
			noa++;
		}

		long end = new Date().getTime();
		
		R.factors[0] = (int)(end - beg);
		R.factors[1] = noa;
		R.factors[4] = Integer.bitCount(k);
		return R;
	}

	public Point BinaryLeftToRight(Point P, int k) throws Exception {
		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();
		Point Q = new Point(P.x, P.y);
		String binary = Integer.toBinaryString(k);

		for (int i = 1; i < binary.length(); i++) {
			Q = doubling(Q);
			nod++;
			if (binary.charAt(i) == '1') {
				Q = add(Q, P);
				noa++;
			}
		}
		
		long end = new Date().getTime();
		Q.factors[0] = (int)(end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[4] = Integer.bitCount(k);

		
		return Q;
	}

	public Point BinaryRightToLeft(Point P, int k) throws Exception {
		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();
		Point Q = new Point();
		Q.infinity = true;
		Point N = new Point(P.x, P.y);
		String binary = Integer.toBinaryString(k);

		for (int i = binary.length() - 1; i >= 0; i--) {
			if (binary.charAt(i) == '1') {
				Q = add(Q, N);
				noa++;
			}
			nod++;
			N = doubling(N);
		}
		
		long end = new Date().getTime();
		Q.factors[0] = (int)(end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[4] = Integer.bitCount(k);
		
		return Q;
	}

	public Point AdditionSubtraction(Point P, int k) throws Exception {
		int[] naf = NAF.toNAF(k);
		Point Q = new Point(P.x, P.y);
		for (int i = 1; i < naf.length; i++) {
			Q = doubling(Q);
			if (naf[i] == 1) {
				Q = add(Q, P);
			} else if (naf[i] == -1) {
				Point inv = new Point(P.x, -1 * P.y);
				Q = add(Q, inv);
			}
		}
		return Q;
	}

	public Point Windowed(Point P, int k, int w) throws Exception {
		int size = (int) Math.pow(2, w);
		Point[] precomputation = new Point[size];

		// 0 index
		Point Origin = new Point();
		Origin.infinity = true;
		precomputation[0] = Origin;

		// 1st index
		precomputation[1] = new Point(P.x, P.y);

		// From 2nd index to last
		Point temp = new Point(P.x, P.y);
		for (int i = 2; i < size; i++) {
			temp = add(temp, P);
			precomputation[i] = new Point(temp.x, temp.y);
		}

		String converted = Conversion.convertToBase(k, size);

		Point Q = new Point();
		Q.infinity = true;
		for (int i = 0; i < converted.length(); i++) {
			for (int times = 0; times < w; times++) {
				Q = doubling(Q);
			}
			int val = Conversion.getNum(converted.charAt(i));
			if (val > 0) {
				Q = add(Q, precomputation[val]);
			}
		}

		return Q;
	}

	public Point wNAF(Point P, int k, int w) throws Exception{
		//precomputation
		int size = (int) Math.pow(2, w);
		Point P2 = doubling(P);
		Point[] precomputation = new Point[size];
		
		precomputation[1] = new Point(P.x, P.y);
		
		for(int i = 3; i < size; i+=2) {
			precomputation[i] = add(precomputation[i - 2], P2);
		}
		
		
		int[] wnaf = NAF.toWNAF(k, w);
		Point Q = new Point();
		Q.infinity = true;
		
		for(int i = 0; i < wnaf.length; i++) {
			Q = doubling(Q);
			if(wnaf[i] != 0) {
				if(wnaf[i] > 0) {
					Q = add(Q, precomputation[wnaf[i]]);
				} else {
					int mod = -1 * wnaf[i];
					Point temp = new Point(precomputation[mod].x, -1 * precomputation[mod].y);
					Q = add(Q, temp);
				}
			}
		}
		
		
		return Q;
	}
	
	public Point MontgomeryLadder(Point P, int k) throws Exception{
		Point R0 = new Point();
		R0.infinity = true;
		
		Point R1 = new Point(P.x, P.y);
		
		
		int temp = k;
		int x = (int) (Math.pow(2, 30));
		boolean start = false;
		StringBuilder binary = new StringBuilder();
		for (int i = 30; i >= 0; i--) {
			if ((x & temp) == x) {
				start = true;
				binary.append(1);
			} else if (start) {
				binary.append(0);
			}
			x = x >> 1;
		}

		
		for (int i = 0; i < binary.length(); i++) {
			if(binary.charAt(i) == '0') {
				R1 = add(R0, R1);
				R0 = doubling(R0);
			}else {
				R0 = add(R0, R1);
				R1 = doubling(R1);
			}
			
		}
		
		return R0;
		
	}
	
}
