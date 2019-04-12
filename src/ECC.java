import java.util.Arrays;
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
		 * [0] = time [1] = number of adds [2] = number of doubles [3] = precomputation
		 * [4] = hamming weight
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

//			return "Time = " + factors[0] + ", Num of Add = " + factors[1] + ", Number of Doubles = " + factors[2] + ", Precomputation = " + factors[3] + ", Hamming Weight = " + factors[4];
			return printArray(factors);
		}
	}

	private static String printArray(int[] arr) {
		StringBuilder rv = new StringBuilder("");
		for(int i = 0; i < arr.length; i++) {
			rv.append(arr[i]+ " ");
		}
		return rv.toString();
	}
	
	public ECC(int a, int b, int p) {
		this.a = a;
		this.b = b;
		this.p = p;
	}

	public static void main(String[] args) throws Exception {

		int a = 4;
		int b = 3;
		int p = 607;
		int x = 234;
		int y = 121;
		int k = 407;

		if (args.length == 6) {
			a = Integer.parseInt(args[0]);
			b = Integer.parseInt(args[1]);
			p = Integer.parseInt(args[2]);
			k = Integer.parseInt(args[3]);
			x = Integer.parseInt(args[4]);
			y = Integer.parseInt(args[5]);
		}
		// a = 4, b = 3, p = 607, x = 234, y = 121
		ECC curve = new ECC(a, b, p);
		Point P = new Point(x, y);

//		System.out.println("Naive Multiplication ==> "+ curve.NaiveMultiplication(P, k));
		System.out.print(curve.BinaryLeftToRight(P, k));
		System.out.print(curve.BinaryRightToLeft(P, k));
		System.out.print(curve.AdditionSubtraction(P, k));
		System.out.print(curve.Windowed(P, k, 4));
		System.out.print(curve.MontgomeryLadder(P, k));
		System.out.print(curve.wNAF(P, k, 4));

		/*
		 * google.charts.load('current', {packages: ['corechart', 'bar']});
		 * google.charts.setOnLoadCallback(drawBarColors);
		 * 
		 * function drawBarColors() { var data = google.visualization.arrayToDataTable([
		 * ['Algo', 'Addition Operations'], ['Binary Left to Right Method', 18],
		 * ['Binary Right to Left Method', 19], ['Addition Subtraction Method', 6],
		 * ['Windowed Method', 8], ['Montgomery Ladder Method', 27], ['wNAF Method', 5]
		 * ]);
		 * 
		 * var options = {
		 * 
		 * chartArea: {width: '40%'}, colors: ['#b0120a'], hAxis: { title: 'Number of
		 * addition operations', minValue: 0 }, vAxis: { title: 'Algorithm' } }; var
		 * chart = new
		 * google.visualization.BarChart(document.getElementById('chart_div'));
		 * chart.draw(data, options); }
		 */

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

		R.factors[0] = (int) (end - beg);
		R.factors[1] = noa;

		System.out.println(Integer.bitCount(k));
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
		Q.factors[0] = (int) (end - beg);
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
		Q.factors[0] = (int) (end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[4] = Integer.bitCount(k);

		return Q;
	}

	public Point AdditionSubtraction(Point P, int k) throws Exception {

		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();

		int[] naf = NAF.toNAF(k);
		Point Q = new Point(P.x, P.y);
		for (int i = 1; i < naf.length; i++) {
			Q = doubling(Q);
			nod++;
			if (naf[i] == 1) {
				Q = add(Q, P);
				noa++;
			} else if (naf[i] == -1) {
				Point inv = new Point(P.x, -1 * P.y);
				Q = add(Q, inv);
				noa++;
			}
		}

		long end = new Date().getTime();
		Q.factors[0] = (int) (end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[4] = NAF.hammingWeight(naf);

		return Q;
	}

	public Point Windowed(Point P, int k, int w) throws Exception {
		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();

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
				nod++;
			}
			int val = Conversion.getNum(converted.charAt(i));
			if (val > 0) {
				Q = add(Q, precomputation[val]);
				noa++;
			}
		}

		long end = new Date().getTime();
		Q.factors[0] = (int) (end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[3] = size;
		Q.factors[4] = Integer.bitCount(k);

		return Q;
	}

	public Point wNAF(Point P, int k, int w) throws Exception {
		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();

		// precomputation
		int size = (int) Math.pow(2, w);
		Point P2 = doubling(P);
		Point[] precomputation = new Point[size];

		precomputation[1] = new Point(P.x, P.y);

		for (int i = 3; i < size; i += 2) {
			precomputation[i] = add(precomputation[i - 2], P2);
		}

		int[] wnaf = NAF.toWNAF(k, w);
		Point Q = new Point();
		Q.infinity = true;

		for (int i = 0; i < wnaf.length; i++) {
			Q = doubling(Q);
			nod++;
			if (wnaf[i] != 0) {
				if (wnaf[i] > 0) {
					Q = add(Q, precomputation[wnaf[i]]);
					noa++;
				} else {
					int mod = -1 * wnaf[i];
					Point temp = new Point(precomputation[mod].x, -1 * precomputation[mod].y);
					Q = add(Q, temp);
					noa++;
				}
			}
		}

		long end = new Date().getTime();
		Q.factors[0] = (int) (end - beg);
		Q.factors[1] = noa;
		Q.factors[2] = nod;
		Q.factors[3] = size / 2;
		Q.factors[4] = NAF.hammingWeight(wnaf);

		return Q;
	}

	public Point MontgomeryLadder(Point P, int k) throws Exception {
		int noa = 0;
		int nod = 0;
		long beg = new Date().getTime();

		Point R0 = new Point();
		R0.infinity = true;

		Point R1 = new Point(P.x, P.y);

		String binary = Integer.toBinaryString(k);

		for (int i = 0; i < binary.length(); i++) {
			if (binary.charAt(i) == '0') {
				R1 = add(R0, R1);
				R0 = doubling(R0);
			} else {
				R0 = add(R0, R1);
				R1 = doubling(R1);
			}
			noa++;
			nod++;
		}

		long end = new Date().getTime();
		R0.factors[0] = (int) (end - beg);
		R0.factors[1] = noa;
		R0.factors[2] = nod;
		R0.factors[4] = Integer.bitCount(k);

		return R0;

	}

}
