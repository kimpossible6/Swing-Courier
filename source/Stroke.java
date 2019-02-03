import java.util.*;

public class Stroke {
	private int[] pX;
	private int[] pY;
	private int size;
	private int dTop;
	private int dBottom;
	private int dLeft;
	private int dR;
	private ArrayList<Integer> listPX;
	private ArrayList<Integer> listPY;

	public Stroke(int dTop, int dBottom, int dLeft, int dR, ArrayList<Integer> x, ArrayList<Integer> y) {
		this.listPX = x;
		this.listPY = y;
		this.size = x.size();
		this.setpX(x);
		this.setpY(y);
		this.dTop = dTop;
		this.dBottom = dBottom;
		this.dLeft = dLeft;
		this.dR = dR;
	}
	public void setpX(ArrayList<Integer> x) {
		int[] pX2 = new int[this.size];
		for (int index = 0; index < this.size; index++) {
			pX2[index] = x.get(index);
		}
		this.pX = pX2;
	}
	public void setpY(ArrayList<Integer> y) {
		int[] pY2 = new int[this.size];
		for (int index = 0; index < this.size; index++) {
			pY2[index] = y.get(index);
		}
		this.pY = pY2;
	}
	public int[] getpX() {
		return this.pX;
	}

	public int[] getpY() {
		return this.pY;
	}

	public ArrayList<Integer> getlistPX() {
		return this.listPX;
	}
	public ArrayList<Integer> getlistPY() {
		return this.listPY;
	}
	public int getSize() {
		return this.size;
	}
	public int getdTop() {
		return this.dTop;
	}

	public int getdBottom() {
		return this.dBottom;
	}

	public int getdLeft() {
		return this.dLeft;
	}
	public int getdR() {
		return this.dR;
	}

	public void getDiffX(int i, int val) {
		pX[i] = val;
		listPX.set(i, val);
	}
	public void getDiffY(int i, int val) {
		pY[i] = val;
		listPY.set(i, val);
	}
	public void dispDiff(int diffX, int diffY) {
		//difference in y
		dTop = dTop - diffY;
		dBottom =	dBottom - diffY;
		//differ in x
		dLeft = dLeft - diffX;
		dR = dR - diffX;
	}
}
