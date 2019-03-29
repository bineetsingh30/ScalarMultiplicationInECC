import java.util.ArrayList;
import java.util.Arrays;

public class NAF {

	public static void main(String[] args) {
		int a = 2927;
		int[] b = toWNAF(a, 2);
		System.out.println(Arrays.toString(b));
//		System.out.println(a + "=>" + toINT(b));

	}

	public static int[] toNAF(int k) {
		int x = k;
		int i = 0;
		ArrayList<Integer> rv = new ArrayList<Integer>();
		while (x > 0) {
			if (x % 2 == 1) {
				int temp = 2 - (x % 4);
				rv.add(temp);
				x = x - temp;
			} else {
				rv.add(0);
			}

			x = x / 2;
			i++;
		}

		int[] naf = new int[rv.size()];
		for (i = 0; i < naf.length; i++) {
			naf[i] = rv.get(naf.length - 1 - i);
		}

		return naf;
	}

	public static int toINT(int[] naf) {

		int rv = 0;

		int pow = 1;
		for (int i = naf.length - 1; i >= 0; i--) {
			rv = rv + naf[i] * pow;
			pow = 2 * pow;
		}

		return rv;
	}

	public static int[] toWNAF(int k, int w) {
		int d = k;
		int i = 0;
		ArrayList<Integer> rv = new ArrayList<Integer>();
		while (d > 0) {
			if (d % 2 == 1) {
				int temp = mods(d, w);
				rv.add(temp);
				d = d - temp;
			} else {
				rv.add(0);
			}

			d = d / 2;
			i++;
		}

		int[] naf = new int[rv.size()];
		for (i = 0; i < naf.length; i++) {
			naf[i] = rv.get(naf.length - 1 - i);
		}

		return naf;
	}

	private static int mods(int d, int w) {
		int num = (int)(Math.pow(2, w));
		int numby2 = num / 2;
		
		if((d%num) >= numby2) {
			return (d%num) - num;
		} else {
			return d%num;
		}
	}
}
