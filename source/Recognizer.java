import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.regex.Pattern;

public class Recognizer {
	//the Array strings of gestures
	private final static String goPrevious = "^.{0,2}+[ESC]+[WDS]+.{0,2}+$";
	private final static String goNext = "^.{0,2}+[WDS]+[ESC]+.{0,2}+$";
	// {"L_Us", "R_Us", "R_Ds", "L_Ds"};
	// {"Ls", "UPS", "Rs", "DOWNS", "Ls"};
	private final static String delete1 = "^.{0,2}+[AWD]+[DSC]+[CEB]+[NBA]+[AWD]+[WDS]+.{0,2}+$";
	private final static String select2 = "^.{0,2}+[AWD]+[NBA]+[CEB]+[DSC]+[AWD]+.{0,2}+$";
	private final static String goOverview = "^.{0,2}+[ESC]+[NBE]+.{0,2}+$";

	private Pattern previousP, nextP, delete, select;
	private Pattern overview;

	private String gestureStr;
	private String gestureRecog;

	private boolean isPrevious;
	private boolean isNext;
	private boolean isDelete;
	private boolean isMove;
	private boolean isPageFlip;
	private boolean isOverview;


	/**
    * Constructor the class, initializes all variables
    */
		public Recognizer(String gestures, boolean isPageFlip) {
				previousP = Pattern.compile(goPrevious);
				nextP = Pattern.compile(goNext);
				delete = Pattern.compile(delete1);
				select = Pattern.compile(select2);
				overview = Pattern.compile(goOverview);
				this.gestureStr = gestures;
				this.isPageFlip = isPageFlip;
				getPattern();
				getGesture();
			}
			//Get patterns
			public void getPattern() {
				List<Character> listChar = new ArrayList<>();
				char[] gestureArr = gestureStr.toCharArray();
				for (char ch: gestureArr) {
					listChar.add(ch);
				}

				for (int i = 1; i < listChar.size(); i++) {
					if (listChar.get(i).equals(listChar.get(i - 1))) {
						listChar.remove(i);
						i--;
					}
				}


				StringBuilder mergeChar = new StringBuilder(listChar.size());
				for (Character ch: listChar) {
					mergeChar.append(ch);
				}
				gestureRecog = mergeChar.toString();

			}

			//match gesture
			public void getGesture() {
				if (previousP.matcher(gestureRecog).find()) {
					isPrevious = true;
				} else if (nextP.matcher(gestureRecog).find()) {
					isNext = true;
				} else if (delete.matcher(gestureRecog).find()) {
					isDelete = true;
				} else if (select.matcher(gestureRecog).find()) {
					isMove = true;
				} else if (overview.matcher(gestureRecog).find()) {
					isOverview = true;
				}
			}

			public boolean getIsPrevious() {
				System.out.println("get right" + isPrevious);
				return this.isPrevious;
			}

			public boolean getIsNext() {
				System.out.println("get left" + isNext);
				return this.isNext;
			}

			public boolean getIsDelete() {
				System.out.println("get delete" +  isDelete);
				return this.isDelete;
			}

			public boolean getIsMove() {
				System.out.println("get Move" + isMove );
				return this.isMove;
			}

			public boolean getIsOverview() {
				System.out.println("getOverview" + isOverview);
				return this.isOverview;
			}
		}
